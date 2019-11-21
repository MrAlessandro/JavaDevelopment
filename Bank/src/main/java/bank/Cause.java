package bank;

public enum Cause
{
    Bonifico
        {
            public String toString()
            {
                return "Bonifico";
            }
        },
    Accredito
        {
            public String toString()
            {
                return "Accredito";
            }
        },
    Bollettino
        {
            public String toString()
            {
                return "Bollettino";
            }
        },
    F24
        {
            public String toString()
            {
                return "F24";
            }
        },
    PagoBancomat
        {
            public String toString()
            {
                return "PagoBancomat";
            }
        }
}
