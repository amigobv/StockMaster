package model;

import java.util.Date;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Domain class for the entry table
 *
 */
public class Entry extends Model{
	private Date date;
	private Ticker ticker;
	private double close;
	private double change;
	private Date dateOfChange;
	private double priceAtChange;
	private double wma10;
	private double wma20;
	private double wma100;
	private Recommandation recommandation;
	private double percentageSinceRecommandation;
	private int rating;
	private int previousRating;
	private Date previousRatingDate;

	private double gain;
	private double loss;
	private double gainAverage;
	private double lossAverage;
	private double rs;
	private double rsi;
	
	
	public Entry() {
		super(-1);
	}
	
	
	
	public Entry(int id, Date date, Ticker ticker, double close, double change, Date dateOfChange, double priceAtChange,
				 double wma10, double wma20, double wma100, Recommandation recommandation, double percentageSinceRecommandation,
				 int rating, int previousRating, Date previousRatingDate, double rsi) {
		super(id);
		
		this.date = date;
		this.ticker = ticker;
		this.close = close;
		this.change = change;
		this.dateOfChange = dateOfChange;
		this.priceAtChange = priceAtChange;
		this.wma10 = wma10;
		this.wma20 = wma20;
		this.wma100 = wma100;
		this.recommandation = recommandation;
		this.percentageSinceRecommandation = percentageSinceRecommandation;
		this.rating = rating;
		this.previousRating = previousRating;
		this.previousRatingDate = previousRatingDate;
		this.rsi = rsi;
	}
	
	public Entry (Ticker ticker, Date date, double close) {
		this();
		this.ticker = ticker;
		this.date = date;
		this.dateOfChange = date;
		this.previousRatingDate = date;
		this.recommandation = Recommandation.STAY;
		this.close = close;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	public double getClose() {
		return close;
	}
	
	public void setClose(double close) {
		this.close = close;
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

	public Ticker getTicker() {
		return ticker;
	}

	public void setTicker(Ticker ticker) {
		this.ticker = ticker;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public Date getDateOfChange() {
		return dateOfChange;
	}

	public void setDateOfChange(Date dateOfChange) {
		this.dateOfChange = dateOfChange;
	}

	public double getPriceAtChange() {
		return priceAtChange;
	}

	public void setPriceAtChange(double priceAtChange) {
		this.priceAtChange = priceAtChange;
	}

	public double getWma10() {
		return wma10;
	}

	public void setWma10(double wma10) {
		this.wma10 = wma10;
	}

	public double getWma20() {
		return wma20;
	}

	public void setWma20(double wma20) {
		this.wma20 = wma20;
	}

	public double getWma100() {
		return wma100;
	}

	public void setWma100(double wma100) {
		this.wma100 = wma100;
	}

	public Recommandation getRecommandation() {
		return recommandation;
	}

	public void setRecommandation(Recommandation recommandation) {
		this.recommandation = recommandation;
	}

	public double getPercentageSinceRecommandation() {
		return percentageSinceRecommandation;
	}

	public void setPercentageSinceRecommandation(double percentageSinceRecommandation) {
		this.percentageSinceRecommandation = percentageSinceRecommandation;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getPreviousRating() {
		return previousRating;
	}

	public void setPreviousRating(int previousRating) {
		this.previousRating = previousRating;
	}

	public Date getPreviousRatingDate() {
		return previousRatingDate;
	}

	public void setPreviousRatingDate(Date previousRatingDate) {
		this.previousRatingDate = previousRatingDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Entry other = (Entry) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return date + " " + close;
	}
}
