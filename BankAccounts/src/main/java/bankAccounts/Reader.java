package bankAccounts;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

public class Reader implements Runnable
{
    private Report bankReport;
    private Path backupPath;
    private LinkedBlockingQueue<Account> workChain;

    public Reader(Report rep, Path backup, LinkedBlockingQueue<Account> wc)
    {
        bankReport = rep;
        backupPath = backup;
        workChain = wc;
    }

    public void run()
    {
        FileChannel inChannel;
        String read;
        ByteBuffer buffer = null;

        try
        {
            buffer.allocate(16384);
            inChannel = FileChannel.open(backupPath, StandardOpenOption.READ);

            while((inChannel.read(buffer)) != -1 )
            {

            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
