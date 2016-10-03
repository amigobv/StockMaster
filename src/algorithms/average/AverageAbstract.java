package algorithms.average;

import java.util.Date;
import java.util.List;

import model.Entry;

/**
 * 
 * @author Daniel Rotaru
 *
 */
public abstract class AverageAbstract implements Average {
	protected List<Entry> entries;
	
	/**
	 * 
	 * @param entries
	 */
	public AverageAbstract(List<Entry> entries) {
		this.entries = entries;
		
		calculatePriceChanges();
	}
	
	/**
	 * Iterates through all entries and calculates the price changes.
	 * The price changes are saved in each entry object.
	 */
	private void calculatePriceChanges() {
		for (int i = 1; i < entries.size(); i++) {
			double change = entries.get(i).getClose() - entries.get(i-1).getClose();
			
			if (change > 0) entries.get(i).setGain(change);
			else if (change < 0) entries.get(i).setLoss(Math.abs(change));
		}
	}
	
	/**
	 * Iterates through all entries and returns the entry
	 * with the provided date 
	 * 
	 * @param date
	 */
	public Entry getEntryByDay(Date date) {
		for(Entry price : entries) {
			if (price.getDate().equals(date))
				return price;
		}
		
		return null;
	}
	
	/**
	 * Iterates through all entries and returns the position in list of the entry
	 * with the provided date 
	 * 
	 * @param date
	 * @return
	 */
	protected int getIndexByDate(Date date) {
		Entry price = getEntryByDay(date);
		return entries.indexOf(price);
	}
}
