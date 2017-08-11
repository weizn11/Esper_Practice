/**
 * Created by weizinan on 2017/5/10.
 */
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import java.util.Random;

public class HelloEsper {
    private static Random generator=new Random();

    private static void GenerateRandomTick(EPRuntime cepRT) {
        double price = (double) generator.nextInt(10);
        long timeStamp = System.currentTimeMillis();
        String symbol = "AAPL";
        Tick tick = new Tick(symbol, price, timeStamp);
        System.out.println("Sending tick:" + tick);
        cepRT.sendEvent(tick);           //向CEP发送事件
    }

    public static void main(String[] args) {
        /* 设置配置信息 */
        Configuration cepConfig = new Configuration();
        cepConfig.addEventType("StockTick", Tick.class); //添加事件类型定义

        /* 创建引擎实例 */
        EPServiceProvider cepProvider = EPServiceProviderManager.getProvider("myCEPEngine",cepConfig);

        /* 创建statement的管理接口实例 */
        EPAdministrator cepAdmin = cepProvider.getEPAdministrator();
        EPStatement cepStatement = cepAdmin.createEPL("select avg(price) from " +
                "StockTick.win:length_batch(3)");      //定义事件触发策略

        /*绑定事件处理对象*/
        cepStatement.addListener(new EventHandler_1());

        /* 引擎实例运行接口，负责为引擎实例接收数据并发送给引擎处理 */
        EPRuntime cepRT = cepProvider.getEPRuntime();   //启动CEP引擎


        for(int i = 0; i < 9; i++) {
            GenerateRandomTick(cepRT);    //生成随机事件
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }

}