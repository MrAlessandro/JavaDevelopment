import java.util.concurrent.ThreadLocalRandom;

public class Person implements Runnable
{
    private int ticketNum;

    public Person(int num)
    {
        this.ticketNum = num;
    }

    public void run()
    {
        int workload = ThreadLocalRandom.current().nextInt(1, 10);

        try
        {
            Thread.sleep(workload * 1000);
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println("Person with tiket N° " + this.ticketNum + " has been served");
    }
}
