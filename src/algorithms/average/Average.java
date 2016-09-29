package algorithms.average;

import java.util.Date;

import model.Entry;

public interface Average {
	public double calculateGainAverage(Date date);
	public double calculateLossAverage(Date date);
	public Entry getEntryByDay(Date date);
}
