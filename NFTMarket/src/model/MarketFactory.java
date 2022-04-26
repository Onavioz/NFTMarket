package model;

public class MarketFactory {

	public Market createMarket(String marketName) {
		if(marketName == null) {
			return null;
		}
		if(marketName.equalsIgnoreCase("EdenMarket")) {
			return new EdenMarketModel();
		}
		else if(marketName.equalsIgnoreCase("OpenSeaMarket")) {
			return new OpenSeaMarketModel();
		}
		return null;
	}
	
}
