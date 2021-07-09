import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		readJSON();
	}
	
	public static void writeHTML(JSONObject customerRecord) {
		String strHTMLTop = "<!DOCTYPE html><html><body>\r\n";
        String strHTMLBottom = "</body></html>";
        String strHTMLCustInfo = String.format("<h3>%s</h3><p>%s %s</p>\r\n<p>%s</p><br>",
                LocalDate.now(), customerRecord.get("first_name"), customerRecord.get("last_name"), customerRecord.get("ssn"));
        String strHTMLTableTradeTop = "<table style=\"width:100%\">\r\n<tr><th>Type</th>"
                + "<th>Symbol</th><th>Price</th><th>Shares</th><th>Total</th></tr>";
        String strHTMLTableTradeBottom = "</table>\r\n";
        String strHTMLSummary = String.format("<p>Cash Value: %s</p>\r\n<p>Stock Value:"
                + " %s</p>\r\n", "cash", "stock");
        String strFileName = customerRecord.get("account_number").toString();
        
		FileWriter fw;
		
		try
		{
			fw = new FileWriter("data\\" + strFileName + ".html");
			fw.write(strHTMLTop);
			fw.write(strHTMLCustInfo);
			fw.write(strHTMLTableTradeTop);
			//rows of trades
			fw.write(strHTMLTableTradeBottom);
			fw.write(strHTMLSummary);
			fw.write(strHTMLBottom);
			fw.close();
			
			System.out.println("HTML generated...");
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public static void readJSON() {
		try
		{
			Object json = new JSONParser().parse(new FileReader("data\\stocks.json"));
			JSONArray jsonA = (JSONArray) json;
			
			//for(int i = 0; i < jsonA.size(); i++)
			for(int i = 0; i < 5; i++)
			{
				JSONObject customerRecord = (JSONObject) jsonA.get(i);
				writeHTML(customerRecord);
				
//				long accountNumber = (long) customerRecord.get("account_number");
//				System.out.println(accountNumber);
//				
//				JSONArray trades = (JSONArray) customerRecord.get("stock_trades");
//				for(int x = 0; x < trades.size(); x++) 
//				{
//					JSONObject trade = (JSONObject) trades.get(x);
//					String trade_type = (String) trade.get("type");
//					System.out.println(trade_type);
//				}
//				
//				System.out.println();
			}
			
			System.out.println("Succefully read from JSON...");
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
