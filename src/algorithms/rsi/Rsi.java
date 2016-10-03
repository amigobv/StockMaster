package algorithms.rsi;

import java.util.Date;

import algorithms.average.Average;
import model.Entry;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Calculates the Relative Strength Index (RSI)
 * 
 * In order the have a flexible RSI algorithm the average calculation algorithm
 * is provided at the instantiation. This way the user can decide at runtime which
 * average algorithm will be used for the RSI calculation.
 * 
 * ATTENTION: The RSI Algorithm needs at least 100 Entries in order to calculate correctly
 *
 */
public class Rsi extends RelativeStrengthIndexAlgo {
	private Average avg;

	/**
	 * The constructor expects the average algorithm which should be used
	 * for the RSI calculation (Inverse of Control)
	 * 
	 * @param algo
	 */
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
