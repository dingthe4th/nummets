import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
* @author Ding Bayeta IV
* reginald_geoffrey_bayetaiv@dlsu.edu.ph
**/

public class Gaussian_Solver {
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> result = new ArrayList<>();

        /* Sample input of 3x3 array | can solve NxN array*/
        double[][] input_matrix = {
                                    {-4,2,4},
                                    {1,-5,3},
                                    {2,3,-4},
                                  };
        double[] input_result = {-16,-52,50};

        for (double input : input_result) result.add(input);

        for(int i=0; i<input_matrix.length; i++) {
            matrix = new ArrayList<>(Gaussian_Solver.generateArray(input_matrix));
        }
        Answer answer = Gaussian_Solver.getFinalMatrix(matrix,result);

        Gaussian_Solver.displayMatrix(answer,true);

    }

    /**
    * @READ_ME
    *   Dear reader,
    *       There are only three public functions to this program:
    *           1. getFinalMatrix = gets the matrix, result, and values of the unknowns
    *           2. displayMatrix = display the matrix, result, and values of the unknowns
    *           3. generateArray = generates input 2D ArrayList of a given matrix
    *
    *       So let's begin with definition of terms:
    *       The following are the structure of the solution in this program
    *
    *             MATRIX           UNKNOWNS     RESULTS
    *       [00, 01, 02, ... 0n] [unknown_1] = [result_1]
    *       [10, 11, 12, ... 1n] [unknown_2] = [result_2]
    *       [20, 21, 22, ... 2n] [unknown_3] = [result_3]
    *       The entries in the matrix above are patterned to its indices.
    *       Say at index (0,0) we have '00'
    *       This follows the form 0,0 -> i,j which will be used a lot in this program
    *       i --> row, j --> column
    *
    *       The main goal of this problem is to take a given matrix and result arrays
    *       and generate the corresponding upper pyramid with values , and lower pyramid of
    *       zeroes (sorry, I don't know what it's called)
    *
    *       The expected result will be like this:
     *             MATRIX           UNKNOWNS     RESULTS
     *       [VAL, VAL, VAL, ... VAL] [unknown_1] = [result_1]
     *       [0, VAL, VAL, ... VAL] [unknown_2] = [result_2]
     *       [0, 0, VAL, ... VAL] [unknown_3] = [result_3]
     *
     *      The result will be stored in class 'Answer' that stores the ff:
     *              ArrayList<ArrayList<Double>> matrix;   -> 2D Array of matrix
     *              ArrayList<Double> result;              -> Array of results
     *              ArrayList<Double> unknowns;            -> Array of unknowns
    */

    /* ========================================================================================== */
    /* ====================================== ANSWER CLASS ====================================== */
    /* ========================================================================================== */


    static class Answer {
        ArrayList<ArrayList<Double>> matrix;
        ArrayList<Double> result;
        ArrayList<Double> unknowns;

        Answer() {
            matrix = new ArrayList<>();
            result = new ArrayList<>();
            unknowns = new ArrayList<>();
        }

        ArrayList<ArrayList<Double>> getMatrix() {
            return this.matrix;
        }

        ArrayList<Double> getResult() {
            return this.result;
        }

        ArrayList<Double> getUnknowns() {
            return this.unknowns;
        }
    }


    /* ========================================================================================== */
    /* ===================================== PUBLIC METHODS ===================================== */
    /* ========================================================================================== */


    // This method is used to display the matrix when called
    public static void displayMatrix(Answer answer, boolean showUnknowns){
        DecimalFormat df = new DecimalFormat("#.#####");

        /* Gets the dimension of the matrix */
        int dimension = answer.getMatrix().size();


        /* Checker if valid inputs or not */
        if(dimension == 0) {
            System.out.println("Empty matrix.");
            return;
        }

        /* Rounds final matrices values */
        if(showUnknowns) {
            for(int i=0 ; i < dimension; i++) {
                for(int j=0 ; j < dimension; j++) {
                    double a = new Double(df.format(answer.matrix.get(i).get(j)));
                    answer.matrix.get(i).set(j,a);
                }
                double b = new Double(df.format((answer.getResult().get(i))));
                double c = new Double(df.format((answer.getUnknowns().get(i))));

                answer.result.set(i,b);
                answer.unknowns.set(i,c);
            }
        }

        if(!showUnknowns)System.out.println("Displaying given matrix...");
        else System.out.println("Displaying final matrix...");

        for(int i = 0; i< dimension; i++) {
            System.out.println(answer.getMatrix().get(i)+" ["+answer.getResult().get(i)+"]");
        }

        /* If unknown values are prompted to be shown, display the values of unknowns */
        if(showUnknowns) {
            System.out.println("Displaying unknowns...");
            if(answer.getUnknowns().size() == 0) System.out.println("Not yet evaluated.");
            else {
                System.out.println(answer.getUnknowns());
            }
        }
    }

    // This method is used to get the corresponding answer
    public static Answer getFinalMatrix(ArrayList<ArrayList<Double>> matrix, ArrayList<Double> result) {
        int dimension = matrix.size();
        /* Initialize an answer */
        Answer ans = new Answer();

        /*
        * finalMatrix -> catch values from input matrix 2d array
        * finalResult -> catch values from input result array
        */
        ArrayList<ArrayList<Double>> finalMatrix = new ArrayList<>(matrix);
        ArrayList<Double> finalResult = new ArrayList<>(result);

        ans.matrix = new ArrayList<>(finalMatrix);
        ans.result = new ArrayList<>(finalResult);
        /* Displays the inputs of the user */
        Gaussian_Solver.displayMatrix(ans,false);

        /*
        * Flag: if the dimension != size of result
        * It means invalid input. Returns null as answer
        * */
        if(dimension != result.size()) {
            System.out.println("Invalid Input.");
            return ans;
        }

        /**
        * @MAIN_SOLUTION
        * The following code handles the main solution to
        * the Gauss Matrix Problem
        *
        * The flow of the solution is as follows:
        *   1. Get the gaussian factor -- gf
        *       How to get gf
        *           Given an input X with the index (row,col)
        *               We look to the value of the col,
        *               col of X = row and col of factor
        *           gf = X(row,col) / factor(colX,colX)
        *   2. We use gf to get the values (rowMultiplied and resultMultiplied)
        *       to be subtracted from the row that we need to update.
        *
        *       Example calculation at instance x,y
        *       Row at index x,y = [10,5,3]      Result at x,y    = [3]
        *       rowMultiplied    = [-10,14,13]   resultMultiplied = [-14]
        *       updateRow        = [0,-9,-10]    updateResult     = [17]
        *
        *       This values are then updated to the finalMatrix and finalResult
        * */

        for(int i=0; i<dimension; i++) {
            for(int j=0; j<dimension; j++) {
                /* Checks if instance i,j is part of the lower pyramid
                *  We make those part of the lower pyramid as zeroes
                * */
                if(Gaussian_Solver.isLowerTriangle(i,j)) {
                    /* 1. Get the gaussian factor - gf. */
                    double gf = finalMatrix.get(i).get(j) / finalMatrix.get(j).get(j);

                    /* 2.1 Get the row and result arrays multiplied to the gf */
                    ArrayList<Double> row = new ArrayList<>(rowMultiplied(gf,j,finalMatrix));
                    ArrayList<Double> res = new ArrayList<>(resultMultiplied(gf,i,j,finalResult));

                    /* 2.2 Subtract what you got from 2.1 to the current matrix and result */
                    row = new ArrayList<>(updateRow(finalMatrix.get(i),row));   // newRow
                    res = new ArrayList<>(updateResult(finalResult,res,i));       // newRes

                    /* 3. Update values of finalMatrix and finalResult */
                    finalMatrix.set(i,row);
                    finalResult = new ArrayList<>(res);
                }
            }
        }

        ans.matrix = new ArrayList<>(finalMatrix);
        ans.result = new ArrayList<>(finalResult);
        ans.unknowns = new ArrayList<>(Gaussian_Solver.updateUnknowns(finalMatrix,finalResult));
        return ans;
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

    /* ========================================================================================== */
    /* ===================================== PRIVATE METHODS ==================================== */
    /* ========================================================================================== */

    // This method is used to get the resulting multiplied row to be subtracted from the current row values
    private static ArrayList<Double> rowMultiplied(double gf, int row, ArrayList<ArrayList<Double>> matrix) {
        ArrayList<Double> res= new ArrayList<>(matrix.get(row));
        int dimension = res.size();

        if(dimension == 0) return res;

        for(int x=0 ; x<dimension; x++) {
            double newValue = res.get(x) * gf;
            res.set(x,newValue);
        }

        return res;
    }

    // This method is used to get the resulting multiplied result to be subtracted from the current result value
    private static ArrayList<Double> resultMultiplied(double gf, int target, int row, ArrayList<Double> result) {
        ArrayList<Double> res = new ArrayList<>(result);
        int dimension = res.size();

        if(dimension == 0) return res;

        double newValue = res.get(row) * gf;
        res.set(target, newValue);

        return res;
    }

    // This method is used to subtract the rowMultiplied from the current row values
    private static ArrayList<Double> updateRow(ArrayList<Double> rowCurrent, ArrayList<Double> rowMultiplied) {
        ArrayList<Double> resultingRow = new ArrayList<>(rowCurrent);
        if(rowCurrent.size() != rowMultiplied.size()) return resultingRow;

        for(int i=0; i<rowCurrent.size(); i++) {
            double newValue = rowCurrent.get(i) - rowMultiplied.get(i);
            resultingRow.set(i,newValue);
        }
        return resultingRow;
    }

    // This method is used to subtract the resultMultiplied from the current result value
    private static ArrayList<Double> updateResult(ArrayList<Double> resultCurrent, ArrayList<Double> resultMultiplied, int index) {
        ArrayList<Double> resultingRow = new ArrayList<>(resultCurrent);
        if(resultCurrent.size() != resultMultiplied.size()) return resultingRow;

        double difference = resultCurrent.get(index) - resultMultiplied.get(index);
        resultingRow.set(index,difference);

        return resultingRow;
    }

    // This method is used to find the unknowns of the given matrix and result arrays
    private static ArrayList<Double> updateUnknowns(ArrayList<ArrayList<Double>> matrix, ArrayList<Double> result) {
        ArrayList unk = new ArrayList();
        ArrayList<ArrayList<Double>> finalMatrix = new ArrayList<>(matrix);
        ArrayList<Double> finalResult = new ArrayList<>(result);

        int dimensions = matrix.size();

        /* Flag: Checks if valid input */
        if(dimensions != result.size()) {
            System.out.println("Invalid input.");
            return unk;
        }

        HashMap<Double, Double> unknownHash = new HashMap<>();
        /* Initialize values to unknownHash
        *  with the inputs unknown (index) : value
        * */
        for(int i=0; i<dimensions; i++) unknownHash.put((double)i,0.0);

        /**
        * @PURPOSE_OF_HASHMAP
        * What's the unknownHash map for?
        *    it stores the  (A) unknown index (mapped to) (B) the multiplier (hence, the unknown)
        *
        *    INDEX:       0      1     2
        *    Let's say, 4x^1 + 2x^2 + x^3
        *    the 4,2, and the invisible 1 are the coefficients in the matrix
        *
        *    The unknowns are the values for the variables of different powers of x'es (the multiplier)
         *
         *    Initially, we set the key values of the unknownHash -> index values from 0 to dimension
         *    and we set the values of each key as 0.0
         *
         *    We will use this hash map in order to find each unknowns per iteration
        * */

        /* Solve: We traverse the matrix at the end -> start */
        for(int i=dimensions-1; i != -1; i--) {
            /* We temporarily flag the multiplier of the end value of the matrix as Math.E */
            unknownHash.replace((double)i,Math.E);

            double sum = 0;             // sum of the left side of the equation except the target unknown
            double coefficient = 0;     // coefficient of the unknown element

            for(int j=0; j<dimensions; j++) {
                // If we meet the flag, we store its coefficient
                if(unknownHash.get((double)j) == Math.E) {
                    coefficient = finalMatrix.get(i).get(j);
                    continue;
                }

                /* We continue to sum all the other values
                *(coefficient of non unknowns * multiplier from hash)
                * except for the target unknown
                * */
                sum += (finalMatrix.get(i).get(j) * unknownHash.get((double)j));
            }

            // Gets the unknown
            double unknown = (finalResult.get(i) - sum) / coefficient;
            // Updates the value of flag (The one with the Math.E before) to the updated value
            unknownHash.replace((double)i,unknown);
        }

        // Append all values of unknownHash to the unk array
        for(double unknowns : unknownHash.values()) {
            unk.add(unknowns);
        }

        return unk;
    }

    // This method is a helper function to check if input at instance row, col is part of the lower pyramid
    private static boolean isLowerTriangle(int row, int col) {
        /*  Kindly refer to this:
        *             MATRIX           UNKNOWNS     RESULTS
        *       [00, 01, 02, ... 0n] [unknown_1] = [result_1]
        *       [10, 11, 12, ... 1n] [unknown_2] = [result_2]
        *       [20, 21, 22, ... 2n] [unknown_3] = [result_3]
        *
        *   00, 11, 22, 33 ... nn is the boundary between the lower and upper pyramid
        *   We want all values below that boundary as zeroes
        *   Given that 00 follows the form row,col
        *   The lower pyramid is obviously the indices wherein
        *   the row value is greater than the column
        * */
        return row>col;
    }
}
