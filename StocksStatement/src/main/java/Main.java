import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		//readJSON();
		writeHTML();
	}
	
	public static void writeHTML() {
		FileWriter fw;
		
		try
		{
			fw = new FileWriter("data\\1.html");
			fw.write("name");
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
			
			for(int i = 0; i < jsonA.size(); i++)
			{
				JSONObject customerRecord = (JSONObject) jsonA.get(i);
				long accountNumber = (long) customerRecord.get("account_number");
				System.out.println(accountNumber);
				
				JSONArray trades = (JSONArray) customerRecord.get("stock_trades");
				for(int x = 0; x < trades.size(); x++) 
				{
					JSONObject trade = (JSONObject) trades.get(x);
					String trade_type = (String) trade.get("type");
					System.out.println(trade_type);
				}
				
				System.out.println();
			}
			
			System.out.println("Succefully read from JSON...");
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
