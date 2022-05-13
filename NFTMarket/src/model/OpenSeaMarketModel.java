package model;

import java.util.ArrayList;
import java.util.HashMap;

public class OpenSeaMarketModel implements Collection {
	public static OpenSeaMarketModel instance = new OpenSeaMarketModel();
	private HashMap<String, String> openSea_data = new HashMap<String, String>();



	public static OpenSeaMarketModel getInstance() {
		return instance;
	}

	public void setOpenSea_data(HashMap<String, String> openSea_data) {
		this.openSea_data = openSea_data;
	}
	
	public HashMap<String, String> getOpenSea_data() {
		return openSea_data;
	}

	@Override
	public HashMap<String, String> getCollection() {
		return openSea_data;
	}
	
	public void InitModel(ArrayList<String> collections) {
		for(int i=0 ; i<100;i++)
			openSea_data.put(collections.get(i), "Loading");
	}

}
