import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.err.println("Specify file storage dierectory path");
            System.exit(1);
        }

        Path storagePath = Paths.get(args[0]);

        if(!Files.exists(storagePath))
        {
            System.err.println("Specified storage directory doesn't exists");
            System.exit(1);
        }

        ExecutorService pool = Executors.newFixedThreadPool(1);
        int port = 6789;

        try(ServerSocket connectionSocket = new ServerSocket())
        {
            connectionSocket.bind(new InetSocketAddress("localhost", 6789));
            while(true)
            {
                Socket clientSocket = connectionSocket.accept();
                Servant servant = new Servant(clientSocket, args[0]);
                pool.execute(servant);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
