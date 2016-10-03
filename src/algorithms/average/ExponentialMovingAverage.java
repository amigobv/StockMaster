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
	
	/**
	 * The constructor expects a list of entries which will be use for the calculation
	 * and the desired period.
	 * 
	 * @param entries
	 * @param period
	 */
	public ExponentialMovingAverage(List<Entry> entries, int period) {
		super(entries);
			
		this.period = period;
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
    	
    	double prevAvg = calculateFirstGainAverage();
    	
    	if(dayIdx == period) 
    		return prevAvg;

    	for (int i = period + 1; i <= dayIdx; i++) {
    		double currGainAvg = ((prevAvg * (period - 1)) + entries.get(i).getGain()) / period;
    		prevAvg = currGainAvg;
    		entries.get(i).setGainAverage(currGainAvg);
    	}
    	
		return entries.get(dayIdx).getGainAverage();
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
    		return calculateFirstLossAverage();

    	double prevAvg = calculateFirstLossAverage();
    	for (int i = period + 1; i <= dayIdx; i++) {
    		double currLossAvg = ((prevAvg * (period - 1)) + entries.get(i).getLoss()) / period;
    		prevAvg = currLossAvg;
    		entries.get(i).setLossAverage(currLossAvg);
    	}
    	
		return entries.get(dayIdx).getLossAverage();
	}
}
