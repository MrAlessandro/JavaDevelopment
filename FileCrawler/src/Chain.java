import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Chain<E> extends LinkedList<E>
{
    private ReentrantLock listLock;
    private Condition isNotEmpty;
    private Boolean noMoreData;

    public Chain()
    {
        super();
        listLock = new ReentrantLock();
        isNotEmpty = listLock.newCondition();
        noMoreData = false;
    }

    public boolean add(E element)
    {
        boolean test;

        synchronized (this) {
            test = super.add(element);
        }

        return test;
    }

    public E poll()
    {
        E rvalue;


        synchronized (this) {
            rvalue = super.poll();
        }
        return rvalue;
    }

    public boolean isNoMoreData()
    {
        boolean retValue;
        synchronized (noMoreData)
        {
            retValue = noMoreData;
        }
        return retValue;
    }

    public void NoMoreData()
    {
        synchronized (noMoreData)
        {
            noMoreData = true;
        }
    }
}
