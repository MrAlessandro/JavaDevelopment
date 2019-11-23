import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Server
{
    private static int DEFAULT_PORT = 8040;
    private static int THREADS_NUM = 1;

    public static void main(String[] args)
    {
        LinkedBlockingQueue<Wrapper> readables = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Wrapper> writables = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<SelectionKey> registerables = new LinkedBlockingQueue<>();
        Assistant[] pool = new Assistant[THREADS_NUM];

        for (int i = 0; i < THREADS_NUM; i++)
        {
            pool[i] = new Assistant(readables, writables, registerables);
            pool[i].start();
        }

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

                        ByteBuffer linkBuffer = ByteBuffer.allocate(2048);

                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE).attach(linkBuffer);

                        System.out.println("Accepted connection from " + client);
                    }
                    else if (currentKey.isReadable())
                    {
                        SocketChannel client = (SocketChannel) currentKey.channel();
                        ByteBuffer linkBuffer = (ByteBuffer) currentKey.attachment();
                        currentKey.interestOps(0);

                        Wrapper toInsert = new Wrapper(client, linkBuffer, currentKey);

                        readables.put(toInsert);
                    }
                    else if (currentKey.isWritable())
                    {
                        SocketChannel client = (SocketChannel) currentKey.channel();
                        ByteBuffer linkBuffer = (ByteBuffer) currentKey.attachment();
                        currentKey.interestOps(0);

                        Wrapper toInsert = new Wrapper(client, linkBuffer, currentKey);

                        writables.put(toInsert);
                    }
                }

                SelectionKey taken;
                while ((taken = registerables.poll()) != null)
                {
                    taken.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
