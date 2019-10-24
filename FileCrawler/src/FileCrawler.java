import java.io.File;

public class FileCrawler
{
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java FileCrawler s [Directory's path]");
            System.exit(1);
        };

        File dir = new File(args[0]);

        if (dir.isDirectory())
        {
            String[] files = dir.list();

            for (String file : files)
            {
                if (file != "." || file != "..")
                {
                    File actualfile = new File(file);

                    if (actualfile.isDirectory())
                    {

                    }
                    else
                    {
                        Printer.printInfo(actualfile);
                    }
                }
            }
        }
        else
        {
            System.err.println("Argument must be a directory path");
            System.exit(1);
        }
    }
}
