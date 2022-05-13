package model;

public class CollectionFactory {

	public Collection GetCollection(String collectionName) {
		if (collectionName == null) {
			return null;
		}
		if (collectionName.equalsIgnoreCase("EdenMarketCollection")) {
			return  EdenMarketModel.instance;
		} 
		else if (collectionName.equalsIgnoreCase("OpenSeaMarketCollection")) {
			return  OpenSeaMarketModel.instance;
		} 
		else if (collectionName.equalsIgnoreCase("diffCollection")) {
			return DiffCollection.instance;
		}
		return null;
	}

}
