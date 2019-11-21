package bank;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movement
{
    public Date date;
    public Cause cause;

    @JsonCreator
    public Movement(@JsonProperty("date")Date date, @JsonProperty("cause") Cause cause)
    {
        this.date = date;
        this.cause = cause;
    }

    public String toString()
    {
        String ret = new String("Date: " + this.date.toString() + ";     Cause: " + this.cause.toString());
        return ret;
    }

}
