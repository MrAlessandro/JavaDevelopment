import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

public class Client
{
    private static int BUFF_SIZE = 64;

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("ERR -arg 0, ERR -arg 1");
            System.exit(1);
        }

        DatagramSocket server;
        InetAddress serverAddress = null;
        byte[] recBuffer;
        byte[] sendBuffer;
        String message;
        Timestamp timestamp;
        int PORT = 0;

        try
        {
            PORT = Integer.parseInt(args[1]);
            if (PORT <= 0 || PORT >= 65535)
                throw new NumberFormatException();
        }
        catch (NumberFormatException e)
        {
            System.err.println("ERR -arg 1");
            System.exit(1);
        }

        try
        {
            serverAddress = InetAddress.getByName("localhost");
        }
        catch (IllegalArgumentException | UnknownHostException e)
        {
            System.err.println("ERR -arg 0");
            System.exit(1);
        }


        try
        {
            server = new DatagramSocket();
            server.setSoTimeout(2000);
            recBuffer = new byte[BUFF_SIZE];

            int seqN = 0;

            while (seqN < 10)
            {
                timestamp = new Timestamp(System.currentTimeMillis());
                message = new String("PING " + seqN + " " + timestamp.getTime());

                sendBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, PORT);

                server.send(sendPacket);

                try
                {
                    DatagramPacket recPacket = new DatagramPacket(recBuffer, recBuffer.length);
                    server.receive(recPacket);

                    String messageReceived = new String(recPacket.getData(), StandardCharsets.UTF_8);
                    messageReceived = messageReceived.trim();
                    String[] splinted = messageReceived.split(" ");

                    if (!splinted[0].equals("PING"))
                        System.err.println("Received invalid message: " + messageReceived);
                    else if(Integer.parseInt(splinted[1]) != seqN)
                        System.err.println("Received message out of sequence: " + messageReceived);
                    else
                        System.out.println(messageReceived + " RTT: " + (System.currentTimeMillis() - Long.parseLong(splinted[2])) + " ms");

                }
                catch (SocketTimeoutException e)
                {
                    System.out.println("\t*");
                }

                seqN++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
