package algorithms.average;

import java.time.LocalDate;
import java.util.List;

import model.Entry;

public abstract class AverageAbstract implements Average {
	protected List<Entry> entries;
	
	public AverageAbstract(List<Entry> entries) {
		this.entries = entries;
		
		calculatePriceChanges();
	}
	
	private void calculatePriceChanges() {
		for (int i = 1; i < entries.size(); i++) {
			double change = entries.get(i).getClose() - entries.get(i-1).getClose();
			
			if (change > 0) entries.get(i).setGain(change);
			else if (change < 0) entries.get(i).setLoss(Math.abs(change));
		}
	}
	
	public Entry getEntryByDay(LocalDate date) {
		for(Entry price : entries) {
			if (price.getDate().equals(date))
				return price;
		}
		
		return null;
	}
	
	protected int getIndexByDate(LocalDate date) {
		Entry price = getEntryByDay(date);
		return entries.indexOf(price);
	}
}
