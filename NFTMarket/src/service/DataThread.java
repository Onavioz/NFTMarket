package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import model.EdenMarketModel;
import model.OpenSeaMarketModel;
import okhttp3.OkHttpClient;

public class DataThread implements Runnable {

	ArrayList<String> symbols;
	EdenMarketModel edenMarket;
	OpenSeaMarketModel openSea;
	OkHttpClient os_client ;
	OkHttpClient me_client ;
	
	HashMap<String, String> magiceden_data_sub = new HashMap<String, String>();
	HashMap<String, String> openSea_data_sub = new HashMap<String, String>();
	HashMap<String, String> magiceden_data = new HashMap<String, String>();
	HashMap<String, String> openSea_data = new HashMap<String, String>();
	ApiService api = ApiService.getInstance();
	ConvertService converter;
	String osSymbol;
	boolean flag;
	boolean finish_iterations;

	DataThread(ArrayList<String> symbols, ConvertService converter) {
		this.symbols = symbols;
		this.converter = converter;
		edenMarket = EdenMarketModel.getInstance();
		openSea = OpenSeaMarketModel.getInstance();
		os_client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS)
				.writeTimeout(5, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();
		me_client=new OkHttpClient().newBuilder().build();
	
	}

	@Override
	public void run() {
		String ME_floor_price;
		String OS_floor_price;
		while (true) {

			flag = false;
			finish_iterations = false;

			try {
				for (String symbol : symbols) {
				ME_floor_price= converter.convertMagicEdenFormat(api.GetfloorPriceMagicEden(symbol,me_client));
				magiceden_data = edenMarket.getCollection();
				magiceden_data.put(symbol,ME_floor_price);
				edenMarket.setMagiceden_data(magiceden_data);
				}

				for (String symbol : symbols) {
					if (symbol.contains("_"))
						osSymbol = symbol.replaceAll("_", "-");
					else
						osSymbol = symbol;
					OS_floor_price=converter.convertOpenSeaFormat(api.GetfloorPriceOpenSea(osSymbol,os_client));
					openSea_data = openSea.getCollection();
					openSea_data.put(osSymbol,OS_floor_price);
					openSea.setOpenSea_data(openSea_data);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flag = true;
			while (!finish_iterations);
		}

	}

	public boolean GetFlagStatus() {
		return flag;
	}

	public void ChangeHasFinishedFlag(boolean finishedIterations) {
		finish_iterations = finishedIterations;
	}

	public HashMap<String, String> GetMagicEdenData() {
		return magiceden_data;

	}

	public HashMap<String, String> GetOpenSeaData() {
		return openSea_data;

	}

	public ArrayList<String> GetSymbolsList() {
		return symbols;
	}
}
