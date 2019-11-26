import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;

public class Assistant extends Thread
{
    private LinkedBlockingQueue<Wrapper> readable;
    private LinkedBlockingQueue<Wrapper> writable;
    private LinkedBlockingQueue<SelectionKey> registerable;

    Assistant(LinkedBlockingQueue<Wrapper> rd, LinkedBlockingQueue<Wrapper> wr, LinkedBlockingQueue<SelectionKey> reg)
    {
        this.readable = rd;
        this.writable = wr;
        this.registerable = reg;
    }

    @Override
    public void run()
    {
        SocketChannel client;
        ByteBuffer buffer;
        Wrapper taken;

        while (true)
        {
            try
            {
                if ((taken = readable.poll()) != null)
                {
                    client = taken.client;
                    buffer = taken.linkBuffer;

                    int bytesRead = client.read(buffer);
                    if (bytesRead != -1)
                    {
                        registerable.put(taken.key);

                        System.out.print("Server read: ");

                        buffer.flip();

                        while(buffer.hasRemaining())
                        {
                            System.out.print((char) buffer.get());
                        }
                        System.out.print("\n");
                    }
                    else
                        client.close();
                }

                if((taken = writable.poll()) != null)
                {
                    client = taken.client;
                    buffer = taken.linkBuffer;

                    int bytesWritten = 0;
                    buffer.flip();

                    while (buffer.hasRemaining())
                        client.write(buffer);

                    buffer.clear();

                    registerable.put(taken.key);
                }
            }
            catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
