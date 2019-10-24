import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Chain<E> extends LinkedList<E>
{
    private ReentrantLock listLock;
    private Condition isNotEmpty;

    public Chain()
    {
        super();
        listLock = new ReentrantLock();
        isNotEmpty = listLock.newCondition();
    }

    public boolean add(E element)
    {
        boolean test;

        listLock.lock();
            test = super.add(element);
            isNotEmpty.signal();
        listLock.unlock();

        return test;
    }

    public E poll()
    {
        E rvalue;

        listLock.lock();
            while (this.isEmpty())
            {
                try
                {
                    isNotEmpty.await();
                }
                catch (InterruptedException e)
                {
                    rvalue = null;
                }
            }

            rvalue = super.poll();
        listLock.unlock();

        return rvalue;
    }
}
