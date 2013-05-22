
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;

//Lop du doan theo phuong trinh phi tuyen tinh y = ax2 + bx + c
public class ForecastNonLinear {

    public double[][] m; // MATRIX OF CO-EFFICIENTS
    public double[] constants; // VECTOR OF CONSTANT TERMS
    public int numEq = 3; // NUMBER OF EQUATIONS
    static Scanner sc = new Scanner(System.in);
    String nameStock;
    public List<Integer> rsForecastPrice;
    public ArrayList<Double> price;

    public ForecastNonLinear(String nameStock) {
        this.nameStock = nameStock;
        m = new double[numEq][numEq];
        constants = new double[numEq];
        rsForecastPrice = new ArrayList<Integer>();
        price = new ArrayList<Double>();
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

    private double[] add(ArrayList<Double> a) {
        m[0][0] = 0;
        m[0][0] = a.size();
        m[0][1] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[0][1] += i;
        }
        m[0][2] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[0][2] += i * i;
        }
        constants[0] = 0;
        for (int i = 0; i < a.size(); i++) {
            constants[0] += a.get(i);
        }
        m[1][0] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[1][0] += i;
        }
        m[1][1] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[1][1] += i * i;
        }
        m[1][2] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[1][2] += i * i * i;
        }
        constants[1] = 0;
        for (int i = 0; i < a.size(); i++) {
            constants[1] += a.get(i) * (i + 1);
        }
        m[2][0] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[2][0] += i * i;
        }
        m[2][1] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[2][1] += i * i * i;
        }
        m[2][2] = 0;
        for (int i = 1; i <= a.size(); i++) {
            m[1][2] += i * i * i * i;
        }
        constants[2] = 0;
        for (int i = 0; i < a.size(); i++) {
            constants[2] += a.get(i) * ((i + 1) * (i + 1));
        }
        eliminate();
        return solve();
    }

    private double forecastNonLinear(ArrayList<Double> a) {
        double[] solution = add(a);
        double tam;
        tam = solution[0] + solution[1] * (a.size() + 1) + solution[2] * ((a.size() + 1) * (a.size() + 1));
        return tam;
    }

    public void runForecast() throws Exception {
        new GetData(nameStock);
        int tam = 0;
        int n = GetData.getN();
        if (GetData.price[n - 1] == 0.0) {
            n--;
        }
        for (int j = 0; j < n - 9; j++) {
            for (int i = j; i < 10 + j; i++) {
                price.add(GetData.price[i]);
            }
            tam = (int) Math.ceil(forecastNonLinear(price));
            tam = xuLyGia(tam);
            rsForecastPrice.add(tam);

            price.removeAll(price);
        }

        //Du doan cho ngay tiep theo
        for (int i = 8; i < GetData.n; i++) {
            price.add(GetData.price[i]);
        }



        tam = (int) Math.ceil(forecastNonLinear(price));
        tam = xuLyGia(tam);
        rsForecastPrice.add(tam);




        System.out.println("gia doan duoc");
        for (int i = 0; i < GetData.getPrintDate().size() ; i++) {
            System.out.println(GetData.getPrintDate().get(i) + " " + rsForecastPrice.get(i));
        }
    }

    private int xuLyGia(int x) {
        int x1 = 0;
        int a = 0;
        int b = 0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (x != 0) {
            list.add(x % 10);
            x = x / 10;
        }
        b = 10 * list.get(1) + list.get(0);
        if (b >= 50) {
            a = list.get(2) + 1;
            list.remove(2);
            list.add(2, a);
            for (int i = 0; i < 2; i++) {
                list.remove(i);
                list.add(i, 0);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                list.remove(i);
                list.add(i, 0);
            }
        }

        Collections.reverse(list);

        for (int i = 0; i < list.size(); i++) {
            x1 = x1 * 10 + list.get(i);
        }

        return x1;
    }

    public List<Integer> getRsForecastPrice() {
        return rsForecastPrice;
    }

    public static void main(String[] args) throws Exception {
        ForecastNonLinear linear = new ForecastNonLinear("FPT");
        linear.runForecast();
        DrawGraph dGraph = new DrawGraph(linear.getRsForecastPrice(), GetData.printPrice, GetData.getPrintDate());

        JFrame jFrame = new JFrame();
        jFrame.add(dGraph);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
