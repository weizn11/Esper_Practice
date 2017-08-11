/**
 * Created by weizinan on 2017/5/10.
 */
import java.util.Date;

public class Tick {
    private String symbol;
    private Double price;
    private Date timeStamp;

    Tick(String s, double p, long t) {
        symbol = s;
        price = p;
        timeStamp = new Date(t);
    }
    public double getPrice() {return price;}
    public String getSymbol() {return symbol;}
    public Date getTimeStamp() {return timeStamp;}

    @Override
    public String toString() {
        return "Price: " + price.toString() + " time: " + timeStamp.toString();
    }
}