import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.time.LocalDate;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class Main {

	public static void main(String[] args) {
		readJSON();
	}
	
	public static void createPDF(String strFileName) {
		try {
			File inFile = new File("data\\" + strFileName + ".html");
			File outFile = new File("data\\" + strFileName + ".pdf");
			
			OutputStream os = new FileOutputStream(outFile);
			PdfRendererBuilder builder = new PdfRendererBuilder();
			
			builder.useFastMode();
			builder.withFile(inFile);
			builder.toStream(os);
			builder.run();
			
			System.out.println("PDF generated...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeHTML(JSONObject customerRecord) {
		String strHTMLTop = "<!DOCTYPE html><html><body>\r\n";
        String strHTMLBottom = "</body></html>";
        String strHTMLCustInfo = String.format("<h3>%s</h3><p>%s %s</p>\r\n<p>%s</p>\r\n",
                LocalDate.now(), "Name: " + customerRecord.get("first_name"), customerRecord.get("last_name"), "SSN: " + customerRecord.get("ssn"));
        String strHTMLTableTradeTop = "<table style=\"width:100%; border:2px solid black;\">\r\n<tr><th>Type</th>"
                + "<th>Symbol</th><th>Price</th><th>Shares</th><th>Total</th></tr>";
        String strHTMLTableTradeBottom = "</table>\r\n";
        String strHTMLSummary;
        String strFileName = customerRecord.get("account_number").toString();
        
		FileWriter fw;
		
		JSONArray trades = (JSONArray) customerRecord.get("stock_trades");
		double cashValue = Double.parseDouble(customerRecord.get("beginning_balance").toString().substring(1));
		double stockValue = 0.00;
		String trade_type;
		String stock_symbol;
		long count_shares;
		String price_per_share;
		double total_trade_value;
		
		try
		{
			fw = new FileWriter("data\\" + strFileName + ".html");
			fw.write(strHTMLTop);
			fw.write(strHTMLCustInfo);
			fw.write(strHTMLTableTradeTop);
			for(int x = 0; x < trades.size(); x++) 
			{
				JSONObject trade = (JSONObject) trades.get(x);
				trade_type = (String) trade.get("type");
				stock_symbol = (String) trade.get("stock_symbol");
				count_shares = (long) trade.get("count_shares");
				price_per_share = (String) trade.get("price_per_share");
				total_trade_value = count_shares * Double.parseDouble(price_per_share.substring(1));
				
				if(trade_type.equals("Sell")) {
					cashValue += total_trade_value;
					stockValue -= total_trade_value;
				} else if(trade_type.equals("Buy")) {
					cashValue -= total_trade_value;
					stockValue += total_trade_value;
				}
				
				String strHTMLRowData = String.format("<tr style=\"text-align:center;\"><td style=\"border:1px double;\">%s</td>"
						+ "<td style=\"border:1px double;\">%s</td>"
						+ "<td style=\"border:1px double;\">%s</td>"
						+ "<td style=\"border:1px double;\">%s</td>"
						+ "<td style=\"border:1px double;\">$%,.2f</td></tr>",
						trade_type, stock_symbol, price_per_share, count_shares, Math.round(total_trade_value * 100.0) / 100.0);
				fw.write(strHTMLRowData);
			}
			fw.write(strHTMLTableTradeBottom);
			strHTMLSummary = String.format("<p>Cash Value: $%,.2f</p>\r\n<p>Stock Value:"
	                + "$%,.2f</p>\r\n", Math.round(cashValue * 100.0) / 100.0, Math.round(stockValue * 100.0) / 100.0);
			fw.write(strHTMLSummary);
			fw.write(strHTMLBottom);
			fw.close();
			
			System.out.println("HTML generated...");
			
			createPDF(strFileName);
			
		} catch (Exception e) {
			e.printStackTrace();
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
				writeHTML(customerRecord);
				
			}
			
			System.out.println("Succefully read from JSON...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
