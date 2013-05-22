
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
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

public class ShowPrice extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel componentPane;
    private JTable tbl;
    private JComboBox comboBox;
    private boolean isNull = false;

    public ShowPrice(final String nameStock) throws Exception {

//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        setBounds(200, 200, 400, 400);
        contentPane = new JPanel();
        componentPane = new JPanel();
        comboBox = new JComboBox();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        componentPane.setLayout(new FlowLayout());
        setContentPane(contentPane);
        tbl = new JTable();
        contentPane.add(tbl, BorderLayout.CENTER);
        comboBox.addItem("Du doan theo tuyen tinh");
        comboBox.addItem("Du doan theo phi tuyen tinh");
        JButton btnDuDoan = new JButton("Du Doan");
        componentPane.add(btnDuDoan);
        contentPane.add(componentPane, BorderLayout.SOUTH);
        contentPane.add(comboBox, BorderLayout.NORTH);
        // -------------------------------------------------------------------
        btnDuDoan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem().equals("Du doan theo tuyen tinh")) {
                    try {
                        Forecast forecast = new Forecast(nameStock);
                        forecast.runForecast();
                        DrawGraph dGraph = new DrawGraph(forecast.getRsForecastPrice(),
                                GetData.printPrice, GetData.getPrintDate());
                        JFrame jFrame = new JFrame("Du doan theo tuyen tinh");
                        jFrame.add(dGraph);
                        jFrame.pack();
                        jFrame.setVisible(true);
                       
                        
                        List<Integer> arbitrage = new ArrayList<Integer>();
                        for (int i = 0; i < GetData.printPrice.size(); i++) {
                            int x = forecast.getRsForecastPrice().get(i) - GetData.printPrice.get(i);
                            arbitrage.add(x);
                        }
                        CompareToPriceAndForecast ctpaf = new CompareToPriceAndForecast(GetData.getPrintDate(),
                                GetData.printPrice, forecast.getRsForecastPrice(), arbitrage);

                        
                        
                    } catch (Exception ex) {
                        Logger.getLogger(ShowPrice.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (comboBox.getSelectedItem().equals("Du doan theo phi tuyen tinh")) {
                    try {
                        ForecastNonLinear nonLinear = new ForecastNonLinear(nameStock);
                        nonLinear.runForecast();
                        DrawGraph dGraph = new DrawGraph(nonLinear.getRsForecastPrice(),
                                GetData.printPrice, GetData.getPrintDate());
                        JFrame jFrame = new JFrame("Du doan theo phi tuyen tinh");
                        jFrame.add(dGraph);
                        jFrame.pack();
                        jFrame.setVisible(true);
                        
                        
                        List<Integer> arbitrage = new ArrayList<Integer>();
                        for (int i = 0; i < GetData.printPrice.size(); i++) {
                            int x = nonLinear.getRsForecastPrice().get(i) - GetData.printPrice.get(i);
                            arbitrage.add(x);
                        }
                        CompareToPriceAndForecast ctpaf = new CompareToPriceAndForecast(GetData.getPrintDate(),
                                GetData.printPrice, nonLinear.getRsForecastPrice(), arbitrage);
                        
                        
                        
                    } catch (Exception ex) {
                        Logger.getLogger(ShowPrice.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        // -------------------------------------------------------------------
        refresh();
    }

    public void refresh() {
        try {
            String[] header = {"Trading Date", "Price"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            dtm.addRow(header);
            FileInputStream fi;
            fi = new FileInputStream("D:/out.txt");
            Scanner sc = new Scanner(fi);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                StringTokenizer stz = new StringTokenizer(line, " ");// trim
                String[] row = {stz.nextToken(), stz.nextToken()};
                dtm.addRow(row);
            }
            fi.close();
            tbl.setModel(dtm);
            dtm.fireTableDataChanged();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isNull() {
        return isNull;
    }
}
