import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ShowPrice price;
	private JComboBox jComboBox = new JComboBox();
	private JLabel jLabel = new JLabel(
			"Please choice name Stock and press Display");
	private JButton button = new JButton("DisPlay");
	private String url = "http://vnexpress.net/User/ck/Source/GetHCMSDataSmall.asp";
	public static String nameStock;
	public String dayCurrent;
	public String monthCurrent;
	public List<String>listNameStocks;
	public Main() {

		JPanel jp1 = new JPanel(new GridBagLayout());
		JPanel jp2 = new JPanel();
		listNameStocks = new ArrayList<String>();
		jp1.add(jLabel);
		jp1.add(jComboBox);
		jp1.add(button);
		this.setLayout(new BorderLayout());
		this.add(jp1, BorderLayout.NORTH);
		this.add(jp2, BorderLayout.CENTER);

		init();
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (jComboBox.getSelectedIndex() > -1) {
					nameStock = jComboBox.getSelectedItem().toString();
					System.out.println(nameStock);
					try {
						getCurrentDateTime();
						new GetData(nameStock);

						if (price == null) {
							price = new ShowPrice(nameStock);
							price.setVisible(true);
						} else {
							if (price.isNull()) {
								price = new ShowPrice(nameStock);
								price.setVisible(true);
							} else {
								new GetData(nameStock);
								price.refresh();
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

	public void getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// get current date time with Date()
		Date date = new Date();
		String str = dateFormat.format(date);
		StringTokenizer stz = new StringTokenizer(str, "/");// trim
		String[] row = { stz.nextToken(), stz.nextToken() };
		dayCurrent = row[0];
		monthCurrent = row[1];
	}

	public void init() {

		int i;
		Object ob;
		try {
			URL u = new URL(url);
			ob = u.getContent();
			if (ob instanceof InputStream) {
				BufferedInputStream t = new BufferedInputStream(
						(InputStream) ob);
				String st = "";
				while ((i = t.read()) > 0) {
					st = st + (char) i;
				}

				Pattern pt1 = Pattern.compile("<tr>.+?</tr>");
				Matcher st1 = pt1.matcher(st);
				while (st1.find()) {

					String st21 = st1.group();

					Pattern pt2 = Pattern.compile("<td.+?</td>");
					Matcher st2 = pt2.matcher(st21);
					if (st2.find()) {

						String tp = st2.group();

						tp = tp.replaceAll("<.+?>", "");
						listNameStocks.add(tp);
						jComboBox.addItem(tp);

                                        }
				}
			}
		} catch (MalformedURLException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String arg[]) throws IOException {
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
		Main display = new Main();
		frame.add(display);
		frame.setBackground(Color.green);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
