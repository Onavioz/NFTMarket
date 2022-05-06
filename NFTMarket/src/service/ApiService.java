package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import model.EdenMarketModel;
import model.OpenSeaMarketModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
	
	public static ApiService instance = new ApiService();
	
	public static ApiService getInstance() {
		return instance;
	}
	public ArrayList<String> GetCollectionsSymbols() throws IOException {

		ArrayList<String> symbol_list = new ArrayList<String>();

		// using okhttp to collect 100 collections
		JSONObject json_elm = new JSONObject();

		OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
		Request request = new Request.Builder()
				.url("https://api-mainnet.magiceden.dev/v2/collections?offset=400&limit=100").method("GET", null)
				.build();

		Response response = client.newCall(request).execute();
		Object parsed = JSONValue.parse(response.body().string());
		JSONArray json_arr = (JSONArray) parsed;

		// inserting each symbol from the JSON array into the string list.
		for (int i = 0; i < json_arr.size(); i++) {
			json_elm = (JSONObject) json_arr.get(i);
			symbol_list.add((String) json_elm.get("symbol"));
		}

		return symbol_list;
	}

	public String GetfloorPriceOpenSea(String name) throws IOException {
		// using okhttp to collect stats of collection
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS)
					.writeTimeout(5, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();

			Request request = new Request.Builder().url("https://api.opensea.io/api/v1/collection/" + name + "/stats")
					.get().addHeader("Accept", "application/json").build();
			// executing the api command
			Response response = client.newCall(request).execute();

			// parsing the string into json
			Object parsed = JSONValue.parse(response.body().string());
			JSONObject parsed_js = (JSONObject) parsed;

			// checking for data validation or availability
			if (!parsed_js.containsKey("stats") || parsed_js == null) {
				return "N/A";
			}

			// accessing to "stats" attribute
			parsed_js = (JSONObject) parsed_js.get("stats");

			// gathering floor_price
			Double floor_price = (Double) parsed_js.get("floor_price");

			if (String.valueOf(floor_price) == "null") {
				return "N/A";
			}
			return String.valueOf(floor_price);
		} catch (Exception e) {
			return "N/A";
		}
	}

	public String GetfloorPriceMagicEden(String name) throws IOException {
		// using okhttp to collect stats of collection
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();

			Request request = new Request.Builder()
					.url("https://api-mainnet.magiceden.dev/v2/collections/" + name + "/stats").method("GET", null)
					.build();
			// executing the api command
			Response response = client.newCall(request).execute();

			// parsing the string into json
			Object parsed = JSONValue.parse(response.body().string());
			JSONObject parsed_js = (JSONObject) parsed;

			// checking for data validation or availability
			if (!parsed_js.containsKey("floorPrice") || parsed_js == null)
				return "N/A";

			// gathering floor_price
			Long floor_price = (Long) parsed_js.get("floorPrice");

			if (String.valueOf(floor_price) == "null")
				return "N/A";
			return String.valueOf(floor_price);
		} catch (Exception e) {
			return "N/A";
		}
	}

	public void makeCollections(ConvertService converter) {
		EdenMarketModel edenMarketData = EdenMarketModel.getInstance();
		OpenSeaMarketModel openSeaMarketData = OpenSeaMarketModel.getInstance();
		ArrayList<String> sub_collections;
		HashMap<String, String> magiceden_data = new HashMap<String, String>();
		HashMap<String, String> openSea_data = new HashMap<String, String>();
		ArrayList<Runnable> thread_data = new ArrayList<Runnable>();
		ArrayList<Thread> threads_list = new ArrayList<Thread>();
		ArrayList<Boolean> flags;
		boolean allEnded = false;
		try {
			ArrayList<String> collections = GetCollectionsSymbols();

			for (int i = 0; i < collections.size(); i += 2) {
				sub_collections = new ArrayList<String>();
				for (int j = 0; j < 2; j++)
					sub_collections.add(collections.get(i + j));
				thread_data.add(new DataThread(sub_collections, converter));
			}

			for (int i = 0; i < 50; i++) {
				threads_list.add(new Thread(thread_data.get(i)));
				threads_list.get(i).start();
			}
			while (!allEnded) {
				flags = new ArrayList<Boolean>();
				for (int i = 0; i < 50; i++) {
					DataThread data_th = (DataThread) thread_data.get(i);
					flags.add(data_th.GetFlagStatus());
				}

				if (!flags.contains(false))
					allEnded = true;
			}

			for (int i = 0; i < 50; i++) {
				DataThread data_th = (DataThread) thread_data.get(i);
				magiceden_data.putAll(data_th.GetMagicEdenData());
				openSea_data.putAll(data_th.GetOpenSeaData());
			}
			edenMarketData.setMagiceden_data(magiceden_data);
			openSeaMarketData.setOpenSea_data(openSea_data);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
