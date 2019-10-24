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
            File currentDir = new File(currentDirPat);
            String[] content = currentDir.list();

            for(String f : content)
            {
                String completePath = currentDirPat + "/" + f;
                File current = new File(completePath);

                if (current.isDirectory())
                {
                    toVisit.add(current.getAbsolutePath());
                    chain.add(current.getAbsolutePath());
                }
            }
        }

        chain.NoMoreData();

        return;
    }
}
