
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Forecast {
	public double[][] m; // MATRIX OF CO-EFFICIENTS
	public double[] constants; // VECTOR OF CONSTANT TERMS
//	public double[] solution; // SOLUTION SET
	public int numEq = 3; // NUMBER OF EQUATIONS
	static Scanner sc = new Scanner(System.in);
	String nameStock;
	public List<Integer> rsForecastPrice;
	public ArrayList<Double> highest;
	public ArrayList<Double> lowest;
	public ArrayList<Double> price;


	public Forecast(String nameStock) throws Exception {

		this.nameStock = nameStock;
		m = new double[numEq][numEq];
		constants = new double[numEq];
		rsForecastPrice = new ArrayList<Integer>();
		highest = new ArrayList<Double>();
		lowest = new ArrayList<Double>();
		price = new ArrayList<Double>();
	}

	private double[] add(ArrayList<Double> a, ArrayList<Double> b, ArrayList<Double> c) {
		/*
		 * a la list chua cac gia tri highest
		 * b la listt chua cac gia tri lowest
		 * c la list chua cac gia tri price
		 * 
		 */
		double []solution = new double[numEq];
	
		m[0][0] = 0;
		m[0][0] = a.size();
		m[0][1] = 0;
		for (int i = 0; i < a.size(); i++) {
			m[0][1] += a.get(i);
		}
		m[0][2] = 0;
		for (int i = 0; i < b.size(); i++) {
			m[0][2] += b.get(i);
		}
		constants[0] = 0;
		for (int i = 0; i < c.size(); i++) {
			constants[0] += c.get(i);
		}
		m[1][0] = 0;
		for (int i = 0; i < a.size(); i++) {
			m[1][0] += a.get(i);
		}
		m[1][1] = 0;
		for (int i = 0; i < a.size(); i++) {
			m[1][1] += a.get(i) * a.get(i);
		}
		m[1][2] = 0;
		for (int i = 0; i < b.size(); i++) {
			m[1][2] += b.get(i) * a.get(i);
		}
		constants[1] = 0;
		for (int i = 0; i < c.size(); i++) {
			constants[1] += c.get(i) * a.get(i);
		}
		m[2][0] = 0;
		for (int i = 0; i < b.size(); i++) {
			m[2][0] += b.get(i);
		}
		m[2][1] = 0;
		for (int i = 0; i < a.size(); i++) {
			m[2][1] += a.get(i) * b.get(i);
		}
		m[2][2] = 0;
		for (int i = 0; i < a.size(); i++) {
			m[2][2] += b.get(i) * b.get(i);
		}
		constants[2] = 0;
		for (int i = 0; i < c.size(); i++) {
			constants[2] += c.get(i) * b.get(i);
		}

		eliminate();
		solution = solve();		
		return solution;
	}

	public double forecastOneDay(double a,double b,double[] solution) {	
		double gtdd = 0;
		gtdd = solution[0]+solution[1]*a  + solution[2]*b;
		return gtdd;
	}
	
	public void runForecast() throws Exception{	
		new GetData(nameStock);
		Forecast_Component component = new Forecast_Component();
		double[] solution = new double[numEq];
		double a = 0, b = 0;
		int tam = 0;
		int n= GetData.getN();
		if(GetData.price[n-1] == 0.0) n--;
		for(int j=0;j<n-9;j++){
		for(int i=j;i<10+j;i++){
			highest.add(GetData.highest[i]);
			lowest.add(GetData.lowest[i]);
			price.add(GetData.price[i]);
		}
		solution = add(highest, lowest, price);
		a = component.forecast_Element(highest);
		b = component.forecast_Element(lowest);
		
		tam = (int)Math.ceil(forecastOneDay(a,b,solution));
		tam = xuLyGia(tam);
		rsForecastPrice.add(tam);
		
		highest.removeAll(highest);
		lowest.removeAll(lowest);
		price.removeAll(price);
		}

		System.out.println("gia doan duoc");
		
		
		//Du doan cho ngay tiep theo
		for(int i=8;i<GetData.n;i++){
			highest.add(GetData.highest[i]);
			lowest.add(GetData.lowest[i]);
			price.add(GetData.price[i]);
		}
		solution = add(highest, lowest, price);
		a = component.forecast_Element(highest);
		b = component.forecast_Element(lowest);
		
		tam = (int)Math.ceil(forecastOneDay(a,b,solution));
		tam = xuLyGia(tam);
		rsForecastPrice.add(tam);
		
		for(int i=0;i<GetData.getPrintDate().size();i++){
		System.out.println(GetData.getPrintDate().get(i)+" "+rsForecastPrice.get(i));
		}
	}
	
	// phan xu ly Gauss
	//---------------------------------------------------------------------------
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
	public double[] solve() {
		double solution[] = new double[numEq];
		for (int i = numEq - 1; i >= 0; i--) {
			solution[i] = constants[i]; // COPY
			for (int j = numEq - 1; j > i; j--) {
				solution[i] -= m[i][j] * solution[j];
			}
			solution[i] /= m[i][i];
		}
		return solution;
	}
	//---------------------------------------------------------------------------
	
	 public List<Integer> getRsForecastPrice() {
		return rsForecastPrice;
	}
	 
	private int xuLyGia(int x){
		int x1 =0;
		int a=0;
		int b=0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (x!=0) {
			list.add(x%10);
			x=x/10;
		}
		b = 10*list.get(1)+list.get(0);
		if(b>=50){
			a=list.get(2)+1;
			list.remove(2);
			list.add(2,a);
			for(int i=0;i<2;i++){
				list.remove(i);
				list.add(i,0);
			}
		}else{
			for(int i=0;i<2;i++){
				list.remove(i);
				list.add(i,0);
			}
		}
		
		Collections.reverse(list);
		
		for (int i = 0; i <list.size() ; i++) {
			x1 = x1*10+list.get(i);
		}
		
		return x1;
	}
		 
	/*public static void main(String[] args) throws Exception {
		Forecast forecast = new Forecast("FPT");
		forecast.runForecast();
		
		DrawGraph dGraph = new DrawGraph(forecast.getRsForecastPrice(),GetData.printPrice,GetData.getPrintDate());
		
		JFrame jFrame= new JFrame();
		jFrame.add(dGraph);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
	}*/
}
