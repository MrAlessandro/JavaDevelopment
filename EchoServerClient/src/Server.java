import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Server
{
    private static int DEFAULT_PORT = 8040;

    public static void main(String[] args)
    {
        LinkedBlockingQueue<SocketChannel> readables = new LinkedBlockingQueue<SocketChannel>();
        LinkedBlockingQueue<SocketChannel> writables = new LinkedBlockingQueue<SocketChannel>();
        LinkedBlockingQueue<SocketChannel> registerables = new LinkedBlockingQueue<SocketChannel>();

        try (Selector selector = Selector.open();
             ServerSocketChannel connectionSocket = ServerSocketChannel.open())
        {
            connectionSocket.bind(new InetSocketAddress("localhost", DEFAULT_PORT));
            connectionSocket.configureBlocking(false);
            connectionSocket.register(selector, SelectionKey.OP_ACCEPT);

            while(true)
            {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                for (SelectionKey currentKey : selectedKeys)
                {
                    selectedKeys.remove(currentKey);

                    if (currentKey.isAcceptable())
                    {
                        ServerSocketChannel server = (ServerSocketChannel) currentKey.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);

                        System.out.println("Accepted connection from " + client);
                    }
                    else if (currentKey.isReadable())
                    {
                        SocketChannel client = (SocketChannel) currentKey.channel();
                        currentKey.cancel();

                        readables.put(client);
                    }
                }
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
