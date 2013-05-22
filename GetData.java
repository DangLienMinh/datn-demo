
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class GetData {

    public static double price[];// gia tri co phieu
    public static double lowest[];// gia tri thap nhat
    public static double highest[];// gia tri cao nhat
    public static int stt[];
    public static String date[];
    public static String tradingDate[];
    public static List<String> printDate;
    private static String writeFile[];
    static String filePath = "d://out.xml";
    public static int day, month;
    public static int n;

    public static int getN() {
        return n;
    }
    private String dayCurrent;
    private String monthCurrent;
    public static List<Integer> printPrice;

    public GetData() {
    }

    public GetData(String nameStock) throws Exception {
        getCurrentDateTime();
        String thang1 = Integer.toString((Integer.parseInt(monthCurrent) - 1));
        int day1 = Integer.parseInt(dayCurrent) - 1;
        String url = "http://finance.vietstock.vn/Controls/TradingResult/Matching_Hose_Result.aspx?scode="
                + nameStock
                + "&lcol=CN%2CTN%2CGD3%2C&sort=Time&dir=desc&page=1&psize=0&fdate="
                + thang1
                + "%2F"
                + dayCurrent
                + "%2F13&tdate="
                + monthCurrent
                + "%2F" + Integer.toString(day1) + "%2F13&exp=xml";

        URL pageUrl = new URL(url);
        URLConnection getConn = pageUrl.openConnection();
        getConn.connect();
        BufferedReader dis = new BufferedReader(new InputStreamReader(
                getConn.getInputStream()));
        String myString;
        while ((myString = dis.readLine()) != null) {
            try {
                // Create file
                FileWriter fstream = new FileWriter(filePath);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(myString);
                // Close the output stream
                out.close();
            } catch (Exception e) {// Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }

        // Goi ham doc file xml
        ReadXML(filePath);

        processDate();
        price = reverse(price);
        highest = reverse(highest);
        lowest = reverse(lowest);
        date = reverseStringArray(date);
        tradingDate = reverseStringArray(tradingDate);
        
//        insertDatabase(nameStock);

        for (int i = 9; i < n; i++) {
            printPrice.add((int) price[i]);
            printDate.add(tradingDate[i]);
        }

        int x = 0;
        x = Integer.parseInt(dayCurrent) + validateTh(Integer.parseInt(dayCurrent), Integer.parseInt(monthCurrent), 2013);
        String nextDay = Integer.toString(x) + "/" + monthCurrent;
        printDate.add(nextDay);

        writeFileToDisk();

    }

    //Phuong thuc kiem tra ngay thang
    private int validateTh(int ngay, int thang, int nam) {

        int songay, thu;
        int a[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        songay = ((nam - 1) % 7) * 365 + (nam - 1) / 4;
        /*
         * Do so qua lon nen minh lay phan du luon o day khong lam sai thuat
         * toan nhe
         */
        if (nam % 4 == 0) {
            a[1] = 29;
        }
        for (int i = 0; i < (thang - 1); i++) {
            songay += a[i];
        }
        songay += ngay;
        thu = songay % 7;
        if (thu == 1) {
            return 1;
        } else if (thu == 0) {
            return 2;
        }
        return 0;
    }

    // Phuong thuc doc file XML
    public double[] ReadXML(String path) throws Exception {

        // The two lines below are just for getting an
        // instance of DocumentBuilder which we use
        // for parsing XML data
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Here we do the actual parsing
        Document doc = builder.parse(new File(path));

        // Here we get the root element of XML and print out
        Element rootElement = doc.getDocumentElement();

        NodeList listPrice = rootElement.getElementsByTagName("Price");
        NodeList listHighest = rootElement.getElementsByTagName("Highest");
        NodeList listLowest = rootElement.getElementsByTagName("Lowest");

        NodeList listDate = rootElement.getElementsByTagName("TradingDate");

        // out its data
        n = listPrice.getLength();
        price = new double[n];
        printPrice = new ArrayList<Integer>();
        printDate = new ArrayList<String>();
        highest = new double[n];
        lowest = new double[n];
        date = new String[n];
        tradingDate = new String[n];
        stt = new int[n];

        for (int i = 0; i < n; i++) {
            Node priceNode = listPrice.item(i);
            Node highestNode = listHighest.item(i);
            Node lowestNode = listLowest.item(i);
            Node dateNode = listDate.item(i);

            price[i] = Double.parseDouble(priceNode.getTextContent());
            highest[i] = Double.parseDouble(highestNode.getTextContent());
            lowest[i] = Double.parseDouble(lowestNode.getTextContent());
            stt[i] = i + 1;
            String str = dateNode.getTextContent();
            date[i] = str;
        }
        return price;
    }

    // Lay ngay gio he thong
    public void getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // get current date time with Date()
        Date date = new Date();
        String str = dateFormat.format(date);
        StringTokenizer stz = new StringTokenizer(str, "/");// trim
        String[] row = {stz.nextToken(), stz.nextToken()};
        dayCurrent = row[0];
        monthCurrent = row[1];
    }

    public void writeFileToDisk() {
        writeFile = new String[n];
        for (int i = 0; i < n; i++) {
            writeFile[i] = tradingDate[i].concat(" "
                    + Double.toString(price[i]));
        }
        try {
            FileOutputStream file = new FileOutputStream("D:/out.txt");
            PrintWriter out = new PrintWriter(file);
            for (int i = 0; i < n; i++) {
                out.write(writeFile[i] + "\r\n");
                out.flush();
            }
            out.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Phuong thuc cat bo gio trong tradingdate
    public String[] processDate() {
        for (int i = 0; i < n; i++) {
            tradingDate[i] = date[i].substring(0, 5);
        }
        return tradingDate;
    }

    // Ham dao nguoc mang
    public static double[] reverse(double[] array) {
        double[] reverseArray = new double[array.length];
        for (int i = 0; i < reverseArray.length; i++) {
            reverseArray[i] = array[array.length - i - 1];
        }
        return reverseArray;
    }

    // Ham dao nguoc mang String
    public static String[] reverseStringArray(String[] array) {
        String[] reverseArray = new String[array.length];
        for (int i = 0; i < reverseArray.length; i++) {
            reverseArray[i] = array[array.length - i - 1];
        }
        return reverseArray;
    }

    public static String[] getTradingDate() {
        return tradingDate;
    }

    public static List<Integer> getPrintPrice() {
        return printPrice;
    }

    public static List<String> getPrintDate() {
        return printDate;
    }
    
    public void insertDatabase(String table){
        try {

            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver);
            String url = "jdbc:sqlserver://localhost:1433;databasename=DATN";
            String user = "sa";
            String pass = "123456";
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement statement = con.createStatement();
            statement.execute("Delete from "+table+"");
            statement.close();
            
            
            for (int i = 0; i < price.length; i++) {
            PreparedStatement pst = con.prepareStatement("insert into "+ table +" values(?,?,?,?)");
            pst.setString(1, tradingDate[i]);
            pst.setDouble(2, highest[i]);
            pst.setDouble(3, lowest[i]);
            pst.setDouble(4, price[i]);
            int y = pst.executeUpdate();
            if (y > 0) {
                System.out.println("Promotion Updated");
            }
            pst.close();
            }
            con.commit();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
