import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * @author Lenard Ryan S. Llarenas
 **/

public class SecantMethod {
    public static void main(String[] args) throws IOException, ScriptException {
        SecantMethod s = new SecantMethod();
        s.run();
    }

    public void run() throws ScriptException {
        Scanner myObj = new Scanner(System.in);
        System.out.println();
        System.out.println("NOTE: Don't forget the operators (i.e. +,-,*,/)");
        System.out.println("Enter equation: ");
        String eqOrig = myObj.nextLine();
        String eq = convEqSyntax(eqOrig);
        System.out.println("Enter x for Iteration -1: ");
        String x0th = myObj.nextLine();
        System.out.println("Enter x for Iteration 0: ");
        String x1st = myObj.nextLine();

        /**Java class that evaluates mathematical expressions using the javascript engine**/
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine eng = mgr.getEngineByName("JavaScript");

        double xPrev = Double.valueOf(x0th);
        double xNew = Double.valueOf(x1st);
        double x;
        double fOfX = 0;
        double fOfXPrev = 0;
        double error;


        System.out.println();
        System.out.println();
        System.out.format("%15s%15s%15s%15s%15s", "I", "x", "f(x)", "Prev f(x)", "Error");
        System.out.println();

        /**Primary loop for the iterations**/
        for(int i = -1; i<9; i++){
            /**NOTE: Math.floor was used for the truncation of all variables to 5 decimal places**/
            /**Condition for Iteration -1 **/
            if(i==-1){
                x = Math.floor(xPrev * 100000) / 100000;
                fOfX = Math.floor(((Double) eng.eval(eq.replace("x", String.valueOf(x))))* 100000) / 100000;
                System.out.format("%15s%15s%15s%15s%15s", i, String.format("%.5f", x), String.format("%.5f", fOfX), "-------", "-------");
                System.out.println();
            }
            /**Condition for Iteration 0 **/
            else if(i==0){
                x = Math.floor(xNew * 100000) / 100000;
                fOfXPrev = Math.floor(fOfX * 100000) / 100000;
                fOfX = Math.floor(((Double) eng.eval(eq.replace("x", String.valueOf(x))))* 100000) / 100000;
                error = Math.floor(Math.abs((xNew - xPrev)/xNew)* 100000) / 100000;
                System.out.format("%15s%15s%15s%15s%15s", i, String.format("%.5f", x), String.format("%.5f", fOfX), String.format("%.5f", fOfXPrev), String.format("%.5f", error));
                System.out.println();
            }
            /**Condition for succeeding iterations **/
            else {
                x = Math.floor((xNew - ((fOfX*(xNew - xPrev))/((fOfX)-(fOfXPrev))))* 100000) / 100000;
                fOfXPrev = fOfX;
                fOfX =  Math.floor(((Double) eng.eval(eq.replace("x", String.valueOf(x))))* 100000) / 100000;
                xPrev = xNew;
                xNew = x;
                error = Math.floor(Math.abs((xNew - xPrev)/xNew)* 100000) / 100000;
                System.out.format("%15s%15s%15s%15s%15s", i, String.format("%.5f", x), String.format("%.5f", fOfX), String.format("%.5f", fOfXPrev), String.format("%.5f", error));
                System.out.println();
                if((Math.abs(fOfX)<=0.001)||(Math.abs(error)<=0.001)) break;
            }
        }
    }

    private String convEqSyntax(String eqOrig){
        String newEq;
        String exponent = "";
        String replace = "";
        int i;

        /**To get the index of the '^' operator**/
        for(i=0; i<eqOrig.length(); i++){
            if(eqOrig.charAt(i)=='^') break;
        }
        /**exponent substring of the equation**/
        exponent = exponent + eqOrig.charAt(i-1) + eqOrig.charAt(i) + eqOrig.charAt(i+1);
        /**replacement substring to the exponent substring**/
        replace = replace + "Math.pow(" + eqOrig.charAt(i-1) + "," + eqOrig.charAt(i+1) +")";
        newEq = eqOrig.replace(exponent, replace);
        return newEq;
    }
}
