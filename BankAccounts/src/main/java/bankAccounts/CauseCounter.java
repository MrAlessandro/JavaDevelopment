package bankAccounts;

public class CauseCounter
{
    public static void main(String[] args)
    {
        Account test = new Account("Paolo");
        test.generateMovements();

        test.print();
    }
}
