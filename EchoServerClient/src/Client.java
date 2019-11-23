import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client
{
    private static int DEFAULT_PORT = 8040;

    public static void main(String[] args)
    {
        int port = DEFAULT_PORT;

        try
        {
            SocketAddress address = new InetSocketAddress("localhost", port);
            SocketChannel server = SocketChannel.open(address);
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            Scanner scanner = new Scanner(System.in);

            while (true)
            {
                System.out.println("Write some text to be echoed: ");
                String toSend = scanner.nextLine();

                if (toSend.equals("exit"))
                    break;

                buffer.put(toSend.getBytes());
                buffer.flip();

                while (buffer.hasRemaining())
                    server.write(buffer);

                buffer.clear();

                server.read(buffer);

                System.out.println("Server echoed: " + new String(buffer.array()));
            }

            server.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}