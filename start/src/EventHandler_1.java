import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by weizinan on 2017/5/10.
 */
public class EventHandler_1 implements UpdateListener {
    @Override
    public void update(EventBean[] newData, EventBean[] oldData) {
        if(newData != null) {
            System.out.println("New event list length: " + newData.length);
            double avg = (double)newData[0].get("price");
            System.out.println("Average price: " + avg);
        }
    }
}
