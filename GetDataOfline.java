
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GetDataOfline {

    public List<Double> price;
    public List<Double> lowest;
    public List<Double> highest;
    public List<String> tradingDate;
    public List<String> printDate;
    public int day, month;
    public int n;
    private ConnectDB connectDB;

    public GetDataOfline(String nameStock) {
        tradingDate = new ArrayList<String>();
        price = new ArrayList<Double>();
        highest = new ArrayList<Double>();
        lowest = new ArrayList<Double>();
        setLengArray(nameStock);
    }

    private void setLengArray(String table) {
        try {
            connectDB = new ConnectDB();
            Connection conn = connectDB.connection();
            String sql = "select * from " + table + "";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            n = 0;
            while (rs.next()) {
                tradingDate.add(rs.getString("date"));
                highest.add(rs.getDouble("highest"));
                lowest.add(rs.getDouble("lowest"));
                price.add(rs.getDouble("price"));
            }
            n = tradingDate.size();

        } catch (Exception ez) {
            ez.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GetDataOfline getDataOfline = new GetDataOfline("FPT");
        System.out.println("do dai mang" + getDataOfline.n);
        for (int i = 0; i < getDataOfline.n; i++) {
            System.out.println(getDataOfline.tradingDate.get(i) + " " + getDataOfline.highest.get(i) + " " + getDataOfline.lowest.get(i) + " " + getDataOfline.price.get(i));
        }
    }
}
