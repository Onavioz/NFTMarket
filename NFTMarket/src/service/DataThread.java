package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataThread implements Runnable {

	ArrayList<String> symbols;

	HashMap<String, String> magiceden_data = new HashMap<String, String>();
	HashMap<String, String> openSea_data = new HashMap<String, String>();
	ApiService api = ApiService.getInstance();
	ConvertService converter;
	boolean flag = false;

	DataThread(ArrayList<String> symbols, ConvertService converter) {
		this.symbols = symbols;
		this.converter = converter;
	}

	@Override
	public void run() {
		for (String symbol : symbols) {
			try {
				magiceden_data.put(symbol, converter.convertMagicEdenFormat(api.GetfloorPriceMagicEden(symbol)));
				openSea_data.put(symbol, converter.convertOpenSeaFormat(api.GetfloorPriceOpenSea(symbol)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		flag = true;

	}

	public boolean GetFlagStatus() {
		return flag;
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
