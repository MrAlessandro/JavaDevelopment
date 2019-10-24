import java.io.File;
import java.util.Date;

public class Consumer implements Runnable
{
    private Chain<String> chain;

    public Consumer(Chain<String> ch)
    {
        chain = ch;
    }

    @Override
    public void run()
    {
        String currentPath;
        File currentDir;
        File currentFile;
        String[] content;

        while (true)
        {
            currentPath = chain.poll();
            if (currentPath == null)
                if (chain.isNoMoreData())
                    return;
                else
                    continue;

            currentDir = new File(currentPath);
            content = currentDir.list();

            for (String element : content)
            {
                String completePath = currentPath + "/" + element;
                currentFile = new File(completePath);
                if (currentFile.isFile())
                {
                    System.out.println("    File: " + currentFile.getAbsolutePath());
                    System.out.println("        Size: " + currentFile.length());
                    if(currentFile.canExecute())
                        System.out.println("        Executable");
                    if(currentFile.canRead())
                        System.out.println("        Readable");
                    if(currentFile.canWrite())
                        System.out.println("        Writable");

                    Date dt = new Date(currentFile.lastModified());
                    System.out.println("        Last modified: " + dt);
                }
            }
        }
    }
}
