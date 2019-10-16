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

        int repeatVisit;

        if (args.length != 3)
        {
            System.err.println("Usage: Ward W [Number of white code patients], " +
                                "Y [Number of yellow code patients], " +
                                "R [Number of red code patients]");

            System.exit(1);
        }

        try
        {
            // Doctors initialization
            team = new Equipe(10);

            // Patients initialization
            whitePatients = new Patient[parseInt(args[0])];
            yellowPatients = new Patient[parseInt(args[1])];
            redPatients = new Patient[parseInt(args[2])];

            repeatVisit = 5;

            for (int i = 0; i < whitePatients.length; i++)
                whitePatients[i] = new Patient(Codes.WHITE, team, repeatVisit);
            for (int i = 0; i < yellowPatients.length; i++)
                yellowPatients[i] = new Patient(Codes.YELLOW, team, repeatVisit);
            for (int i = 0; i < redPatients.length; i++)
                redPatients[i] = new Patient(Codes.RED, team, repeatVisit);

            for (Patient whitePatient : whitePatients) whitePatient.start();
            for (Patient yellowPatient : yellowPatients) yellowPatient.start();
            for (Patient redPatient : redPatients) redPatient.start();

            for (Patient whitePatient : whitePatients) whitePatient.join();
            for (Patient yellowPatient : yellowPatients) yellowPatient.join();
            for (Patient redPatient : redPatients) redPatient.join();
        }
        catch (NumberFormatException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
