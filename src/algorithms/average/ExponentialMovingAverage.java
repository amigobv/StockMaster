package algorithms.average;

import java.util.Date;
import java.util.List;

import model.Entry;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Use this class to calculate the exponential moving average
 *
 */
public class ExponentialMovingAverage extends AverageAbstract {
	private int period;
	private double prevGainAvg;
	private double prevLossAvg;
	
	/**
	 * The constructor expects a list of entries which will be use for the calculation
	 * and the desired period 
	 * 
	 * @param entries
	 * @param period
	 */
	public ExponentialMovingAverage(List<Entry> entries, int period) {
		super(entries);
		this.period = period;
		
		prevGainAvg = calculateFirstGainAverage();
		prevLossAvg = calculateFirstLossAverage();
	}

	/**
	 * Use to calculate the first loss average by adding the gains
	 * of the first 14 days and divide the sum by 14
	 * @return first gain average
	 */
	private double calculateFirstGainAverage()  {
        double avg = 0.0;
        for (int i = 1; i <= period; i++)
        {
            avg += entries.get(i).getGain();
        }

        return avg/period;
    }

	/**
	 * Use to calculate the first loss average by adding the losses
	 * of the first 14 days and divide the sum by 14
	 * 
	 * @return first loss average
	 */
    private double calculateFirstLossAverage()  {
        double avg = 0.0;
        for (int i = 1; i <= period; i++)
        {
        	double val = entries.get(i).getLoss();
            avg += val;
        }

        return avg/period;
    }
    
	/**
	 * Calculate the gain average of a date
	 * @param date
	 * @return gain average
	 */
    @Override
    public double calculateGainAverage(Date date) {
    	int dayIdx = getIndexByDate(date);
    	
    	if (dayIdx < period)
    		return 0;
    	
    	if(dayIdx == period) 
    		return prevGainAvg;

    	
    	double currGainAvg = ((prevGainAvg * (period - 1)) + entries.get(dayIdx).getGain()) / period;
    	prevGainAvg = currGainAvg;
		return currGainAvg;
	}
	
	/**
	 * Calculate the loss average of a date
	 * @param date
	 * @return loss average
	 */
    @Override
	public double calculateLossAverage(Date date) {
		int dayIdx = getIndexByDate(date);
		
		if (dayIdx < period)
    		return 0;
		
		if(dayIdx == period)
    		return prevLossAvg;

		double currLossAvg = ((prevLossAvg * (period - 1)) + entries.get(dayIdx).getLoss()) / period;
    	prevLossAvg = currLossAvg;
		return currLossAvg;
	}
}
