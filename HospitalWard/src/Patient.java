import java.util.concurrent.ThreadLocalRandom;

public class Patient implements Runnable
{
    private Codes code;
    private int visitTime;
    private int nextVisitTime;
    private Equipe team;

    public Patient(Codes cd, Equipe t)
    {
        this.code = cd;
        this.team = t;
        this.visitTime = ThreadLocalRandom.current().nextInt(1, 10);
        this.nextVisitTime = ThreadLocalRandom.current().nextInt(1, 10);
    }

    @Override
    public void run()
    {
        switch (this.code)
        {
            case RED:
                try
                {
                    this.team.writing.lockInterruptibly();

                    for (int i = 0; i < this.team.team.length; i++)
                        this.team.team[i].docLock.lockInterruptibly();

                    Thread.currentThread().sleep(visitTime);

                    for (int i = 0; i < this.team.team.length; i++)
                        this.team.team[i].docLock.unlock();

                    this.team.writing.unlock();

                    this.team.writing.lockInterruptibly();
                    this.team.redWaitersCounter--;
                    this.team.writing.unlock();

                    this.team.reading.lockInterruptibly();
                    if (this.team.redWaitersCounter == 0)
                    {
                        this.team.reading.unlock();
                        this.team.noRedWaiters.signalAll();
                    }
                    else
                        this.team.reading.unlock();
                }
                catch ( InterruptedException e)
                {
                    e.printStackTrace();
                }
                break;
            case YELLOW:
                try
                {
                    this.team.reading.lockInterruptibly();
                    while (this.team.redWaitersCounter > 0)
                        this.team.noRedWaiters.await();
                    this.team.reading.unlock();

                    int specIndex = ThreadLocalRandom.current().nextInt(0, this.team.team.length);
                }
                catch ( InterruptedException e)
                {
                    e.printStackTrace();
                }
                break;
            case WHITE:
                break;
        }
    }
}
