import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.omg.CORBA.ParameterModeHolder;

public class Servant implements Runnable
{
    private SocketChannel client;
    private String storagePath;

    public Servant(SocketChannel client, String storagePath)
    {
        this.client = client;
        this.storagePath = storagePath;
    }

    @Override
    public void run()
    {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        StringBuilder strRequest = new StringBuilder();
        Path requestedRoseource;
        HTTTPrequest request;
        int bytesRead = 0;
        int bytesWritten = 0;

        try
        {
            while ((bytesRead = client.read(buffer)) != -1)
            {
                strRequest.append(new String(buffer.array()));
                buffer.clear();
            }

            buffer.clear();
            bytesRead = 0;
            request = new HTTTPrequest(strRequest.toString());

            if (request.getMethod().equals("GET"))
            {
                requestedRoseource = Paths.get(storagePath.concat(request.getURI()));

                if (Files.exists(requestedRoseource))
                {
                    FileChannel inChannel = FileChannel.open(requestedRoseource);
                    boolean stop = false;

                    buffer.put("HTTP/1.1 200 OK\r\n".getBytes());

                    while(!stop)
                    {
                        bytesRead = inChannel.read(buffer);
                        if(bytesRead == -1)
                            stop = true;
                        else
                        {
                            buffer.flip();
                            while (buffer.hasRemaining())
                            {
                                client.write(buffer);
                            }
                            buffer.clear();
                        }
                    }
                }
                else
                {
                    buffer.put("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                }
            }
        }
        catch(HTTPFormatException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
