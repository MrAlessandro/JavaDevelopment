import java.io.*;
import java.util.LinkedList;

public class ReaderWriter
{
    private static void cleanByteArray(byte[] chunk)
    {
        for (int i = 0; i < chunk.length; i++)
        {
            chunk[i] = 0;
        }
    }

    private static void reverseByteArray(byte[] chunk)
    {
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.err.println("Usage: java ReaderWriter sourceFile destFile");
            System.exit(1);
        }

        String sourceFilePath = args[0];
        String destFilePath = args[1];

        File sourceFile = new File(sourceFilePath);
        File destFile = new File (destFilePath);

        if (sourceFile.length() == 0)
        {
            System.err.println("Empty source file");
            System.exit(1);
        }

        if (destFile.length() != 0)
        {
            System.err.println("Dest file must be empty");
            System.exit(1);
        }

        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destFile))
        {
            int chunk = 100;
            int readByte;
            byte[] line = new byte[chunk];
            int off = 0;
            LinkedList<byte[]> linesList = new LinkedList<byte[]>();

            while ((readByte=inputStream.read()) != -1)
            {
                line[off] = (byte)readByte;
                if (readByte == '\n')
                {
                    byte[] tmp = new byte[off+1];

                    for (int i = 0; i < off+1; i++)
                    {
                        tmp[i] = line[i];
                        line[i] = 0;
                    }

                    linesList.add(tmp);
                    off = 0;
                }
                else
                    off++;
            }

            while ((line = (byte[]) linesList.pollLast()) != null)
            {
                outputStream.write(line);

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
