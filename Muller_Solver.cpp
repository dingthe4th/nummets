/*
	author: Anaheim Magcamit
*/

#include <stdio.h>
#include <math.h>

/***********************************************************

How to calculate the root of a function using muller method?

1. Three initial guesses must be given
2. Calculate f(x) of the three initial guesses
3. Calculate the following:
	a. h0 = x1 - x0
	b. h1 = x2 - x1
	c. s0 = [f(x1) - f(x0)] / [x1 - x0]
	d. s1 = [f(x2) - f(x1)] / [x2 - x1]
	e. a = (s1 - s0) / (h1 + h0)
	f. b = a*h1 + s1
	g. c = f(x2)
4. The roots can now be determined using:
	x3 = x2 + [ (-2)*c / (b + sqrt(b^2 - 4*a*c)) ]  
						OR
	x3 = x2 + [ (-2)*c / (b - sqrt(b^2 - 4*a*c)) ]
5. Calculate the error
	error = [ (x3 - x2) / x3 ]
	
***********************************************************/

double function(double x, float a[], int degree);
void calculateRoot(float x0, float x1, float x2, float a[], int degree);

// stopping criteria
const int iterations = 100;
const float err = 0.00001;
const float fx = 0.0001;

main(){
	float x0, x1, x2, a[20];
	int degree;
	
	printf("Enter degree of equation: ");
	scanf("%i", &degree);
	
	for(int i=0; i<=degree; i++){
		printf("Enter coefficient for degree %d: ", degree-i);
		scanf("%f", &a[i]);
	}
	
	printf("\nx0: ");
	scanf("%f", &x0);
	printf("x1: ");
	scanf("%f", &x1);
	printf("x2: ");
	scanf("%f", &x2);
	
	calculateRoot(x0, x1, x2, a, degree);
}

// This function is used to calculate the value of f(x)
double function(double x, float a[], int degree){
	double val=0;
	
	for(int i=0; i<=degree; i++){
		val+=(a[i]*pow(x,degree-i));
	}
	
	return val;
}

void calculateRoot(float x0, float x1, float x2, float a[], int degree){
	double x3;
	for(int i=0; i<=iterations; ++i){
		double f0 = function(x0, a, degree);
		double f1 = function(x1, a, degree);
		double f2 = function(x2, a, degree);
		double h0 = x1 - x0;
		double h1 = x2 - x1;
		double s0 = (f1 - f0)/h0;
		double s1 = (f2 - f1)/h1;
		double a = (s1 - s0)/(h1 + h0);
		double b = (a*h1) + s1;
		double c = f2;
		
		
		if(b>0){
			x3 = x2 + ((-2*c) / (b + (sqrt(b*b - 4*a*c))));
		}
		else{
			x3 = x2 + ((-2*c) / (b - (sqrt(b*b - 4*a*c))));
		}
		
		double temp = floor(x3*100000)/100000;
		double temp2 = floor(x2*100000)/100000;
		double error = (x3 - x2)/x3;
		if(error<0){
			error = -1*error;
		}
		
		if(i==iterations){
			printf("\nMaximum Number of Iterations Reached... \n\n");
			break;
		}
		else{
			// to check if the f(x) has reached the stopping criteria, f(x) <= 0.0001
			if(f2<fx){
				break;
			}
			printf("\nIteration Number: %i \n", i+1);
			printf("Root: %.5f \n", temp);
			printf("x0: %.5f  x1: %.5f  x2: %.5f \n", x0, x1, x2);
			printf("f(x0): %.5f  f(x1): %.5f  f(x2): %.5f \n", f0, f1, f2);
			printf("h0: %.5f  h1: %.5f \n", h0, h1);
			printf("s0: %.5f  s1: %.5f \n", s0, s1);
			printf("a: %.5f  b: %.5f   c: %.5f \n", a, b, c);
			printf("Ea: %.5f \n", error);
			
			// to check if the error reached the stopping criteria, error <= 0.00001
			if( error<err || temp==x2 ){
				break;
			}
		}
		
		x0 = x1;
		x1 = x2;
		x2 = temp;
		
	}
}


