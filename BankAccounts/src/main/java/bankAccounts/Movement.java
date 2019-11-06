package bankAccounts;

import java.util.Date;

public class Movement
{
    public Date date;
    public Cause cause;

    public Movement(Date when, Cause why)
    {
        this.date = when;
        this.cause = why;
    }

    public String toString()
    {
        String ret = new String("Date: " + this.date.toString() + ";     Cause: " + this.cause.toString());
        return ret;
    }

}
