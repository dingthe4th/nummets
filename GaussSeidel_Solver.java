import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ding Bayeta IV
 * reginald_geoffrey_bayetaiv@dlsu.edu.ph
 **/

public class GaussSeidel_Solver {
    /**
     * @PROGRAM_INFO
     *  Steps on how to get a root using Bisection Method
     *  0. Make sure that the input matrix is diagonally dominant
     *  1. Decide on your initial guess, tolerance value, and max iteration value
     *  2. For each unknown 'x' in the range 'i' =  0 ... number of unknowns
     *      Solve for the estimate new value of guess by the following formula
     *          x[0] = (result[0] - a[0,1] * x[1] - a[0,2] * x[2] - ... a[0,n] * x[n]) / a[0,0]
     *          wherein x is the new estimated value of unknown x at index 0 (first row at matrix)
     *                  result[0] is the corresponding value of a first row of the matrix
     *                  a is the coefficient at a given index (values in the matrix given index row,col)
     *  3. Check if value of error is less than or equal to the tolerance given,
     *      The algorithm shall proceed to stop if and only if all error
     *      values are less than or equal to the tolerance value OR
     *      it reached the limit iteration
     *
     *  Iteration class stores all the values of each iteration
     *  It is used for display purposes only
     */

    /**
     * @HOW_TO_USE
     *  To use the program, the following inputs are required:
     *      2D Array Matrix
     *      Result array
     *      Guess array
     *
     *      Because solving matrices comes in the form:
     *          [matrix] [unknown] = [result]
     *
     *  To have a valid input, you need to make sure that:
     *      row of matrix = col of matrix
     *      size of result = row of matrix = col of matrix
     *      size of initial guess = size of result
     *      AND the matrix is DIAGONALLY DOMINANT
     *
     *  Thank you for using this program. Feel free to contact me if you have questions.
     *
     *  Best.
     * */
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> result = new ArrayList<>();
        ArrayList<Double> guess = new ArrayList<>();

        /*
         * Sample inputs of 3x3 array
         * */
        double[][] input_matrix = {
                {10,-4,3},
                {5,7,-1},
                {2,5,-9}
        };
        double[] input_result = {40,40,-59};
        double[] initial_guess = {1.0,1.0,1.0};

        for (double input : input_result)  result.add(input);
        for (double input : initial_guess) guess.add(input);

        for(int i=0; i<input_matrix.length; i++) {
            matrix = new ArrayList<>(GaussSeidel_Solver.generateArray(input_matrix));
        }
        Iteration answer = GaussSeidel_Solver.getFinalUnknowns(matrix,result,guess,0.0001,15);
        GaussSeidel_Solver.displayBisectionTable(answer,answer.iteration);
    }

    /* ========================================================================================== */
    /* ==================================== ITERATION CLASS ===================================== */
    /* ========================================================================================== */

    static class Iteration {
        private ArrayList<Double> unknownList;
        private HashMap<Integer,ArrayList<Double>> unknownHashMap;
        private HashMap<Integer,ArrayList<Double>> errorHashMap;
        private int iteration;

        Iteration(ArrayList<Double> initialGuess) {
            this.unknownList = new ArrayList<>(initialGuess);
            this.errorHashMap = new HashMap<>();
            this.unknownHashMap = new HashMap<>();
        }

        /*  Updates the current error hash map */
        private void updateErrorHashMap(int index, ArrayList<Double> updatedList) {
            ArrayList<Double> updatedUnknownList = new ArrayList<>(updatedList);
            this.errorHashMap.put(index,updatedUnknownList);
        }

        /*  Updates the current unknown hash map */
        private void updateUnknownHashMap(int index, ArrayList<Double> updatedList) {
            ArrayList<Double> updatedUnknownList = new ArrayList<>(updatedList);
            this.unknownHashMap.put(index,updatedUnknownList);
        }

        /*  Sets iteration value */
        private void updateIteration(int iteration) {
            this.iteration = iteration;
        }
    }

    /* ========================================================================================== */
    /* ===================================== PUBLIC METHODS ===================================== */
    /* ========================================================================================== */

    // This method displays the run through of the solution given proper input
    public static void displayBisectionTable(Iteration result, int iteration) {
        /* Displays the header with the format X1 E1 ... XN EN* */
        String s = "\t";
        System.out.print("ITER" + (s));
        for(int i=0; i<result.unknownList.size(); i++) {
            System.out.print("X"+ (i+1) + s+s+s + "E" + (i+1) + s+s+s);
        }
        System.out.println();

        /* Displays the values of the column : X1 E1 ... XN EN* */
        for(int i=0; i<iteration; i++) {
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(5);
            df.setMaximumFractionDigits(5);
            df.setMinimumIntegerDigits(2);
            System.out.print(i+1);
            for(int j=0; j<result.unknownList.size(); j++) {
                double unk = result.unknownHashMap.get(i).get(j);
                double err = result.errorHashMap.get(i).get(j);
                System.out.print(s + df.format(unk) + s + df.format(err));
            }
            System.out.println();
        }

        /* Displays the final unknown values X1 ... XN */
        System.out.println("Final unknown values: " + result.unknownList);
    }

    // This method is used to generate the input matrix to be used as given in this program
    public static ArrayList<ArrayList<Double>> generateArray(double[][] input) {
        /*
         * The following method is just converting the standard c++
         * 2D array - [][] to 'Java style' 2D ArrayList
         * */
        ArrayList<ArrayList<Double>> ret = new ArrayList<>();
        ArrayList<Double> temp = new ArrayList<>();
        for(int i=0; i<input.length ;i++) {
            for(int j=0; j<input.length; j++) {
                temp.add(input[i][j]);
            }
            ret.add(temp);
            temp = new ArrayList<>();
        }
        return ret;
    }

    // This method is used to get the values of the unknowns (The GaussSeidel Algorithm)
    private static Iteration getFinalUnknowns(ArrayList<ArrayList<Double>> matrix, ArrayList<Double> result,
                                              ArrayList<Double> guess, double tolerance, int iteration) {
        Iteration iterationValues = new Iteration(guess);

        /*====Initializing values====*/
        int iter = 0;
        /*===========================*/

        while (iter < iteration) {
            ArrayList<Double> temp_unknowns = new ArrayList<>();
            ArrayList<Double> temp_errors = new ArrayList<>();
            for(int i=0; i<matrix.size(); i++) {
                double newEstimate = getNewEstimate(matrix,i,iterationValues.unknownList,result.get(i));
                double oldEstimate = iterationValues.unknownList.get(i);

                temp_unknowns.add(newEstimate);
                temp_errors.add(getAbsRelativeError(oldEstimate,newEstimate));

                iterationValues.unknownList.set(i,newEstimate);
            }


            iterationValues.updateErrorHashMap(iter,temp_errors);
            iterationValues.updateUnknownHashMap(iter,temp_unknowns);



            if(stopAlgorithmFlagged(iterationValues.errorHashMap,iter,tolerance)) {
                iter++;
                iterationValues.updateIteration(iter);
                break;
            }

            iter++;
        }

        iterationValues.updateIteration(iter);
        return iterationValues;
    }


    /* ========================================================================================== */
    /* ===================================== PRIVATE METHODS ==================================== */
    /* ========================================================================================== */

    // This method is used to get a new estimate of the unknown using the index of the diagonal/determinant
    private static double getNewEstimate(ArrayList<ArrayList<Double>> matrix, int index, ArrayList<Double> guessList, double result) {
        /**
         * @Getting_new_estimate
         *  row,col = index,index (the diagonals of the matrix)
         *  Getting estimate = (result - [summation] (coefficients * currentGuess^))
         *                      / coefficient@row,col
         *  ^ ---- summation does not include coefficient@row,col since it will be the denominator
         * */

        double newEstimate = 0;
        double demominator = matrix.get(index).get(index);
        double summation = 0;


        for(int j=0; j<matrix.size(); j++) {
            /* If we encounter the determinant index, we ignore it since it's the denominator */
            if(j == index) {
                continue;
            }

            double current_guess = guessList.get(j);
            summation += (-1*(matrix.get(index).get(j)) * current_guess);
        }

        newEstimate = (result + summation) / demominator;

        newEstimate = (newEstimate*100000d) / 100000d;

        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.FLOOR);
        newEstimate = new Double(df.format(newEstimate));
        return newEstimate;
    }

    // This method is used to check if we need to stop the algorithm or not based on the value of the errors per current unknown values
    private static boolean stopAlgorithmFlagged(HashMap<Integer,ArrayList<Double>> errorHashMap, int index, double tolerance) {
        ArrayList<Double> currentErrorList = new ArrayList<>(errorHashMap.get(index));
        /* The algorithm shall proceed to stop if and only if all error
        values are less than or equal to the tolerance value */
        for(int i=0; i<currentErrorList.size(); i++) {
            if(currentErrorList.get(i) > tolerance) {
                return false;
            }
        }

        return true;
    }

    // This method is used to get the absolute relative error between the old and new mid point value
    private static double getAbsRelativeError(double oldXm, double newXm) {
        double OLD = (oldXm*100000) / 100000;
        double NEW = (newXm*100000) / 100000;
        double ABS = Math.abs((NEW-OLD)/NEW);
        ABS = (ABS*100000) / 100000;

        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.FLOOR);
        ABS = new Double(df.format(ABS));
        return ABS;
    }
}
