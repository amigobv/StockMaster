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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Ticker other = (Ticker) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
