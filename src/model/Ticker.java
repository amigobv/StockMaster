package model;

import java.util.ArrayList;


public class Ticker {
	private int id;
	private String name;
	private String symbol;
	private String exchange;
	private ArrayList<Entry> entries;
	
	public Ticker() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Ticker(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Entry> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<Entry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(Entry entry) {
		this.entries.add(entry);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
}
