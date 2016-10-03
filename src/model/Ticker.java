package model;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Domain class for the ticker table
 *
 */
public class Ticker extends Model{
	private String name;
	private String symbol;
	private String exchange;
	
	public Ticker() {
		super(-1);
	}
	public Ticker(int id) {
		super(id);
	}
	public Ticker(int id, String name) {
		this(id);
		this.name = name;
	}
	
	public Ticker(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
