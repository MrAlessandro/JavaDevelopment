import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Integer.parseInt;

public class Ward
{
    public static void main(String[] args)
    {
        // Doctors
        Equipe team;

        // Patients
        Patient[] whitePatients;
        Patient[] yellowPatients;
        Patient[] redPatients;

        if (args.length != 3)
        {
            System.err.println("Usage: Ward W [Number of white code patients], " +
                    "Y [Number of yellow code patients], " +
                    "R [Number of red code patients]");
        }

        try
        {
            // Doctors initialization
            team = new Equipe(10);

            // Patients initialization
            whitePatients = new Patient[parseInt(args[0])];
            yellowPatients = new Patient[parseInt(args[1])];
            redPatients = new Patient[parseInt(args[2])];
            for (int i = 0; i < whitePatients.length; i++)
                whitePatients[i] = new Patient(Codes.WHITE, team);
            for (int i = 0; i < yellowPatients.length; i++)
                yellowPatients[i] = new Patient(Codes.YELLOW, team);
            for (int i = 0; i < redPatients.length; i++)
                redPatients[i] = new Patient(Codes.RED, team);

        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
}
