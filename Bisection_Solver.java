import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ding Bayeta IV
 * reginald_geoffrey_bayetaiv@dlsu.edu.ph
 **/

public class Bisection_Solver {
    /**
     * @PROGRAM_INFO
     *  Steps on how to get a root using Bisection Method
     *  1. Decide on the boundaries in which you think the root is present
     *      (minimum guess and maximum guess, hereinafter referred to as low and up)
     *  2. Find the middle point of low and up
     *  3. Check if value of error is less than or equal to the tolerance given
     *      If iteration = 0, skip this step (#3)
     *      If so, the algorithm will be finished
     *  4. Determine new low and up values
     *      Check if root is also found (to be discussed in getOneRoot() function)
     *  5. Continue step 1 - 4 until the following has been reached:
     *         -> program loop exceeded prescribed iteration count
     *         -> return value of #3 is true
     *         -> root is found
     *
     *  Iteration class stores all the values of each iteration
     *  It is used for display purposes only
     */

    /**
     * @HOW_TO_USE
     *  To use the program, you need to convert the given equation to
     *  a 2D array form with the following format
     *  {power,coefficient}
     *
     *  For example: x^2 + 6x + 9
     *  Convert it to 2D array it will become
     *    x^2    6x     9
     *  {{2,1},{1,6},{0,9}}
     *
     *  Please don't be confused, the format again is
     *  {power, coefficient}
     *
     *  Why not the other way around ?
     *
     *  It's because in solving for the equation given value of X
     *  we will utilize a hash map data structure.
     *
     *  Hash maps accepts key : values wherein each key is unique
     *
     *  But we know that coefficients in a given equation may repeat
     *  But we also know that the powers in an equation does not repeat
     *  since it's simplified.
     *
     *  Thus the format of the 2D array is {power, coefficient}
     *
     *  Thank you for using this program. Feel free to contact me if you have questions.
     *
     *  Best.
     * */

    public static void main(String[] args) {

        double[][] coefficient_to_power = {{2,1}, {1,-6}, {0,8}};

        HashMap<Double,Double> input = new HashMap<>(Bisection_Solver.generateHashmap(coefficient_to_power));

        ArrayList<Iteration> answer = Bisection_Solver.getOneRoot(input,-4,5,.001,10);

        Bisection_Solver.displayBisectionTable(answer);
    }

    /* ========================================================================================== */
    /* ==================================== ITERATION CLASS ===================================== */
    /* ========================================================================================== */

    static class Iteration {
        private int iteration;
        private double low;
        private double up;
        private double mid;
        private double fLow;
        private double fUp;
        private double fMid;
        private double error;

        Iteration(int iteration, double low, double mid, double up,
                  double fLow, double fMid, double fUp, double error) {
            this.iteration = iteration;
            this.low = low;
            this.up = up;
            this.mid = mid;
            this.fLow = fLow;
            this.fMid = fMid;
            this.fUp = fUp;
            this.error = error;
        }

        public int getIteration() {
            return iteration;
        }

        public double getLow() {
            return low;
        }

        public double getUp() {
            return up;
        }

        public double getMid() {
            return mid;
        }

        public double getfLow() {
            return fLow;
        }

        public double getfUp() {
            return fUp;
        }

        public double getfMid() {
            return fMid;
        }

        public double getError() {
            return error;
        }
    }

    /* ========================================================================================== */
    /* ===================================== PUBLIC METHODS ===================================== */
    /* ========================================================================================== */

    // This method displays the run through of the solution given proper input
    public static void displayBisectionTable(ArrayList<Iteration> result) {
        System.out.println("ITER\tXL\t\t\tXM\t\t\tXU\t\t\tFXL\t\t\tFXM\t\t\tFXU\t\t\tEA");
        for(int i=0; i<result.size(); i++) {
            String s = "\t";
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(5);
            df.setMaximumFractionDigits(5);
            df.setMinimumIntegerDigits(2);
            int iter = result.get(i).getIteration();
            double low = result.get(i).getLow();
            double mid = result.get(i).getMid();
            double up = result.get(i).getUp();
            double fLow = result.get(i).getfLow();
            double fMid = result.get(i).getfMid();
            double fUp = result.get(i).getfUp();
            double error = result.get(i).getError();
            System.out.println(iter+ s +df.format(low)+ s +df.format(mid)+ s +
                    df.format(up)+ s +df.format(fLow)+ s +df.format(fMid)+ s +
                    df.format(fUp)+ s +df.format(error));
        }
    }

    // This method generates the hash map that represents the equation
    public static HashMap<Double,Double> generateHashmap(double[][] inputArray) {
        HashMap<Double,Double> inputHashMap = new HashMap<>();

        if(!Bisection_Solver.isValidInputArray(inputArray)) {
            System.out.println("Invalid input!");
            return inputHashMap;
        }

        for(int i = 0; i < inputArray.length; i++) {
            ArrayList<Double> temp = new ArrayList<>();
            for (int input = 0; input < 2; input++) {
                temp.add(inputArray[i][input]);
            }
            inputHashMap.put(temp.get(0), temp.get(1));
        }
        return inputHashMap;
    }

    // This method gets the root if present between lowerEstimate and upperEstimate values
    public static ArrayList<Iteration> getOneRoot(HashMap<Double,Double> equationHash,
                                    double lowerEstimate, double upperEstimate,
                                     double tolerance, int iteration) {
        ArrayList<Iteration> iterationsValues = new ArrayList<>();

        /*====Initializing values====*/
        double error = 1.00;
        int iter = 1;

        double low = lowerEstimate;
        double mid = 0;
        double old_up = upperEstimate;
        double up = upperEstimate;
        /*===========================*/

        while (iter <= iteration && error > tolerance) {
            /* Get the mid point */
            double old_mid_value = mid;
            mid = getNewEstimateRoot(low, up);

            /* Get the error */
            error = getAbsRelativeError(old_mid_value,mid);

            /* Check if error is within the tolerance range */
            if(iter!=0) {
                /* Gets the values of functions of low, mid, and up for display purposes */
                double fLow = functionSolver(equationHash,low);
                double fMid = functionSolver(equationHash,mid);
                double fUp = functionSolver(equationHash,old_up);
                iterationsValues.add(new Iteration(iter,low,mid,up,fLow,fMid,fUp,error));

                /* The actual checking */
                if(error < tolerance || error == tolerance) {
                    return iterationsValues;
                }
            }

            else {
                /* Gets the values of functions of low, mid, and up for display purposes */
                double fLow = functionSolver(equationHash,low);
                double fMid = functionSolver(equationHash,mid);
                double fUp = functionSolver(equationHash,old_up);
                iterationsValues.add(new Iteration(iter,low,mid,up,fLow,fMid,fUp,-1.0));
            }

            /* Check if mid point is a root, stop the algorithm if found */
            if(isEstimateRootCorrect(equationHash,low,mid)) {
                return iterationsValues;
            }

            /* Determine new lower and upper estimate value */
            old_up = up;
            up = getNewUpperEstimate(equationHash,low,mid,up);

            if(up == old_up) low = mid;

            /* Iteration will increase and repeat the process */
            iter++;
        }
        return iterationsValues;
    }

    /* ========================================================================================== */
    /* ==================================== PRIVATE METHODS ===================================== */
    /* ========================================================================================== */

    // This method is used to get the absolute relative error between the old and new mid point value
    private static double getAbsRelativeError(double oldXm, double newXm) {
        return Math.abs((newXm-oldXm)/newXm) * 100;
    }

    // This method is used to get the new upper estimate per iteration
    private static double getNewUpperEstimate(HashMap<Double,Double> equationHash, double lower, double middle, double upper) {
        double f_of_lower = functionSolver(equationHash,lower);
        double f_of_middle = functionSolver(equationHash,middle);
        if((f_of_lower * f_of_middle) < 0) return middle;
        return upper;
    }

    // This method is used to get the new middle estimate per iteration
    private static double getNewEstimateRoot(double lower, double upper) {
        return (lower+upper) / 2;
    }

    // This method is used to check if a root is found
    private static boolean isEstimateRootCorrect(HashMap<Double,Double> equationHash,double lower, double middle) {
        double function_of_lower = functionSolver(equationHash,lower);
        double function_of_middle = functionSolver(equationHash,middle);
        return function_of_lower * function_of_middle == 0;
    }

    /**
    * @Function_Solver
    * This method is used to solve an equation given the value of the variable
     * For example:
     *      Given the equation:
     *      x^2 + 2x + 1
     * @param rootEstimate represent the x
     *      Let rootEstimate = -1
     *      We know with our algebra that the root is -1
     *      (-1)^2 + 2(-1) + 1 = 0
     *      This function will then return 0
    * */
    private static double functionSolver(HashMap<Double,Double> equationHash, double rootEstimate) {
        double sum = 0.;

        for(Double power : equationHash.keySet()) {
            sum += equationHash.get(power) * Math.pow(rootEstimate,power);
        }

        return sum;
    }

    // This method is used to check if the input 2D array of values is valid
    private static boolean isValidInputArray(double[][] inputArray) {
        boolean valid = true;

        int count = 0;

        for(int row = 0; row < inputArray.length; row++) {
            for(int col = 0; col < inputArray[row].length; col++) {
                count ++;
            }
            if(count != 2) {
                valid = false;
                break;
            }
            count = 0;
        }

        return valid;
    }
}
