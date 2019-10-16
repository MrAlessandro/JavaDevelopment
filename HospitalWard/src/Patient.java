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
                    Equipe.writing.lockInterruptibly();

                    for (int i = 0; i < Equipe.team.length; i++)
                        Equipe.team[i].docLock.lockInterruptibly();

                    Thread.sleep(visitTime);

                    for (int i = 0; i < Equipe.team.length; i++)
                        Equipe.team[i].docLock.unlock();


                    Equipe.writing.unlock();
                }
                catch ( InterruptedException e)
                {
                    e.printStackTrace();
                }
                break;
            case YELLOW:
                try
                {
                    Equipe.reading.lockInterruptibly();

                    int specIndex = ThreadLocalRandom.current().nextInt(0, this.team.team.length);

                    

                    Equipe.reading.unlock();
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
