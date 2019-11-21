import java.util.HashSet;
import java.util.Iterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.databind.*;


public class Report
{
    private static int Bonifico;
    private static int Accredito;
    private static int Bollettino;
    private static int F24;
    private static int PagoBancomat;

    private static Lock bonificoLock;
    private static Lock accreditoLock;
    private static Lock bollettinoLock;
    private static Lock f24Lock;
    private static Lock pagoBancomatLock;

    public static HashSet<Account> Associates;

    public Report()
    {
        Bonifico = 0;
        Accredito = 0;
        Bollettino = 0;
        F24 = 0;
        PagoBancomat = 0;

        bonificoLock = new ReentrantLock();
        accreditoLock = new ReentrantLock();
        bollettinoLock = new ReentrantLock();
        f24Lock = new ReentrantLock();
        pagoBancomatLock = new ReentrantLock();

        Associates = new HashSet<Account>();
    }

    public static void generateAccounts()
    {
        int numAccounts = ThreadLocalRandom.current().nextInt(10, 1000);
        int nameIndex;

        for (int i = 0; i < numAccounts; i++)
        {
            nameIndex = ThreadLocalRandom.current().nextInt(0, Names.NamesList.length);

            Account newAccount = new Account(Names.NamesList[nameIndex]);
            newAccount.generateMovements();

            Associates.add(newAccount);
        }

    }

    public static void backUp(Path backupPath)
    {
        Iterator<Account> assIter = Associates.iterator();
        FileChannel outChannel = null;
        ByteBuffer buffer = null;
        Account selected;
        Account[] compacted = new Account[Associates.size()];
        int index = 0;

        try
        {
            outChannel = FileChannel.open(backupPath, StandardOpenOption.CREATE,
                                          StandardOpenOption.WRITE);

            while (assIter.hasNext())
            {
                compacted[index] = assIter.next();
                index++;
            }

            ObjectMapper toSave = new ObjectMapper();
            buffer = ByteBuffer.wrap(toSave.writeValueAsBytes(compacted));

            while(buffer.hasRemaining())
            {
                outChannel.write(buffer);
            }

            buffer.clear();
            outChannel.close();

            // Reset values
            Bonifico = 0;
            Bollettino = 0;
            F24 = 0;
            PagoBancomat = 0;
            Accredito = 0;

            Associates.clear();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public void incBonifico()
    {
        bonificoLock.lock();
        Bonifico++;
        bonificoLock.unlock();;
    }

    public int getBonifico()
    {
        bonificoLock.lock();
        int tmp = Bonifico;
        bonificoLock.unlock();

        return tmp;
    }

    public void incAccredito()
    {
        accreditoLock.lock();
        Accredito++;
        accreditoLock.unlock();
    }

    public int getAcccredito()
    {
        accreditoLock.lock();
        int tmp = Accredito;
        accreditoLock.unlock();

        return tmp;
    }


    public void incBollettino()
    {
        bollettinoLock.lock();
        Bollettino++;
        bollettinoLock.unlock();
    }

    public int getBollettino()
    {
        bollettinoLock.lock();
        int tmp = Bollettino;
        bollettinoLock.unlock();

        return tmp;
    }

    public void incF24()
    {
        f24Lock.lock();
        F24++;
        f24Lock.unlock();
    }

    public int getF24()
    {
        f24Lock.lock();
        int tmp = F24;
        f24Lock.unlock();

        return tmp;
    }

    public void incPagoBancomat()
    {
        pagoBancomatLock.lock();
        PagoBancomat++;
        pagoBancomatLock.unlock();
    }

    public int getPagoBancomat()
    {
        pagoBancomatLock.lock();
        int tmp = PagoBancomat;
        pagoBancomatLock.unlock();

        return tmp;
    }
}
