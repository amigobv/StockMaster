package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import algorithms.average.Average;
import algorithms.average.ExponentialAverage;
import algorithms.average.MovingAverage;
import algorithms.rsi.Rsi;
import helpers.DateUtils;
import model.Entry;

public class RsiTest {
	private List<Entry> entries;
	
	@Test
	public void movingAverageRsiTest() {
		final int PERIOD = 14;
		FillClientValues();
		
		System.out.println("RSI with moving average");
		System.out.println("No\tDate\t\tClose\tUp\tDown\tGainAvg\tLossAvg\tRS\tRSI");
		int count = 1;
		Average algo = new MovingAverage(entries, PERIOD);
		Rsi rsi = new Rsi(algo);
		
        for (Entry entry : entries)
        {
        	rsi.calculate(entry.getDate());
        
        	System.out.println(String.format("%d\t%s\t%4.2f\t%4.2f\t%4.2f\t%4.4f\t%4.4f\t%4.4f\t%4.4f", 
        																			count++, 
        																			entry.getDate().toString(), 
        																			entry.getClose(),
        																			entry.getGain(),
        																			entry.getLoss(),
        																			entry.getGainAverage(),
        																			entry.getLossAverage(),
        																			entry.getRs(),
        																			entry.getRsi()));
        	
        	
        }
	}
	
	@Test
	public void exponentialAverageRsiTest() {
		final int PERIOD = 14;
		int count = 1;
		double smooth = 2.0 /(PERIOD + 1);
		
		FillClientValues();
		
		System.out.println("RSI with exponential average");
		System.out.println("No\tDate\t\tClose\tUp\tDown\tGainAvg\tLossAvg\tRS\tRSI");

		
		Average algo = new ExponentialAverage(entries, PERIOD, smooth);
		Rsi rsi = new Rsi(algo);
		
        for (Entry price : entries)
        {
        	rsi.calculate(price.getDate());
        
        	System.out.println(String.format("%d\t%s\t%4.2f\t%4.2f\t%4.2f\t%4.4f\t%4.4f\t%4.4f\t%4.4f", 
        																			count++, 
        																			price.getDate().toString(), 
        																			price.getClose(),
        																			price.getGain(),
        																			price.getLoss(),
        																			price.getGainAverage(),
        																			price.getLossAverage(),
        																			price.getRs(),
        																			price.getRsi()));
        }
	}

	
	private void FillClientValues() {
		entries = new ArrayList<Entry>();
		
		
		// source: https://www.oslobors.no/ob_eng/markedsaktivitet/#/details/STL.OSE/overview
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 28)), 125.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 27)), 124.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 26)), 126.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 23)), 128.20));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 22)), 128.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 21)), 126.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 20)), 125.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 19)), 126.70));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 16)), 125.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 15)), 124.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 14)), 125.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 13)), 126.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 12)), 128.70));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  9)), 130.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  8)), 134.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  7)), 134.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  6)), 133.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  5)), 135.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  2)), 132.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9,  1)), 129.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 31)), 131.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 30)), 134.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 29)), 132.70));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 26)), 132.70));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 25)), 132.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 24)), 132.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 23)), 131.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 22)), 132.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 19)), 134.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 18)), 134.20));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 17)), 132.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 16)), 135.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 15)), 134.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 12)), 133.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 11)), 133.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 10)), 133.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  9)), 136.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  8)), 135.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  5)), 134.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  4)), 133.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  3)), 131.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  2)), 130.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8,  1)), 132.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 29)), 132.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 28)), 135.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 27)), 140.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 26)), 142.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 25)), 141.20));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 22)), 144.90));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 21)), 149.20));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 20)), 147.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 19)), 147.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 18)), 147.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 15)), 149.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 14)), 149.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 13)), 149.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 12)), 149.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7, 11)), 148.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7,  8)), 146.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7,  7)), 148.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7,  6)), 142.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7,  5)), 145.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7,  4)), 147.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 7,  1)), 146.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 30)), 144.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 29)), 142.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 28)), 137.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 27)), 135.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 24)), 138.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 23)), 141.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 22)), 139.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 21)), 136.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 20)), 138.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 17)), 135.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 16)), 130.20));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 15)), 133.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 14)), 133.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 13)), 135.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6, 10)), 136.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  9)), 138.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  8)), 139.90));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  7)), 137.70));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  6)), 135.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  3)), 131.70));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  2)), 131.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 6,  1)), 131.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 31)), 133.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 30)), 135.80));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 27)), 135.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 26)), 138.10));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 25)), 138.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 24)), 135.40));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 23)), 134.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 20)), 134.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 19)), 133.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 18)), 138.50));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 13)), 135.30));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 12)), 136.60));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 11)), 135.00));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5, 10)), 133.90));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 5,  9)), 133.90));
		
		Collections.reverse(entries);
	}
}
