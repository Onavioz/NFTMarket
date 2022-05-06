package service;

import java.util.HashMap;

import model.DiffCollection;
import model.EdenMarketModel;
import model.OpenSeaMarketModel;

public class DiffService {

	public DiffService() {

	}

	public HashMap<String, String> GetDiffHashMap(HashMap<String, String> magiceden_data,
			HashMap<String, String> openSea_data) {
		HashMap<String, String> diff_result = new HashMap<String, String>();
		;
		double MEfloor_p;
		double OSfloor_p;
		double result;

		for (String key : magiceden_data.keySet()) {
			if (openSea_data.containsKey(key)) {

				if (magiceden_data.get(key).equals("N/A") || openSea_data.get(key).equals("N/A"))
					diff_result.put(key, "-");
				else {
					MEfloor_p = Double.parseDouble(magiceden_data.get(key));
					OSfloor_p = Double.parseDouble(openSea_data.get(key));
					result = ((MEfloor_p - OSfloor_p) * 100) / OSfloor_p;
					diff_result.put(key, String.format("%.2f", result));
				}
			}
		}
		return diff_result;
	}

	public void makeDiffCollection() {
		DiffCollection diffCollection = DiffCollection.getInstance();
		EdenMarketModel edenMarketData = EdenMarketModel.getInstance();
		OpenSeaMarketModel openSeaMarketData = OpenSeaMarketModel.getInstance();
		diffCollection.setDiff_data(GetDiffHashMap(edenMarketData.getCollection(), openSeaMarketData.getCollection()));
		
	}
}
