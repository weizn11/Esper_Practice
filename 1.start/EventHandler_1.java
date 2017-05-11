import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by weizinan on 2017/5/10.
 */
public class EventHandler_1 implements UpdateListener {
    @Override
    public void update(EventBean[] newData, EventBean[] oldData) {
        System.out.println("Recv event:");

        if(newData != null) {
            System.out.println("New event list length: " + newData.length);
            for(EventBean newEvent : newData) {
                Double newPrice = (Double) newEvent.get("price");
                System.out.println("New event received: " + newPrice);
                System.out.println("New event received: " + (String)newEvent.get("symbol"));
            }
        }
        if(oldData != null) {
            System.out.println("Old event list length: " + oldData.length);
            for(EventBean oldEvent : oldData) {
                Double oldPrice = (Double) oldEvent.get("price");
                System.out.println("Old event received: " + oldPrice);
            }
        }
    }
}
