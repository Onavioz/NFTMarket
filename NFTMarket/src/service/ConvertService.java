package service;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConvertService {
	double eth_to_sol_currency;

//getting the currency of 1ETH->1SOL.
	public void GatherCurrency() throws IOException {

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=SOL")
				.method("GET", null).build();

		Response response = client.newCall(request).execute();
		Object parsed = JSONValue.parse(response.body().string());
		JSONObject json_arr = (JSONObject) parsed;
		eth_to_sol_currency = (Double) json_arr.get("SOL");

	}

//getting the floor price (String) converting from ETH->SOL as double than,  than to string for UI.
	public String convertOpenSeaFormat(String floor_price) {
		if(floor_price.equals("N/A"))
			return floor_price;
		
		double f_price = Double.parseDouble(floor_price);
		f_price = f_price * eth_to_sol_currency;
		return String.format("%.2f", f_price);

	}
	public String convertMagicEdenFormat(String floor_price) {
		if(floor_price.equals("N/A"))
			return floor_price;
		
		double f_price = Double.parseDouble(floor_price);
		f_price = f_price /1000000000;
		return String.format("%.2f", f_price);

	}
	
}
