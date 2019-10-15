import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Doctor
{
    public ReentrantLock docLock;
    public Condition someYellowWaiters;
    public int yellowWaitersConter;

    public Doctor()
    {
        this.docLock = new ReentrantLock();
        this.someYellowWaiters = this.docLock.newCondition();
        this.yellowWaitersConter = 0;
    }
}
