
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetNameStock {

    private ConnectDB connectDB;
    public static String nameStock;
    public String dayCurrent;
    public String monthCurrent;
    public List<String> listNameStocks;
    private String url = "http://vnexpress.net/User/ck/Source/GetHCMSDataSmall.asp";

    public GetNameStock() throws Exception {
        listNameStocks = new ArrayList<String>();
        init();
        insertDatabase(listNameStocks);
        
        for(int i=0;i<listNameStocks.size();i++){
            new GetData(listNameStocks.get(i));
        }
                
    }

    private void insertDatabase(List<String> a) {
        try {

            connectDB = new ConnectDB();
            Connection conn = connectDB.connection();

            for (int i = 0; i < a.size(); i++) {
                PreparedStatement pst = conn.prepareStatement("insert into NAMESTOCK values(?)");
                pst.setString(1, a.get(i));
                int y = pst.executeUpdate();
                if (y > 0) {
                    System.out.println("Promotion Updated");
                }
                pst.close();
            }



            conn.commit();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    }
                }
            }
        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        new GetNameStock();
    }
}
