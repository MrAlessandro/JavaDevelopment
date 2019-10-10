import java.util.concurrent.LinkedBlockingDeque;

public class PostalOffice
{
    public static void main( String[] args )
    {
        int secondRoomSize;
        Windows postalCounters;
        LinkedBlockingDeque<Runnable> firstRoom;


        if(args.length != 1)
        {
            System.err.println("Program has to be lauched with only on argument which represent te second room size");
        }

        try
        {
            // Field initialization
            secondRoomSize = Integer.parseInt(args[0]);
            firstRoom = new LinkedBlockingDeque<Runnable>();
            postalCounters = new Windows(4, secondRoomSize, firstRoom);

            // Generating a bunch of people
            for (int i = 0; i < 10; i++)
            {
                Person p = new Person(i);
                firstRoom.addLast(p);
            }

            // Every person try to enter second room
            while(!firstRoom.isEmpty())
            {
                try
                {
                    postalCounters.execute(firstRoom.takeFirst());
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            postalCounters.shutdown();
        }
        catch (NumberFormatException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
