package service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import model.Product;
import okhttp3.OkHttpClient;

public class ManualCollectionService {
	private Product newCollection;
	OkHttpClient os_client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS)
			.writeTimeout(5, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();;
	OkHttpClient me_client = new OkHttpClient().newBuilder().build();

	public void AddCollection(String collectionName) {
		ApiService apiService = ApiService.getInstance();
		try {
			String magicEdenCurr = convertMagicEdenFormat(apiService.GetfloorPriceMagicEden(collectionName, me_client));
			String openSeaCurr = convertOpenSeaFormat(apiService.GetfloorPriceOpenSea(collectionName, os_client));
			String diffCurr = setDiffcurr(openSeaCurr, magicEdenCurr);
			newCollection = new Product(collectionName, openSeaCurr, magicEdenCurr, diffCurr);
		} catch (IOException e) {
			System.out.println("New product progrees failed");
		}
	}

	public Product GetCollection() {
		return newCollection;
	}

	private String setDiffcurr(String openSeaCurr, String magicEdenCurr) {
		if (magicEdenCurr.equals("N/A") || openSeaCurr.equals("N/A"))
			return "-";
		else {
			Double MEfloor_p = Double.parseDouble(magicEdenCurr);
			Double OSfloor_p = Double.parseDouble(openSeaCurr);
			Double result = ((MEfloor_p - OSfloor_p) * 100) / OSfloor_p;
			return String.format("%.2f", result);
		}
	}

	public String convertOpenSeaFormat(String floor_price) {
		if (floor_price.equals("N/A"))
			return floor_price;

		double f_price = Double.parseDouble(floor_price);
		f_price = f_price * 39.23;
		return String.format("%.2f", f_price);

	}

	public String convertMagicEdenFormat(String floor_price) {
		if (floor_price.equals("N/A"))
			return floor_price;

		double f_price = Double.parseDouble(floor_price);
		f_price = f_price / 1000000000;
		return String.format("%.2f", f_price);

	}
}
