package algorithms.average;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import model.Price;

public abstract class AverageAbstract implements Average {
	protected List<Price> prices;
	
	public AverageAbstract(List<Price> prices) {
		this.prices = prices;
		
		calculatePriceChanges();
	}
	
	private void calculatePriceChanges() {
		for (int i = 1; i < prices.size(); i++) {
			double change = prices.get(i).getClose() - prices.get(i-1).getClose();
			
			if (change > 0) prices.get(i).setGain(change);
			else if (change < 0) prices.get(i).setLoss(Math.abs(change));
		}
	}
	
	public Price getPriceByDay(LocalDate date) {
		for(Price price : prices) {
			if (price.getDate().equals(date))
				return price;
		}
		
		return null;
	}
	
	protected int getIndexByDate(LocalDate date) {
		Price price = getPriceByDay(date);
		return prices.indexOf(price);
	}
}
