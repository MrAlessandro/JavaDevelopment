import java.util.concurrent.ThreadLocalRandom;



public class Patient extends Thread
{
    private Codes code;
    private int visitTime;
    private int nextVisitTime;
    private int repeatVisit;
    private Equipe team;

    Patient(Codes cd, Equipe t, int times)
    {
        this.code = cd;
        this.team = t;
        this.repeatVisit = times;
        this.visitTime = ThreadLocalRandom.current().nextInt(1, 10);
        this.nextVisitTime = ThreadLocalRandom.current().nextInt(1, 10);

        Thread.currentThread().setName(Names.nameList[ThreadLocalRandom.current().nextInt(0, Names.nameList.length)]);
    }

    private void patientLog(String s)
    {
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println("    Patient: " + Thread.currentThread().getName());
        System.out.println("    Code: " + this.code.toString());
        System.out.println("             " + s);
    }

    @Override
    public void run()
    {
        while (this.repeatVisit != 0)
        {
            patientLog("Entering hospital.");

            switch (this.code)
            {
                case RED:
                    try
                    {
                        Equipe.writing.lockInterruptibly();

                        patientLog("Looking for doctors...");

                        for (int i = 0; i < Equipe.team.length; i++)
                            Equipe.team[i].docLock.lockInterruptibly();

                        patientLog("In visit...");

                        Thread.sleep(visitTime);

                        patientLog("Visit done.");

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

                        int specIndex = ThreadLocalRandom.current().nextInt(0, Equipe.team.length);

                        patientLog("Waiting for " + specIndex + "° doctor");

                        Equipe.team[specIndex].docLock.lockInterruptibly();

                        patientLog("In visit...");

                        Thread.sleep(visitTime);

                        patientLog("Visit done.");

                        Equipe.team[specIndex].docLock.unlock();

                        Equipe.reading.unlock();
                    }
                    catch ( InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case WHITE:
                    try
                    {
                        Equipe.reading.lockInterruptibly();

                        int i = 0;

                        patientLog("Looking for a doctor...");

                        while(!Equipe.team[i].docLock.tryLock())
                            i = (i + 1) % Equipe.team.length;

                        patientLog("In visit...");

                        Thread.sleep(visitTime);

                        patientLog("Visit done.");

                        Equipe.team[i].docLock.unlock();

                        Equipe.reading.unlock();
                    }
                    catch ( InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }

            patientLog("Leaving hospital.");

            this.repeatVisit--;
        }
    }
}
