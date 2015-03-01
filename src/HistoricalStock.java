import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import java.util.Calendar;
import java.util.List;
import java.sql.*;
import java.text.SimpleDateFormat;

public class HistoricalStock {

    private int numYear;
    private List<HistoricalQuote>[] listHistory;

    public HistoricalStock(int y) {
        numYear = y;
        listHistory = (List<HistoricalQuote>[]) new List[StockForecaster.SYMBOLS.length];

        Calendar from = Calendar.getInstance();
        from.add(Calendar.YEAR, -numYear);

        for (int i = 0; i < StockForecaster.SYMBOLS.length; i++) {
            Stock stock = YahooFinance.get(StockForecaster.SYMBOLS[i], from, Interval.DAILY);
            listHistory[i] = stock.getHistory();
        }

    }

    public void collectData() {
        
        try {
            
            Connection connection;   
            
            connection = DriverManager.getConnection(DatabaseManager.URL + DatabaseManager.DATABASE_NAME, DatabaseManager.USER_NAME, DatabaseManager.PASSWORD);
            
            Statement statement = connection.createStatement();
                for (int i = 0; i < StockForecaster.SYMBOLS.length; i++) {

                    String symbol = StockForecaster.SYMBOLS[i];
                    String date;
                    double open, high, low, close;
                    long volume;

                    List<HistoricalQuote> historicalQuotes = listHistory[i];
                    for (HistoricalQuote dayData : historicalQuotes) {

                        Calendar cd = dayData.getDate();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        date = dateFormat.format(cd.getTime());
                        open = dayData.getOpen();
                        high = dayData.getHigh();
                        low = dayData.getLow();
                        close = dayData.getClose();
                        volume = dayData.getVolume();
                        
                        statement.executeUpdate("INSERT INTO StockHistorical VALUES ('" + symbol + "', '" + date + "', " + open + ", " + close + ", " + low + ", " + high + ", " + volume + ")");
                    }

                }
                
                connection.close();
                
            } catch(Exception e) {
                System.out.println("database operation error.");
            }
        
    }

}

