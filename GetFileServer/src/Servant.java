import java.io.*;
import java.net.Socket;

public class Servant implements Runnable
{
    private Socket client;
    private String storagePath;

    public Servant(Socket client, String storagePath)
    {
        this.client = client;
        this.storagePath = storagePath;
    }

    @Override
    public void run()
    {
        try
        {
            DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String requestMessageLine = inFromClient.readLine();
            if(requestMessageLine == null)
                return;

            String[] tokenized = requestMessageLine.split(" ");

            if (tokenized[0].equals("GET"))
            {
                File file = new File(storagePath.concat(tokenized[1]));

                if (file.exists())
                {
                    int fileLength = (int) file.length();
                    FileInputStream inFile  = new FileInputStream(file);
                    byte[] fileInBytes = new byte[fileLength];
                    inFile.read(fileInBytes);
                    outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
                    outToClient.writeBytes("Content-Type: image/jpeg\r\n");
                    outToClient.writeBytes("Content-Length: " + fileLength + "\r\n");
                    outToClient.writeBytes("\r\n");

                    outToClient.write(fileInBytes, 0, fileLength);
                }
                else
                {
                    outToClient.writeBytes("HTTP/1.0 404 File not found\r\n\r\n");
                }
            }
            else
            {
                outToClient.writeBytes("HTTP/1.0 400 Bad request \n\r\n");
            }

            client.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
