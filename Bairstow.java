import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.lang.Math;

/**
 * @author John Matthew Vong
**/

public class Bairstow {
    public static void main(String[] args) {
		double r = 0.5;
		double s = -0.5;
		double deltaR;
		double deltaS;
		double error = 0.001;
		String polynomial;
		double constant1[] = new double[10];
		double constant2[] = new double[10];
		double constant3[] = new double[10];
		double temp[] = new double[10];
		Scanner scan = new Scanner(System.in);
		DecimalFormat df = new DecimalFormat("0.00000");
		df.setRoundingMode(RoundingMode.DOWN);

		System.out.println("Input the Polynomial");
		polynomial = scan.nextLine();

		System.out.println(polynomial);

		String C = "";
		int z = 0;
		int q = polynomial.length();

		//This will read the polynomial string index by index to determine whether it is a digit, decimal point, character, or operator.
		for(int i = 0; i < q; i++){
			if (i==0){
				if(polynomial.charAt(i) == '-' && polynomial.charAt(i+1) == 'x'){
					constant1[z] = -1;
					z++;
					i++;
					continue;
				}
				else if (polynomial.charAt(i) == 'x'){
					constant1[z] = 1;
					z++;
					continue;
				}
				else if (isDigit(polynomial.charAt(i))){
					C = C + polynomial.charAt(i);
					continue;
				}
				else if (negative(polynomial.charAt(i)) && isDigit(polynomial.charAt(i+1))){
					C = C + '-' + polynomial.charAt(i+1);
					i++;
					continue;
				}
			}
			else if(isDigit(polynomial.charAt(i))){
				if (i!=0){
					if(exponent(polynomial.charAt(i-1))){
						continue;
					}
					else if (negative(polynomial.charAt(i-1))){
						C = C + '-' + polynomial.charAt(i);
					}
					else {
						C = C + polynomial.charAt(i);
						continue;
					}
				}
				else {
					C = C + polynomial.charAt(i);
				}
			}
			else if (decimal(polynomial.charAt(i))){
				C = C + polynomial.charAt(i);
				continue;
			}
			else if (isChar(polynomial.charAt(i))){
				if (polynomial.charAt(i) == 'x' && negative(polynomial.charAt(i-1))){
					constant1[z] = -1;
					z++;
					continue;
				}
				else if (polynomial.charAt(i) == 'x' && operator(polynomial.charAt(i-1))){
					constant1[z] = 1;
					z++;
					continue;
				}
				else {
					constant1[z] = Double.valueOf(C);
					C = "";
					z++;
					continue;
				}
			}
			else if (operator(polynomial.charAt(i))){
				continue;
			}
			else if (space(polynomial.charAt(i))){
				continue;
			}
		}

		//Extra conversion to take care of the final coefficient without a variable
		if (!C.equals("")){
			constant1[z] = Double.valueOf(C);
			z++;
		}

		//Converting the array holding the coefficients to the proper order
		int check = -1;
		for (int i = 0; i < z; i++){
			temp[i] = constant1[i];
			check++;
		}

		for (int i = 0; i < z; i++){
			constant1[check] = temp[i];
			check--;
		}

		//Actual algorithm
		for (int i = 0; i < 12; i++){
			for (int j = 0; j < z; j++){
				if (j == 0){
					constant2[z-j-1] = constant1[z-j-1];
					constant3[z-j-1] = constant2[z-j-1];
				}
				else if (j == 1){
					constant2[z-j-1] = (constant2[z-j] * r) + constant1[z-j-1];
					constant3[z-j-1] = (constant3[z-j] * r) + constant2[z-j-1];
				}
				else if (j > 1){
					constant2[z-j-1] = constant1[z-j-1] + (constant2[z-j] * r) + (constant2[z-j+1] * s);
					constant3[z-j-1] = constant2[z-j-1] + (constant3[z-j] * r) + (constant3[z-j+1] * s);
				}
			}

			//Iteration number
			int g = i+1;
			System.out.println("Iteration: " + g);
			//Printing of R, S, and Error
			System.out.println("r: " + df.format(r) + " s: " + df.format(s) + " error: " + error);

			for (int c = 0; c < z; c++){
				System.out.print("A" + c + " = " + df.format(constant1[c]) + " ");
			}
				System.out.println(" ");
			for (int c = 0; c < z; c++){
				System.out.print("B" + c + " = " + df.format(constant2[c]) + " ");
			}
			System.out.println(" ");
			for (int c = 0; c < z; c++){
				System.out.print("C" + c + " = " + df.format(constant3[c]) + " ");
			}
			System.out.println(" ");


			// Cramer's Rule
			double determinant_orig = ((constant3[2] * constant3[2]) - (constant3[3] * constant3[1]));

			deltaR = ((-1 * constant2[1] * constant3[2]) - (constant3[3] * -1 * constant2[0])) / determinant_orig;

			deltaS = ((-1 * constant2[1]) - (constant3[2] * deltaR)) / constant3[3];

			//Printing of computed DeltaR and DeltaS
			System.out.println("Delta R: " + df.format(deltaR) + " Delta S: " + df.format(deltaS));

			s = s + deltaS;

			r = r + deltaR;

			double ErrorS = Math.abs(deltaS/s) * 100;

			double ErrorR = Math.abs(deltaR/r) * 100;

			//Printing of computed error for iteration
			System.out.println("Error R: " + df.format(ErrorR) + " Error S: " + df.format(ErrorS));

			//Ending condition
			if (ErrorS <= error && ErrorR <= error){
				double root1, root2;
				root1 = (r + Math.sqrt(r*r + 4*s))/2;
				root2 = (r - Math.sqrt(r*r + 4*s))/2;
				System.out.println("Roots are: " + df.format(root1) + " and " + df.format(root2));
				break;
			}

			//Iteration ending condition without finding the root
			else if (ErrorS > error || ErrorR > error){
				if (i == 11){
					System.out.println("Roots were not found within 12 iterations");
				}
			}

			System.out.println(" ");
		}
	}

	/* ========================================================================================*/
	/* ================================= Helper Functions ==================================== */
	/* ========================================================================================*/

	//To check whether the character at the index within the polynomial array is a digit
	public static Boolean isDigit(char x){
		if (x == '0' || x == '1' || x == '2' || x == '3'|| x == '4' || x == '5' || x == '6' || x == '7'|| x == '8' || x == '9'){
			return true;
		}
		else return false;
	}

	////To check whether the character at the index within the polynomial array is a character
	public static Boolean isChar(char x){
		if (x >= 'a' && x <= 'z' || x >= 'A' && x <= 'Z'){
			return true;
		}
		else return false;
	}

	//To check whether the character at the index within the polynomial array is an operator
	public static Boolean operator (char x){
		if (x == '+' || x == '-' || x == '/' || x == '*'){
			return true;
		}
		else return false;
	}

	//To check whether the character at the index within the polynomial array is a decimal point
	public static Boolean decimal(char x) {
		if (x == '.') {
			return true;
		}
		else return false;
	}

	//To check whether the character at the index before the current index within the polynomial array is a negative sign
	public static Boolean negative (char x){
		if (x == '-'){
			return true;
		}
		else return false;
	}

	//To check whether the character at the index before the current index within the polynomial array is an exponential sign
	public static Boolean exponent(char x) {
		if (x == '^') {
			return true;
		}
		else return false;
	}

	//To check whether the character at the index is a space or _ or " "
	public static Boolean space(char x){
		if (x == ' '){
			return true;
		}
		else return false;
	}
}
