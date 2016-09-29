package algorithms.average;

import java.time.LocalDate;
import java.util.List;

import model.Entry;

public class ExponentialAverage extends AverageAbstract {
	private int period;
	private double prevGainAvg;
	private double prevLossAvg;
	private double alpha;
	
	public ExponentialAverage(List<Entry> prices, int period, double smooth) {
		super(prices);
		this.period = period;
		
		prevGainAvg = calculateFirstGainAverage();
		prevLossAvg = calculateFirstLossAverage();
		alpha = smooth;
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
    public double calculateGainAverage(LocalDate date) {
    	int dayIdx = getIndexByDate(date);
    	
    	if (dayIdx < period)
    		return 0;
    	
    	if(dayIdx == period) 
    		return prevGainAvg;
    	
    	double currGainAvg = (entries.get(dayIdx).getGain() * alpha) + (prevGainAvg*(1-alpha));
    	prevGainAvg = currGainAvg;
		return currGainAvg;
	}
	
    @Override
	public double calculateLossAverage(LocalDate date) {
		int dayIdx = getIndexByDate(date);

		if (dayIdx < period)
    		return 0;
    	
    	if(dayIdx == period) 
    		return prevLossAvg;

		double currLossAvg = (entries.get(dayIdx).getGain() * alpha)+ (prevLossAvg*(1-alpha));
    	prevLossAvg = currLossAvg;
		return currLossAvg;
	}
}
