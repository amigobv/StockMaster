package algorithms.rsi;

import java.time.LocalDate;
import java.util.Date;

import algorithms.average.Average;
import model.Price;

public class Rsi extends RelativeStrengthIndexAbstract {
	private Average avg;

	public Rsi(Average algo) {
		super();
		this.avg = algo;
	}
	
	@Override
	public double calculate(LocalDate date) {
		Price price = avg.getPriceByDay(date);
		
		double gainAvg = avg.calculateGainAverage(date);
		double lossAvg = avg.calculateLossAverage(date);
		double rs = calculateRelativStrength(gainAvg, lossAvg);
		double rsi = calculateRelativeStrengthIndex(rs);
		
		price.Fill(gainAvg, lossAvg, rs, rsi);
		return rsi;
	}
	
	public Average getAvg() {
		return avg;
	}

	public void setAvg(Average avg) {
		this.avg = avg;
	}
}
