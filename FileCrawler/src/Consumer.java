import java.io.File;

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
            if (Thread.currentThread().isInterrupted() || (currentPath = chain.poll()) == null)
            {
                System.out.println("Consumer has been interrupted. No further directory to visit");
                return;
            }

            currentDir = new File(currentPath);
            content = currentDir.list();

            for (String element : content)
            {
                currentFile = new File(element);
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
                    System.out.println("        Last modified: " + currentFile.lastModified());
                }
            }
        }
    }
}
