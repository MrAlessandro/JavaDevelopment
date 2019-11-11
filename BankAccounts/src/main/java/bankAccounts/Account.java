package bankAccounts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Account
{
    public UUID id;
    public String owner;
    public ArrayList<Movement> movements;

    public Account()
    {
        super();
    }

    public Account(String ownerName)
    {
        this.id = UUID.randomUUID();
        this.owner = ownerName;
        this.movements = new ArrayList<Movement>();
    }

    public void generateMovements()
    {
        Calendar actual = Calendar.getInstance();
        int movementListLenght = ThreadLocalRandom.current().nextInt(1, 1000);;
        Cause[] possibleCauseValues = Cause.values();

        for (int i = 0; i < movementListLenght; i++)
        {
            int year = ThreadLocalRandom.current().nextInt(actual.get(Calendar.YEAR) - 2, actual.get(Calendar.YEAR) + 1);
            int day = ThreadLocalRandom.current().nextInt(actual.get(Calendar.DAY_OF_YEAR), actual.getActualMaximum(Calendar.DAY_OF_YEAR));
            int hour = ThreadLocalRandom.current().nextInt(0, 24);
            int minute = ThreadLocalRandom.current().nextInt(0, 60);
            int second = ThreadLocalRandom.current().nextInt(0, 60);

            Calendar generated = Calendar.getInstance();
            generated.set(Calendar.YEAR, year);
            generated.set(Calendar.DAY_OF_YEAR, day);
            generated.set(Calendar.HOUR, hour);
            generated.set(Calendar.MINUTE, minute);
            generated.set(Calendar.SECOND, second);


            Cause cause = possibleCauseValues[ThreadLocalRandom.current().nextInt(0, possibleCauseValues.length - 1)];
            Date when = generated.getTime();

            this.movements.add(new Movement(when, cause));
        }

        this.movements.sort(new SortMovementByDate());
    }


    public void print()
    {
        System.out.println("Account ID: " + this.id.toString());
        System.out.println("    Owner: " + this.owner);
        System.out.println("    Movements list:");

        Iterator<Movement> iter = movements.iterator();
        Movement selected;
        while(iter.hasNext())
        {
            selected = iter.next();
            System.out.println("        " + selected.toString());
        }
    }
}
