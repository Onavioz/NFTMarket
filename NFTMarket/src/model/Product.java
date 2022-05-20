package model;

public class Product {
	String name;
	String Opensea_curr;
	String MagicEden_curr;
	String Diff_curr;

	public Product(String name, String Opensea_curr, String MagicEden_curr, String Diff_curr) {
		this.name = name;
		this.Opensea_curr = Opensea_curr;
		this.MagicEden_curr = MagicEden_curr;
		this.Diff_curr = Diff_curr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpensea_curr() {
		return Opensea_curr;
	}

	public void setOpensea_curr(String Opensea_curr) {
		this.Opensea_curr = Opensea_curr;
	}

	public String getMagicEden_curr() {
		return MagicEden_curr;
	}

	public void setMagicEden_curr(String MagicEden_curr) {
		this.MagicEden_curr = MagicEden_curr;
	}

	public String getDiff_curr() {
		return Diff_curr;
	}

	public void setDiff_curr(String Diff_curr) {
		this.Diff_curr = Diff_curr;
	}
}