package jansem2.Compiler;


public class compobj
{
    //attributes
    private String varname;
    private int number;
    private String word;

    //constructors
    public compobj(String v, int n)
    {
        varname = v;
        number = n;
    }
    public compobj(String v, String w)
    {
        varname = v;
        word = w;
    }

    //default
    public compobj()
    {
        varname = "none";
        number = 0;
        word = "none";
    }

    //getters
    public String getVarname()
    {
        return varname;
    }

    public int getNumber()
    {
        return number;
    }

    public String getWord()
    {
        return word;
    }

    //setters
    public void setVarname (String x)
    {
        varname = x;
    }

    public void setNum (int x)
    {
        number = x;
    }

    public void setWord (String x)
    {
        word = x;
    }

    //printing
    public void print ()
    {
        if (number != 0)
            System.out.println(number);
        else
            System.out.println(word);
    }
}
