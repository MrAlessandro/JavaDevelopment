import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Server
{
    private static int BUFF_SIZE = 64;

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("ERR -args 0");
            System.exit(1);
        }

        DatagramChannel server;
        ByteBuffer buffer;
        Random random;
        String action;
        String delay;
        int PORT = 0;

        try
        {
            PORT = Integer.parseInt(args[0]);
            if (PORT <= 0 || PORT >= 65535)
                throw new NumberFormatException();
        }
        catch (NumberFormatException e)
        {
            System.err.println("ERR -args 0");
            System.exit(1);
        }

        try
        {
            server = DatagramChannel.open();
            server.bind(new InetSocketAddress("localhost", PORT));

            random = new Random();

            buffer = ByteBuffer.allocate(BUFF_SIZE);

            while (true)
            {
                SocketAddress clientAddress = server.receive(buffer);

                buffer.flip();

                byte[] tmp = new byte[buffer.limit()];
                buffer.get(tmp, 0, buffer.limit());
                String messageReceived = new String(tmp, StandardCharsets.UTF_8);

                boolean true25 = random.nextInt(4) == 0;

                if (true25)
                {
                    action = "not sent";
                    delay = "";
                }
                else
                {
                    action = "delayed";
                    int fakeDelay = random.nextInt(200);
                    delay = "" + fakeDelay + " ms";

                    Thread.sleep(fakeDelay);

                    buffer.rewind();

                    server.send(buffer, clientAddress);

                    buffer.clear();
                }

                System.out.println(clientAddress.toString() + "> " + messageReceived + " ACTION: " + action + " " + delay);

                buffer.clear();
            }

        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
