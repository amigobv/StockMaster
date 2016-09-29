package algorithms.average;

import java.time.LocalDate;

import model.Entry;

public interface Average {
	public double calculateGainAverage(LocalDate date);
	public double calculateLossAverage(LocalDate date);
	public Entry getEntryByDay(LocalDate date);
}
