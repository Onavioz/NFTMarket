package model;

import java.util.ArrayList;
import java.util.HashMap;

public class EdenMarketModel implements Collection {
	public static EdenMarketModel instance = new EdenMarketModel();
	private HashMap<String, String> magiceden_data = new HashMap<String, String>();
	
	
	public static EdenMarketModel getInstance() {
		return instance;
	}

	public void setMagiceden_data(HashMap<String, String> magiceden_data) {
		this.magiceden_data = magiceden_data;
	}
	
	public HashMap<String, String> getMagiceden_data() {
		return magiceden_data;
	}

	

	@Override
	public HashMap<String, String> getCollection() {
		return magiceden_data;
	}

	public void InitModel(ArrayList<String> collections) {
		for(int i=0 ; i<collections.size();i++)
			magiceden_data.put(collections.get(i), "Loading");
	}

}
