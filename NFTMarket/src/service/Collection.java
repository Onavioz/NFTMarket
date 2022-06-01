package service;

import java.util.Comparator;

public class Collection implements Comparator<Collection>{
	private String collection_name;
	private String opensea_price;
	private String magic_eden_price;
	private String diff;

	public Collection(String collection_name, String opensea_price, String magic_eden_price, String diff) {
		this.collection_name = collection_name;
		this.opensea_price = opensea_price;
		this.magic_eden_price = magic_eden_price;
		this.diff = diff;
	}

	public String getCollection_name() {
		return collection_name;
	}

	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}

	public String getOpensea_price() {
		return opensea_price;
	}

	public void setOpensea_price(String opensea_price) {
		this.opensea_price = opensea_price;
	}

	public String getMagic_eden_price() {
		return magic_eden_price;
	}

	public void setMagic_eden_price(String magic_eden_price) {
		this.magic_eden_price = magic_eden_price;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	@Override
	public int compare(Collection o1, Collection o2) {
		return o1.getCollection_name().compareTo(o2.getCollection_name());
	}

}