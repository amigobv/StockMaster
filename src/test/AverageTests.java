package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import algorithms.average.Average;
import algorithms.average.ExponentialAverage;
import algorithms.average.MovingAverage;
import helpers.DateUtils;
import model.Entry;

public class AverageTests {
	private List<Entry> entries;

	@Test
	public void movingAverageTest() {
		final int PERIOD = 14;
		Fill();

		System.out.println("Moving Average");
		System.out.println("No\tDate\t\tClose\tUp\tDown\tGainAvg\tLossAvg");
		int count = 1;
		Average algo = new MovingAverage(entries, PERIOD);

		for (Entry entry : entries) {
			entry.setGainAverage(algo.calculateGainAverage(entry.getDate()));
			entry.setLossAverage(algo.calculateLossAverage(entry.getDate()));

			System.out.println(String.format("%d\t%s\t%4.2f\t%4.2f\t%4.2f\t%4.2f\t%4.2f", count++,
					entry.getDate().toString(), entry.getClose(), entry.getGain(), entry.getLoss(),
					entry.getGainAverage(), entry.getLossAverage()));
		}
	}

	public void exponentialAverageTest() {
		final int PERIOD = 14;
		double smooth = 2 / (PERIOD + 1);
		Fill();

		System.out.println("Exponential Average");
		System.out.println("No\tDate\t\tClose\tUp\tDown\tGainAvg\tLossAvg");
		int count = 1;
		Average algo = new ExponentialAverage(entries, PERIOD, smooth);

		for (Entry entry : entries) {
			entry.setGainAverage(algo.calculateGainAverage(entry.getDate()));
			entry.setLossAverage(algo.calculateLossAverage(entry.getDate()));

			System.out.println(String.format("%d\t%s\t%4.2f\t%4.2f\t%4.2f\t%4.2f\t%4.2f", count++,
					entry.getDate().toString(), entry.getClose(), entry.getGain(), entry.getLoss(),
					entry.getGainAverage(), entry.getLossAverage()));

		}
	}

	private void Fill() {
		entries = new ArrayList<Entry>();

		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 30)), 134.0));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 8, 31)), 131.8));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 1)), 129.0));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 2)), 132.0));
                              
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 5)), 135.0));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 6)), 133.0));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 7)), 134.4));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 8)), 134.1));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 9)), 130.6));
                              
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 12)), 128.7));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 13)), 126.1));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 14)), 125.6));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 15)), 124.1));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 16)), 125.1));
                              
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 19)), 126.7));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 20)), 125.4));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 21)), 126.1));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 22)), 128.5));
		entries.add(new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 23)), 128.2));
	}

}
