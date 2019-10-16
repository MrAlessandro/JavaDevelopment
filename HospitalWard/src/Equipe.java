import java.util.concurrent.locks.*;

class Equipe
{
    static Doctor[] team;

    private static ReentrantReadWriteLock redWaitingLock;

    static Lock reading;
    static Lock writing;


    Equipe(int teamSize)
    {
        team = new Doctor[teamSize];
        for (int i = 0; i < teamSize; i++)
            team[i] = new Doctor();

        redWaitingLock = new ReentrantReadWriteLock(true);

        reading = redWaitingLock.readLock();

        writing = redWaitingLock.writeLock();

    }
}
