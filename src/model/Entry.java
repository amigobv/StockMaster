package model;

import java.util.Date;

public class Entry {
	private int id;
	private Date date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	private double value;
	private double gain;
	private double loss;
	private double gainAverage;
	private double lossAverage;
	private double rs;
	private double rsi;
	
	public Entry(int id, Date date, double open, double high, double low, double close, double volume, double value) {
		
		this.id = id;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.value = value;
	}
	
	public Entry (Date date, double open, double close) {
		this.date = date;
		this.open = open;
		this.close = close;
	}
	
	public Entry (Date date, double close) {
		this.date = date;
		this.close = close;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public double getOpen() {
		return open;
	}
	
	public void setOpen(double open) {
		this.open = open;
	}
	
	public double getHigh() {
		return high;
	}
	
	public void setHigh(double high) {
		this.high = high;
	}
	
	public double getLow() {
		return low;
	}
	
	public void setLow(double low) {
		this.low = low;
	}
	
	public double getClose() {
		return close;
	}
	
	public void setClose(double close) {
		this.close = close;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public double getLoss() {
		return loss;
	}

	public void setLoss(double loss) {
		this.loss = loss;
	}

	public double getGainAverage() {
		return gainAverage;
	}

	public void setGainAverage(double gainAverage) {
		this.gainAverage = gainAverage;
	}
	
	public double getLossAverage() {
		return lossAverage;
	}

	public void setLossAverage(double lossAverage) {
		this.lossAverage = lossAverage;
	}

	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

}
