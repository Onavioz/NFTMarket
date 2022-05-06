package model;

import java.util.HashMap;

public class DiffCollection implements Collection {
	public static DiffCollection instance = new DiffCollection();
	private HashMap<String, String> diff_data = new HashMap<String, String>();

	public static DiffCollection getInstance() {
		return instance;
	}

	public void setDiff_data(HashMap<String, String> diff_data) {
		this.diff_data = diff_data;
	}

	@Override
	public HashMap<String, String> getCollection() {
		return diff_data;
	}

}
