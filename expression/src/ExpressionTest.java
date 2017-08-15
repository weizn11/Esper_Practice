/**
 * Created by weizinan on 2017/8/11.
 */

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import java.util.Random;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import java.lang.reflect.*;

class ESB
{

    private int id;
    private int max;
    private int min;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getMax() { return max; }
    public void setMax(int max) { this.max = max; }
    public int getMin() { return min; }
    public void setMin(int min) { this.min = min; }
}

class ESB_2
{

    private int id;
    private int max;
    private int min;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getMax() { return max; }
    public void setMax(int max) { this.max = max; }
    public int getMin() { return min; }
    public void setMin(int min) { this.min = min; }
}

class Listener implements UpdateListener
{

    public void update(EventBean[] newEvents, EventBean[] oldEvents)
    {
        if (newEvents != null)
        {
            EventBean event = newEvents[0];
            System.out.println("ID: " + event.get("id") + " lambda return: " + event.get("res"));
        }
    }
}

public class ExpressionTest
{
    public static void main(String[] args)
    {
        /* 设置配置信息 */
        Configuration cepConfig = new Configuration();
        cepConfig.addEventType("ESB", ESB.class); //添加事件类型定义
        cepConfig.addEventType("ESB_2", ESB_2.class);

        /* 创建引擎实例 */
        EPServiceProvider cepProvider = EPServiceProviderManager.getProvider("myCEPEngine",cepConfig);

        /* 创建statement的管理接口实例 */
        EPAdministrator cepAdmin = cepProvider.getEPAdministrator();

        //创建全局lambda
        cepAdmin.createEPL("create expression avgPrice { x => (x.max + x.min) / 2 } ");
        EPStatement cepStatement1 = cepAdmin.createEPL("expression middle { x => avgPrice(x) } " +
                "select middle(esb2) as res,id from ESB_2 as esb2");

        EPStatement cepStatement = cepAdmin.createEPL("expression middle { x => (x.max + x.min) / 2 } " +
                "select middle(esb) as res,id from ESB as esb");      //定义事件触发策略

        /*绑定事件处理对象*/
        cepStatement.addListener(new Listener());
        cepStatement1.addListener(new Listener());

        /* 引擎实例运行接口，负责为引擎实例接收数据并发送给引擎处理 */
        EPRuntime cepRT = cepProvider.getEPRuntime();   //启动CEP引擎

        ESB e1 = new ESB();
        e1.setId(1);
        e1.setMax(20);
        e1.setMin(10);
        System.out.println("sendEvent: min=10, max=20");
        cepRT.sendEvent(e1);

        ESB_2 e2 = new ESB_2();
        e2.setId(2);
        e2.setMax(100);
        e2.setMin(4);
        System.out.println("sendEvent: min=4, max=100");
        cepRT.sendEvent(e2);
    }
}