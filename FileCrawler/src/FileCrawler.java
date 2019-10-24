import java.io.File;
import java.util.concurrent.locks.Condition;

public class FileCrawler
{
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1)
        {
            System.err.println("Usage: java FileCrawler s [Directory's path]");
            System.exit(1);
        };

        Chain<String> chain = new Chain<String>();
        Producer runProd = new Producer(chain, args[0]);
        Consumer runCons = new Consumer(chain);
        Thread[] consumer = new Thread[5];
        Thread producer = new Thread(runProd);
        for (int i = 0; i < consumer.length; i++)
            consumer[i] = new Thread(runCons);

        producer.start();
        for (int i = 0; i < consumer.length; i++)
            consumer[i].start();

        producer.join();
        for (int i = 0; i < consumer.length; i++)
            consumer[i].join();
    }
}
