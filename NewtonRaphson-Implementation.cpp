#include<bits/stdc++.h>
#include <stdlib.h>
#define clear() printf("\033[H\033[J")
using namespace std;

/*
	author: Joseph V. Portugal
*/

double f(double poly[], int n, double x);
void newtonRaphson(double polynomial[], int n, double x);

int main() {
  
    int degree;
	double initGuess;
	
	char quit;
	bool confimQuit = false;
	
	do{
		// CLEAR SCREEN FOR LINUX
		//clear();
		// CLEAR SCREEN FOR WINDOWS
		system("CLS");
		
		// input of the degree of the polynomial to be utilized
		cout << endl << " Enter the degree of the polynomial: ";
		cin >> degree;
		cout << endl;
		
		// declaration of the array which holds the coefficients (based on the degree of the polynomial)
		double poly[degree];
		
		// input of the polynomial coefficients
		for(int i = 0; i <= degree; i++){
			cout << " Enter coefficient for the [" << degree-i << "] power: ";
			cin >> poly[i];
			cout << endl;
		}
		
		// input of the user's initial guess
		cout << " Enter your initial guess: ";
		cin >> initGuess;
		
		degree++;
		
		// function call to commence the computation of the approximate (using newton-raphson)
		newtonRaphson(poly, degree, initGuess);
	   
		// asks the user if they may wish to run the program again
		while(1){
			cout << endl << endl << " Run the program again? [Y/N] " ;
			cin >> quit; 
			if (quit == 'y' || quit == 'Y') {
				confimQuit = false;
				break;
			}
			if (quit == 'n' || quit == 'N'){
				confimQuit = true; 
				break;
			}
			else{
				// CLEAR SCREEN FOR LINUX
				//clear();
				// CLEAR SCREEN FOR WINDOWS
				system("CLS");
				
				cout << endl << " INVALID INPUT";
			}
		}
		
	} while(confimQuit == false);
	
	return 0;
}

// function to compute the value of a function given an input 'x'
double f(double poly[], int n, double x){
	
	// initially set the result to the coefficient value 	of the leading term
    double result = poly[0];
  
	// computation of the result of the applied polynomial
    for (int i=1; i < n; i++) result = result*x + poly[i];
        
    return result; 
}

// function for generation of the approximate root value using newton-raphson's approximation
void newtonRaphson(double polynomial[], int n, double x) {
	// CLEAR SCREEN FOR LINUX
	//clear();
	// CLEAR SCREEN FOR WINDOWS
	system("CLS");
	 
	// declares variable for the iteration counter
	int iter = 0;
	
	// declares variables for the previous approximation(x), the array for the coefficients of the differentiated polynomial, and the error size
    double xOld, derivativePolynomial[n-1], err = FLT_MAX;
    
    // multiplying the coefficients of our polynomial to its respective exponent and assigning that to the array elements of the differentiated polynomial array
	for(int i = 0; i < n-1; i++) derivativePolynomial[i] = polynomial[i]*(n-(i+1));
	
	// declares and sets the next approximation (x) to the function 
	double xPlusOne = (trunc( f(polynomial, n, x) * 100000)/100000)/(trunc( f(derivativePolynomial, n-1, x) * 100000) / 100000);
    
    // display the table header
    cout << endl << " " << left << setw(12) << "ITERATION" << left << setw(15) << "x" << left << setw(15) << "f(x)" << left << setw(15) <<  "f'(x)" << left << setw(15) << "ERROR" <<  endl;
    
    do {
		// display the current iteration number 
    	cout << endl << " " << left << setw(12) << iter ;
        
        // displays the current approximation
        cout << fixed << setprecision(5) << setw(15) << x;
        
        // displays the result of our function of the current approximation  
		cout << fixed << setprecision(5) << setw(15) << trunc( f(polynomial, n, x) * 100000) / 100000;
		
		// displays the result of out differentiated function of the current approximation
		cout << fixed << setprecision(5) << setw(15) << trunc( f(derivativePolynomial, n-1, x) * 100000) / 100000;
		
		// sets the number to be minused to the current approximation
        xPlusOne = (trunc( f(polynomial, n, x) * 100000)/100000)/(trunc( f(derivativePolynomial, n-1, x) * 100000) / 100000);
        
        // if it is the first iteration, skip the absolute error computation
		if (iter > 0){
			// sets the value of the current absolute error
			err = abs((x-xOld)/x);
			err = trunc(err * 100000) / 100000;
			
			// displays the value of the current absolute error
			cout << fixed << setprecision(5) << setw(15) << err << endl;
		}
		
		else cout << setw(15) << "[ N/A ]" << endl;
		
        // sets the current approximation iteration as 'xOld' (for error computation & final answer purposes)
        xOld = x;
        xOld = trunc(xOld * 100000) / 100000;
        
		// sets the value of the next approximation
        x = x - xPlusOne;
        x = trunc(x * 100000)/100000;
               
        // increments the iteration count for the next loop
        iter++;
        
    // everything is repeated until any one these conditions are not met    
    } while (abs(xPlusOne) > 0.001 && abs(err) > 0.001 && iter <= 20);
  
	// displays the final answer
    cout << endl << " = = = = =" << endl << endl << " FINAL ROOT VALUE : " << fixed << setprecision(5) << xOld; 
} 
  


