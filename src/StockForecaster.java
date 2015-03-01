import java.util.*;

public class StockForecaster {

    public static final int REALTIME_STOCK_INTERVAL = 5;
    public static final String[] SYMBOLS = {"GOOG","YHOO","AAPL","FB","MSFT"};
    HistoricalStock historicalStock;
    RealtimeStock realtimeStock;
    
    private Timer realtimeStockTimer;
    
    public StockForecaster() {  
        
        historicalStock = new HistoricalStock(1);
        historicalStock.collectData();
        
        realtimeStockTimer = new Timer();
     
        realtimeStock = new RealtimeStock();
        realtimeStockTimer.schedule(realtimeStock, 0, REALTIME_STOCK_INTERVAL * 1000);
              
    }
    
    public static void main(String[] args) {
        StockForecaster stockForecaster = new StockForecaster();
    }
    
}
