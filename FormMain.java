
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public final class FormMain extends JPanel {

    private ShowPrice showPrice;
    private JComboBox jComboBox = new JComboBox();
    JPanel jp1, jp2, jp3;
    private JLabel jLabel = new JLabel("Please choice name Stock and press Display");
    private JButton button = new JButton("DisPlay");
    private String url = "http://vnexpress.net/User/ck/Source/GetHCMSDataSmall.asp";
    public static String nameStock;

    public FormMain() throws Exception {
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp1.add(jLabel);
        jp2.add(jComboBox);
        jp3.add(button);
        this.setLayout(new BorderLayout());
        this.add(jp1, BorderLayout.NORTH);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp3, BorderLayout.SOUTH);
        
        GetNameStock getNameStock = new GetNameStock();
        loadComboBox("NAMESTOCK", jComboBox);
        button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (jComboBox.getSelectedIndex() > -1) {
					nameStock = jComboBox.getSelectedItem().toString();
					System.out.println(nameStock);
					try {
                                            GetData data = new GetData(nameStock);
						if (showPrice == null) {
							showPrice = new ShowPrice(nameStock);
							showPrice.setVisible(true);
						} else {
							if (showPrice.isNull()) {
								showPrice = new ShowPrice(nameStock);
								showPrice.setVisible(true);
							} else {
                                                            GetData data1 = new GetData(nameStock);
								showPrice.refresh();
							}
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
    }

    public void loadComboBox(String table, JComboBox a) {

        try {
            ConnectDB connection = new ConnectDB();
            a.addItem("Please choice");
            Connection conn = connection.connection();
            String sql3 = "select * from " + table + "";
            PreparedStatement pst3 = conn.prepareStatement(sql3);
            ResultSet rs3 = pst3.executeQuery();
            while (rs3.next()) {
                String k = rs3.getString(2);
                a.addItem(k);
            }
        } catch (Exception ez) {
              ez.printStackTrace();
        }

    }
    
    public static void main(String arg[]) throws IOException, Exception {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look
            // and feel.
        }
        JFrame frame = new JFrame("Chuong trinh du doan co phieu");
        FormMain display = new FormMain();
        frame.add(display);
        frame.setBackground(Color.green);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
