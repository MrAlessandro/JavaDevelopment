package bank;

import java.util.Comparator;

public class SortMovementByDate implements Comparator <Movement>
{
    public int compare(Movement a, Movement b)
    {
        return a.date.compareTo(b.date);
    }
}
