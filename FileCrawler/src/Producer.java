import java.io.File;
import java.util.LinkedList;

public class Producer implements Runnable
{
    private String path;
    private static Chain<String> chain;
    private LinkedList<String> toVisit;

    public Producer(Chain<String> c, String pth)
    {
        path = pth;
        chain = c;
        toVisit = new LinkedList<String>();
    }

    @Override
    public void run()
    {
        chain.add(path);
        toVisit.add(path);

        while (!toVisit.isEmpty())
        {
            String currentDirPat = toVisit.poll();
            File current = new File(currentDirPat);
            String[] content = current.list();

            for(String f : content)
            {
                current = new File(f);

                if (current.isDirectory())
                {
                    toVisit.add(current.getAbsolutePath());
                    chain.add(current.getAbsolutePath());
                }
            }
        }
    }
}
