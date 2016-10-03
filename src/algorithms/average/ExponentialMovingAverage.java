package algorithms.average;

import java.util.Date;
import java.util.List;

import model.Entry;

public class ExponentialMovingAverage extends AverageAbstract {
	private int period;
	private double prevGainAvg;
	private double prevLossAvg;
	
	public ExponentialMovingAverage(List<Entry> prices, int period) {
		super(prices);
		this.period = period;
		
		prevGainAvg = calculateFirstGainAverage();
		prevLossAvg = calculateFirstLossAverage();
	}

	private double calculateFirstGainAverage()  {
        double avg = 0.0;
        for (int i = 1; i <= period; i++)
        {
            avg += entries.get(i).getGain();
        }

        return avg/period;
    }

    private double calculateFirstLossAverage()  {
        double avg = 0.0;
        for (int i = 1; i <= period; i++)
        {
        	double val = entries.get(i).getLoss();
            avg += val;
        }

        return avg/period;
    }
    
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
