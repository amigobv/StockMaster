package algorithms.average;

import java.time.LocalDate;
import java.util.Date;

import model.Price;

public interface Average {
	public double calculateGainAverage(LocalDate date);
	public double calculateLossAverage(LocalDate date);
	public Price getPriceByDay(LocalDate date);
}
