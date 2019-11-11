package bankAccounts;

import java.util.concurrent.LinkedBlockingQueue;

public class CauseChecker implements Runnable
{
    private Report bankReport;
    private LinkedBlockingQueue<Account> workChain;

    public CauseChecker(Report rep, LinkedBlockingQueue<Account> wc)
    {
        bankReport = rep;
        workChain = wc;
    }

    public void run()
    {
        Account selected;
        boolean stop = false;

        while(!stop)
        {
            try
            {
                selected = workChain.take();
                if (selected.owner == null)
                {
                    workChain.add(selected);
                    stop = true;
                }
                else
                {
                    Movement[] tmp = (Movement[])selected.movements.toArray();

                    for (int i = 0; i < tmp.length; i++)
                    {
                        switch (tmp[i].cause){
                        case Bonifico:
                            bankReport.incBonifico();
                            break;
                        case Accredito:
                            bankReport.incAccredito();
                            break;
                        case Bollettino:
                            bankReport.incBollettino();
                            break;
                        case F24:
                            bankReport.incF24();
                            break;
                        case PagoBancomat:
                            bankReport.incPagoBancomat();
                            break;
                        }
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        return;
    }
}
