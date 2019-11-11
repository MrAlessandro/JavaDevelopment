package bankAccounts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Restorer implements Runnable
{
    private Path backupPath;
    private LinkedBlockingQueue<Account> workChain;

    public Restorer(Path backup, LinkedBlockingQueue<Account> wc)
    {
        backupPath = backup;
        workChain = wc;
    }

    public void run()
    {
        FileChannel inChannel;
        ByteBuffer buffer = null;
        byte[] read;

        try
        {
            ObjectMapper mapper;
            boolean stop = false;
            int test;
            int bytesRead = 0;


            buffer = ByteBuffer.allocate(16384);
            inChannel = FileChannel.open(backupPath, StandardOpenOption.READ);

            read = new byte[(int)inChannel.size()];


            while(!stop)
            {
                test = inChannel.read(buffer);
                if(test == -1)
                    stop = true;
                else
                {
                    buffer.flip();
                    while(buffer.hasRemaining())
                    {
                        read[bytesRead] = buffer.get();
                        bytesRead++;
                    }
                    buffer.clear();
                }
            }

            inChannel.close();

            mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Account[] parsed = mapper.readValue(new String(read), Account[].class);

            for (int i = 0; i < parsed.length; i++)
            {
                workChain.add(parsed[i]);
            }

            workChain.add(new Account(null));

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
