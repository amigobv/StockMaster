package model;

import java.util.ArrayList;

public class Stock {
	private String name;
	private ArrayList<Price> price;
	
	public Stock() {
	}
	
	public Stock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Price> getPrice() {
		return price;
	}

	public void setPrice(ArrayList<Price> price) {
		this.price = price;
	}
}
