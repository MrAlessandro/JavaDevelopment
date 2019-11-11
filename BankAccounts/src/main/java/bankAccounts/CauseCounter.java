package bankAccounts;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;

public class CauseCounter
{
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.err.println("Specify backup file path as argument of program");
            System.exit(1);
        }

        Report bankReport;
        LinkedBlockingQueue<Account> workChain;
        Restorer restorer;
        CauseChecker checker;
        Thread[] checkersThreads;
        Thread restorerThread;

        try
        {
            Path backuPath = Paths.get(args[0]);

            bankReport = new Report();
            workChain = new LinkedBlockingQueue<Account>();

            restorer = new Restorer(backuPath, workChain);
            checker = new CauseChecker(bankReport, workChain);

            checkersThreads = new Thread[10];
            for (int i = 0; i < checkersThreads.length; i++)
            {
                checkersThreads[i] = new Thread(checker);
            }
            restorerThread = new Thread(restorer);

            // Initialize data
            Report.generateAccounts();
            Report.backUp(backuPath);

            // Start threads
            restorerThread.start();
            for (int i = 0; i < checkersThreads.length; i++)
            {
                checkersThreads[i].start();
            }

            restorerThread.join();
            for (int i = 0; i < checkersThreads.length; i++)
            {
                checkersThreads[i].join();
            }

        }
        catch (InvalidPathException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
