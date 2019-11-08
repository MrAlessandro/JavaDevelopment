package bankAccounts;

import java.nio.file.*;
import java.util.concurrent.LinkedBlockingQueue;

public class CauseCounter
{
    public static void main(String[] args)
    {
        Report rp = new Report();
        LinkedBlockingQueue<Account> transportQueue = new LinkedBlockingQueue<Account>();
        Account test = new Account("Paolo");
        test.generateMovements();
        test.print();

        rp.Associates.add(test);

        rp.backUp(Paths.get("backup"));


    }
}
