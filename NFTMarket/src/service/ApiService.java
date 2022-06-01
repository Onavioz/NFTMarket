package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import model.Collection;
import model.CollectionFactory;
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
		response.close();
		JSONArray json_arr = (JSONArray) parsed;

		// inserting each symbol from the JSON array into the string list.
		for (int i = 0; i < json_arr.size(); i++) {
			json_elm = (JSONObject) json_arr.get(i);
			symbol_list.add((String) json_elm.get("symbol"));
		}
		
		return symbol_list;
	}

	public  String GetfloorPriceOpenSea(String name,OkHttpClient client) throws IOException {
		// using okhttp to collect stats of collection

		try {
			Thread.sleep(200); //opensea api get request rate limit = 4sec.
			
			Request request = new Request.Builder().url("https://api.opensea.io/api/v1/collection/" + name + "/stats")
					.get().addHeader("Accept", "application/json").build();
			// executing the api command
			 Response response = client.newCall(request).execute();

			// parsing the string into json
			Object parsed = JSONValue.parse(response.body().string());
			response.close();
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

	public String GetfloorPriceMagicEden(String name,OkHttpClient client) throws IOException {
		// using okhttp to collect stats of collection
		
		try {
			
			Request request = new Request.Builder()
					.url("https://api-mainnet.magiceden.dev/v2/collections/" + name + "/stats").method("GET", null)
					.build();
			// executing the api command
			Response response = client.newCall(request).execute();

			// parsing the string into json
			Object parsed = JSONValue.parse(response.body().string());
			response.close();
			JSONObject parsed_js = (JSONObject) parsed;
			
			// checking for data validation or availability
			if (!parsed_js.containsKey("floorPrice") || parsed_js == null) {
				return "N/A";
			}

			// gathering floor_price
			Long floor_price = (Long) parsed_js.get("floorPrice");

			if (String.valueOf(floor_price) == "null") {
				return "N/A";
			}
			
			return String.valueOf(floor_price);
		} catch (Exception e) {
			return "N/A";
		}
	}

	public void makeCollections(ConvertService converter ,DiffService diff_service) {
	
		Runnable api_main_runnable = new ApiThread(converter,diff_service);
		Thread api_main_thread = new Thread(api_main_runnable);
		api_main_thread.start();
	}

	
	public class ApiThread implements Runnable {
		EdenMarketModel edenMarketData = EdenMarketModel.getInstance();
		OpenSeaMarketModel openSeaMarketData = OpenSeaMarketModel.getInstance();
		ConvertService converter;
		DiffService diff_service;
		boolean allEnded = false;

	

		ApiThread(ConvertService converter,DiffService diff_service) {
			this.converter = converter;
			this.diff_service=diff_service;
		}

		@Override
		public void run() {

			ArrayList<String> sub_collections;
			ArrayList<Runnable> thread_data = new ArrayList<Runnable>();
			ArrayList<Thread> threads_list = new ArrayList<Thread>();
			ArrayList<Boolean> flags;
			int num_of_threads =2;
			int size_of_subCollection=50;
			ArrayList<String> collections;
			

			try {
				collections = GetCollectionsSymbols();
				edenMarketData.InitModel(collections);
				openSeaMarketData.InitModel(collections);
				
				int count = 0;
				for (int i = 0; i < collections.size(); i += size_of_subCollection) {
					sub_collections = new ArrayList<String>();
					for (int j = 0; j < size_of_subCollection; j++)
						sub_collections.add(collections.get(i + j));
					thread_data.add(new DataThread(sub_collections, converter));
					threads_list.add(new Thread(thread_data.get(count)));
					count++;
					
				}
					for (int i = 0; i < num_of_threads; i++)
						threads_list.get(i).start();
					
					while (true) {
					while (!allEnded) {
						flags = new ArrayList<Boolean>();
						for (int i = 0; i < num_of_threads; i++) {
							DataThread data_th = (DataThread) thread_data.get(i);
							flags.add(data_th.GetFlagStatus());
						}

						if (!flags.contains(false)) {
							allEnded = true;
						
						diff_service.makeDiffCollection();
						//announce for all of the working threads that they have finished iteration
						for (int i = 0; i < num_of_threads; i++) {
							DataThread data_th1 = (DataThread) thread_data.get(i);
							data_th1.ChangeHasFinishedFlag(true);
						}

						}
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
}
