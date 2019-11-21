import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class HTTTPrequest
{
    private String requestLine;
    private HashMap<String, String> headers;
    private byte[] body;

    public HTTTPrequest(String request) throws IOException, HTTPFormatException
    {
        BufferedReader reader = new BufferedReader(new StringReader(request));
        this.headers = new HashMap<String, String>();

        this.requestLine = reader.readLine();
        if (this.requestLine == null || this.requestLine.length() == 0)
            throw new HTTPFormatException("Invalid Request-Line: " + requestLine);

        String header;
        while((header = reader.readLine()) != null)
        {
            int idx = header.indexOf(":");
            if (idx != -1)
                this.headers.put(header.substring(0, idx), header.substring(idx+1, header.length()));
        }

        String bodyLine;
        ByteArrayOutputStream builder = new ByteArrayOutputStream();
        while ((bodyLine = reader.readLine()) != null)
        {
            builder.write(bodyLine.getBytes());
        }

        body = builder.toByteArray();
    }

    public String getMethod()
    {
        String[] components = requestLine.split(" ");
        return components[0];
    }

    public String getURI()
    {
        String[] components = requestLine.split(" ");
        return components[1];
    }

    public String getStatus()
    {
        String[] components = requestLine.split(" ");
        return components[2];
    }

    public byte[] getBody()
    {
        return this.body;
    }
}
