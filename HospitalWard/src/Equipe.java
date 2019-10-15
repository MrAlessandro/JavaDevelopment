import java.util.concurrent.locks.*;

public class Equipe
{
    public static Doctor[] team;

    public static ReentrantReadWriteLock redWaitingLock;

    public static Lock reading;
    public static Condition noRedWaiters;
    public static int redWaitersCounter;


    public static Lock writing;
    public static Condition noRedVisit;
    public static boolean redInVisit;

    public Equipe(int teamSize)
    {
        team = new Doctor[teamSize];
        for (int i = 0; i < teamSize; i++)
            this.team[i] = new Doctor();

        redWaitingLock = new ReentrantReadWriteLock(true);

        reading = redWaitingLock.readLock();
        noRedWaiters = reading.newCondition();
        redWaitersCounter = 0;

        writing = redWaitingLock.writeLock();
        noRedVisit = writing.newCondition();
        redInVisit = false;

    }
}
