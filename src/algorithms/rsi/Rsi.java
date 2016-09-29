package algorithms.rsi;

import java.util.Date;

import algorithms.average.Average;
import model.Entry;

public class Rsi extends RelativeStrengthIndexAbstract {
	private Average avg;

	public Rsi(Average algo) {
		super();
		this.avg = algo;
	}
	
	@Override
	public double calculate(Date date) {
		Entry entry = avg.getEntryByDay(date);
		
		double gainAvg = avg.calculateGainAverage(date);
		double lossAvg = avg.calculateLossAverage(date);
		double rs = calculateRelativStrength(gainAvg, lossAvg);
		double rsi = calculateRelativeStrengthIndex(rs);
		
		entry.setGainAverage(gainAvg);
		entry.setLossAverage(lossAvg);
		entry.setRs(gainAvg);
		entry.setRsi(rsi);
		
		return rsi;
	}
	
	public Average getAvg() {
		return avg;
	}

	public void setAvg(Average avg) {
		this.avg = avg;
	}
}
