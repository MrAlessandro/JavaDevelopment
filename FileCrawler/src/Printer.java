import java.io.File;

public class Printer
{
    public static void printInfo(File f)
    {
        System.out.println("File: " + f.getAbsolutePath());
        System.out.println("    Size: " + f.length());
        if(f.canExecute())
            System.out.println("    Executable");
        if(f.canRead())
            System.out.println("    Readable");
        if(f.canWrite())
            System.out.println("    Writable");
        System.out.println("    Last modified: " + f.lastModified());
    }
}
