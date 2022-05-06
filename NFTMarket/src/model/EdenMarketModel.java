package model;

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

	@Override
	public HashMap<String, String> getCollection() {
		return magiceden_data;
	}


}
