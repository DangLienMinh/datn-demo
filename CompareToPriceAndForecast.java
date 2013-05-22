
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class CompareToPriceAndForecast extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel componentPane;
    private JTable tbl;
    private boolean isNull = false;
    List<String> tradingDate;
    List<Integer> price;
    List<Integer> priceForecast;
    List<Integer> arbitrage;
    private ConnectDB connectDB;
    private List<String> writeFile;

    public CompareToPriceAndForecast(List<String> tradingDate, List<Integer> price,
            List<Integer> priceForecast, List<Integer> arbitrage) throws SQLException {
        this.tradingDate = tradingDate;
        this.price = price;
        this.priceForecast = priceForecast;
        this.arbitrage = arbitrage;

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
                super.windowClosed(e);
                isNull = true;
                //System.out.println("close");

            }

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                super.windowClosing(e);
                dispose();
            }
        });

//        writeToDataBase(tradingDate, price, priceForecast, arbitrage);
        writeFileToDisk(tradingDate, price, priceForecast, arbitrage);
    }

    public void writeFileToDisk(List<String> tradingDate, List<Integer> price,
            List<Integer> priceForecast, List<Integer> arbitrage) {
        writeFile = new ArrayList<String>();
        String tam;
        for (int i = 0; i < price.size(); i++) {
            tam = tradingDate.get(i).concat(" " + Integer.toString(price.get(i)) + " "
                    + Integer.toString(priceForecast.get(i)) + " " + Integer.toString(arbitrage.get(i)));
            writeFile.add(tam);
        }

        String str = tradingDate.get(tradingDate.size() - 1).concat(" " + "-" + " "
                + Integer.toString(priceForecast.get(tradingDate.size() - 1)) + " " + "-");
        writeFile.add(str);

        try {
            FileOutputStream file = new FileOutputStream("D:/arbitrage.txt");
            PrintWriter out = new PrintWriter(file);
            for (int i = 0; i < tradingDate.size(); i++) {
                out.write(writeFile.get(i) + "\r\n");
                out.flush();
            }
            out.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void writeToDataBase(List<String> tradingDate, List<Integer> price,
            List<Integer> priceForecast, List<Integer> arbitrage) throws SQLException {

        connectDB = new ConnectDB();
        Connection conn = connectDB.connection();
        for (int i = 0; i < price.size(); i++) {
            PreparedStatement pst = conn.prepareStatement("insert into _ARBITRAGE values(?,?,?,?)");
            pst.setString(1, tradingDate.get(i));
            pst.setInt(2, price.get(i));
            pst.setInt(3, priceForecast.get(i));
            pst.setInt(4, arbitrage.get(i));
            int y = pst.executeUpdate();
            if (y > 0) {
                System.out.println("Promotion Updated");
            }
            pst.close();
        }
        PreparedStatement pst1 = conn.prepareStatement("insert into _ARBITRAGE values(?,?,?,?)");
        pst1.setString(1, tradingDate.get(tradingDate.size() - 1));
        pst1.setInt(2, 0);
        pst1.setInt(3, priceForecast.get(tradingDate.size() - 1));
        pst1.setInt(4, 0);
        int y = pst1.executeUpdate();
        if (y > 0) {
            System.out.println("Promotion Updated");
        }
        pst1.close();

    }

    
    
    public void createdArbitrage() {
    }
}
