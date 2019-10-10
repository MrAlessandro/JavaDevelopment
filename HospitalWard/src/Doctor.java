import java.util.concurrent.locks.ReentrantLock;

public class Doctor
{
    public ReentrantLock docLock;
    private boolean specialization;

    public Doctor(boolean spec)
    {
        this.specialization = spec;
    }
}
