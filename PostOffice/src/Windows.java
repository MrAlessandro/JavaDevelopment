import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Windows extends ThreadPoolExecutor
{
    private static LinkedBlockingDeque<Runnable> firstRoom;
    private static RejectedExecutionHandler rejectHandler = new RejectedExecutionHandler(){
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor)
        {
            if (!executor.isShutdown())
            {
                try
                {
                    firstRoom.putFirst(task);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    public Windows(int windowsNumber, int secondRoomSize, LinkedBlockingDeque<Runnable> fR)
    {
        super(windowsNumber, windowsNumber, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(secondRoomSize), rejectHandler);
        firstRoom = fR;
    }

}
