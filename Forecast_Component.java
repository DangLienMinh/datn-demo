import java.util.ArrayList;

import java.util.Scanner;

//Chuong trinh du doan theo hoi quy don bien
public class Forecast_Component {
	public double[][] m; // MATRIX OF CO-EFFICIENTS
	public double[] constants; // VECTOR OF CONSTANT TERMS
	public double[] solution; // SOLUTION SET
	public int numEq = 2; // NUMBER OF EQUATIONS
	static Scanner sc = new Scanner(System.in);
	
	public Forecast_Component() {
		new GetData();
		m = new double[numEq][numEq];
		constants = new double[numEq];
		solution = new double[numEq];
	}
	
	public void swapRows(int row1, int row2) {
		double temp;
		for (int j = 0; j < numEq; j++) { // SWAPPING CO-EFFICIENT ROWS
			temp = m[row1][j];
			m[row1][j] = m[row2][j];
			m[row2][j] = temp;
		}
		temp = constants[row1]; // SWAPPING CONSTANTS VECTOR
		constants[row1] = constants[row2];
		constants[row2] = temp;
	}

	// phan xu ly Gauss
	public void eliminate() {
		int i, j, k, l;
		for (i = 0; i < numEq; i++) { // i -> ROW ; MATRIX ORDER DECREASES
										// DURING ELIMINATION
			// FIND LARGEST CO-EFFICIENTSOF THE CURRENT COLUMN MOVING ROW-WISE
			double largest = Math.abs(m[i][i]);
			int index = i;
			for (j = i + 1; j < numEq; j++) {
				if (Math.abs(m[j][i]) > largest) {
					largest = m[j][i];
					index = j;
				}
			}
			swapRows(i, index); // SWAPPING i-th ROW to index-th ROW
			for (k = i + 1; k < numEq; k++) {
				double factor = m[k][i] / m[i][i];
				// PROCESSING COLUMN WISE
				for (l = i; l < numEq; l++) {
					m[k][l] -= factor * m[i][l];
				}
				constants[k] -= factor * constants[i]; // PROCESSING CONSTANTS
			}
		}
	}

	// Ngiem tim duoc
	public void solve() {
		for (int i = numEq - 1; i >= 0; i--) {
			solution[i] = constants[i]; // COPY
			for (int j = numEq - 1; j > i; j--) {
				solution[i] -= m[i][j] * solution[j];
			}
			solution[i] /= m[i][i];
		}
	}
	
	private void add(ArrayList<Double> a) {
		m[0][0]=0;
		m[0][0]=a.size();
		m[0][1]=0;
		for(int i=1;i<=a.size();i++){
			m[0][1]+=i;
		}
		constants[0]=0;
		for (int i = 0; i < a.size(); i++) {
			constants[0]+=a.get(i);
		}
		
		m[1][0]=0;
		for(int i=1;i<=a.size();i++){
			m[1][0]+=i;
		}
		m[1][1]=0;
		for(int i=1;i<=a.size();i++){
			m[1][1]+=i*i;
		}
		constants[1]=0;
		for (int i = 0; i < a.size(); i++) {
			constants[1]+=a.get(i)*(i+1);
		}
		
		eliminate();
		solve();
	}
	public double forecast_Element(ArrayList<Double> a){
		double tam = 0;
		add(a);
		tam = solution[0]+solution[1]*(a.size()+1);
		return tam;
	}
	
}
