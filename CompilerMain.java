// Chloe Lin
// Period 1
// 4/18/2021
// Chloe's compiler (programming language written in Java)

package jansem2.Compiler;
import java.util.*;
import java.io.*;

public class CompilerMain
{
    public static void main (String[] args) throws FileNotFoundException
    {
        // read in file
        Scanner input = new Scanner(System.in);
        File myCode = new File("C:\\Users\\chloe\\Downloads\\myCode.txt");
        Scanner s = new Scanner(myCode);

        // variable declarations
        ArrayList <String> lines = new ArrayList<String>();
        int number1 = 0;
        int number2 = 0;
        String word1;
        String word2;
        int comparison1 = 0;
        int comparison2 = 0;
        int marker;

        // saving file to ArrayList
        while (s.hasNext())
        {
            lines.add(s.next());
        }

        // user input (ArrayList vars stores variables)
        ArrayList<compobj> vars = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).equals("input"))
            {
                // numbers (integers)
                if (lines.get(i+1).equals("number"))
                {
                    System.out.println("Enter a number.");
                    int n = input.nextInt();
                    input.nextLine();
                    vars.add(new compobj(lines.get(i+2), n));
                }
                // words (Strings)
                if (lines.get(i+1).equals("word"))
                {
                    System.out.println("Enter a word.");
                    String st = input.nextLine();
                    vars.add(new compobj(lines.get(i+2), st));
                }
            }
        }

        // doMath (math computations)
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).equals("doMath"))
            {
                for (int l = 0; l < vars.size(); l++)
                {
                    if (lines.get(i+1).equals(vars.get(l).getVarname()))
                    {
                        //dummy symbol so loop doesn't run again
                        lines.set(i, "^");
                        char dM = lines.get(i+3).charAt(0);
                        // uses ASCII to change dMint to usable value
                        int dMint = (int)dM-48;
                        char dM2 = lines.get(i+5).charAt(0);
                        // uses ASCII to change dMint2 to usable value
                        int dMint2 = (int)dM2-48;

                        // operation symbols (+, -, *, /, %)
                        if (lines.get(i+4).equals("+"))
                        {
                            vars.get(l).setNum(dMint+dMint2);
                        }
                        if (lines.get(i+4).equals("-"))
                        {
                            vars.get(l).setNum(dMint-dMint2);
                        }
                        if (lines.get(i+4).equals("*"))
                        {
                            vars.get(l).setNum(dMint*dMint2);
                        }
                        if (lines.get(i+4).equals("/"))
                        {
                            vars.get(l).setNum(dMint/dMint2);
                        }
                        if (lines.get(i+4).equals("%"))
                        {
                            vars.get(l).setNum(dMint%dMint2);
                        }
                    }
                }
            }
        }

        // iff (if statements)
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).equals("iff("))
            {
                // comparing numbers
                if (lines.get(i+1).indexOf(".length") < 0)
                {
                    for (int l = 0; l < vars.size(); l++)
                    {
                        if (lines.get(i+1).equals(vars.get(l).getVarname()))
                        {
                            number1 = vars.get(l).getNumber();
                        }
                    }
                    for (int l = 0; l < vars.size(); l++)
                    {
                        if (lines.get(i+3).equals(vars.get(l).getVarname()))
                        {
                            number2 = vars.get(l).getNumber();
                        }
                    }
                    comparison(lines, vars, i, number1, number2);
                }
                // word lengths
                else if (lines.get(i+1).indexOf(".length") > 0)
                {
                    // deleting ".length" text
                    word1 = lines.get(i+1);
                    word2 = lines.get(i+3);
                    marker = lines.get(i+1).indexOf(".length");
                    word1 = word1.substring(0, marker);
                    marker = lines.get(i+3).indexOf(".length");
                    word2 = word2.substring(0, marker);

                    // setting length of words to int values
                    for (int l = 0; l < vars.size(); l++)
                    {
                        if (word1.equals(vars.get(l).getVarname()))
                        {
                            comparison1 = vars.get(l).getWord().length();
                        }
                    }
                    for (int l = 0; l < vars.size(); l++)
                    {
                        if (word2.equals(vars.get(l).getVarname()))
                        {
                            comparison2 = vars.get(l).getWord().length();
                        }
                    }
                    comparison(lines, vars, i, comparison1, comparison2);
                    System.out.println();
                }
            }
        }

        // fLoop (for loops)
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).equals("fLoop("))
            {
                // dummy symbol so loop doesn't run again
                lines.set(i, "^");
                char parameter = lines.get(i+1).charAt(0);
                // uses ASCII to change parameterInt to usable value
                int parameterInt = (int)parameter-48;

                // printing
                if (lines.get(i+3).equals("output("))
                {
                    // printing a variable
                    for (int l = 0; l < vars.size(); l++)
                    {
                        if (lines.get(i+4).equals(vars.get(l).getVarname()))
                        {
                            for (int k = 0; k < parameterInt; k++)
                                vars.get(l).print();
                        }
                    }
                    // printing a string
                    if (lines.get(i+4).equals("\""))
                    {
                        lines.set(i+4, "^");
                        int end = outPut(lines, i+4);
                        lines.set(end, "^");
                    }
                }
            }
        }

        // simple compile errors (using the wrong if, print, and for loop declarations)
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).equals("if") || lines.get(i).equals("print") || lines.get(i).equals(";") || lines.get(i).equals("for"))
            {
                System.out.println("Cannot compile.");
            }
        }

        // commenting with <>
        for (int i = 0; i < lines.size(); i++)
        {
            if ((lines.get(i)).equals("<"))
            {
                // dummy symbol to skip over commented text
                lines.set(i, "^");
                for (int j = i + 1; j < lines.size(); j++)
                {
                    if (!lines.get(j).equals(">"))
                    {
                        lines.set(j, "^");
                    }
                    if (lines.get(j).equals(">"))
                    {
                        lines.set(j, "^");
                        break;
                    }
                }
            }
        }
    }

    // outPut method
    public static int outPut (ArrayList<String> lines, int s)
    {
        for (int i = s; i < lines.size(); i++)
        {
            if (!lines.get(i).equals("\""))
            {
                System.out.print(lines.get(i) + " ");
            }
            if (lines.get(i).equals("\""))
            {
                return i;
            }
        }
        return 0;
    }

    // comparison method
    public static void comparison (ArrayList<String> l, ArrayList<compobj> v, int i, int compare1, int compare2)
    {
        // greater than
        if (l.get(i+2).equals(">"))
        {
            if (compare1 > compare2)
            {
                // printing a variable
                for (int z = 0; z < v.size(); z++)
                {
                    if (l.get(i+6).equals(v.get(z).getVarname()))
                    {
                        v.get(z).print(); //using object method to print
                    }
                }
                // printing a string
                if (l.get(i+6).equals("\""))
                {
                    l.set(i+6, "^");
                    outPut(l, i+7);
                }
            }
        }
        // less than
        if (l.get(i+2).equals("<"))
        {
            if (compare1 < compare2)
            {
                // printing a variable
                for (int z = 0; z < v.size(); z++)
                {
                    if (l.get(i+6).equals(v.get(z).getVarname()))
                    {
                        v.get(z).print();
                    }
                }
                // printing a string
                if (l.get(i+6).equals("\""))
                {
                    l.set(i+6, "^");
                    outPut(l,i+7);
                }
            }
        }
        // equal to
        if (l.get(i+2).equals("=="))
        {
            if (compare1 == compare2)
            {
                // printing a variable
                for (int z = 0; z < v.size(); z++)
                {
                    if (l.get(i+6).equals(v.get(z).getVarname()))
                    {
                        v.get(z).print();
                    }
                }
                // printing a string
                if (l.get(i+6).equals("\""))
                {
                    l.set(i+6, "^");
                    outPut(l,i+7);
                }
            }
        }
    }
}

