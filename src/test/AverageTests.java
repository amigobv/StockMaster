package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import algorithms.average.Average;
import algorithms.average.ExponentialAverage;
import algorithms.average.MovingAverage;
import model.Price;

public class AverageTests {
	private List<Price> prices;
	
	@Test
	public void movingAverageTest() {
		final int PERIOD = 14;
		Fill();
		
		System.out.println("Moving Average");
		System.out.println("No\tDate\t\tClose\tUp\tDown\tGainAvg\tLossAvg");
		int count = 1;
		Average algo = new MovingAverage(prices, PERIOD);
		
        for (Price price : prices)
        {
        	price.setGainAverage(algo.calculateGainAverage(price.getDate()));
        	price.setLossAverage(algo.calculateLossAverage(price.getDate()));
        	
        	System.out.println(String.format("%d\t%s\t%4.2f\t%4.2f\t%4.2f\t%4.2f\t%4.2f", count++, 
        																			price.getDate().toString(), 
        																			price.getClose(),
        																			price.getGain(),
        																			price.getLoss(),
        																			price.getGainAverage(),
        																			price.getLossAverage()));
        	
        	
        }
	}
	
	public void exponentialAverageTest() {
		final int PERIOD = 14;
		double smooth = 2 / (PERIOD + 1);
		Fill();
		
		System.out.println("Exponential Average");
		System.out.println("No\tDate\t\tClose\tUp\tDown\tGainAvg\tLossAvg");
		int count = 1;
		Average algo = new ExponentialAverage(prices, PERIOD, smooth);
		
        for (Price price : prices)
        {
        	price.setGainAverage(algo.calculateGainAverage(price.getDate()));
        	price.setLossAverage(algo.calculateLossAverage(price.getDate()));
        	
        	System.out.println(String.format("%d\t%s\t%4.2f\t%4.2f\t%4.2f\t%4.2f\t%4.2f", count++, 
        																			price.getDate().toString(), 
        																			price.getClose(),
        																			price.getGain(),
        																			price.getLoss(),
        																			price.getGainAverage(),
        																			price.getLossAverage()));
        	
        	
        }
	}
	
	private void Fill() {
		prices = new ArrayList<Price>();
		
		prices.add(new Price(LocalDate.of(2016, 8, 30), 134.0));
		prices.add(new Price(LocalDate.of(2016, 8, 31), 131.8));
		prices.add(new Price(LocalDate.of(2016, 9, 1), 129.0));
		prices.add(new Price(LocalDate.of(2016, 9, 2), 132.0));
		
		prices.add(new Price(LocalDate.of(2016, 9, 5), 135.0));
		prices.add(new Price(LocalDate.of(2016, 9, 6), 133.0));
		prices.add(new Price(LocalDate.of(2016, 9, 7), 134.4));
		prices.add(new Price(LocalDate.of(2016, 9, 8), 134.1));
		prices.add(new Price(LocalDate.of(2016, 9, 9), 130.6));
		
		prices.add(new Price(LocalDate.of(2016, 9, 12), 128.7));
		prices.add(new Price(LocalDate.of(2016, 9, 13), 126.1));
		prices.add(new Price(LocalDate.of(2016, 9, 14), 125.6));
		prices.add(new Price(LocalDate.of(2016, 9, 15), 124.1));
		prices.add(new Price(LocalDate.of(2016, 9, 16), 125.1));
		
		prices.add(new Price(LocalDate.of(2016, 9, 19), 126.7));
		prices.add(new Price(LocalDate.of(2016, 9, 20), 125.4));
		prices.add(new Price(LocalDate.of(2016, 9, 21), 126.1));
		prices.add(new Price(LocalDate.of(2016, 9, 22), 128.5));
		prices.add(new Price(LocalDate.of(2016, 9, 23), 128.2));
	}

}
