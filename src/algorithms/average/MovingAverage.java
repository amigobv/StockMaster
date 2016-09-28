package algorithms.average;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import model.Price;

public class MovingAverage extends AverageAbstract {
	private int period;
	private double prevGainAvg;
	private double prevLossAvg;
	
	public MovingAverage(List<Price> prices, int period) {
		super(prices);
		this.period = period;
		
		prevGainAvg = calculateFirstGainAverage();
		prevLossAvg = calculateFirstLossAverage();
	}

	private double calculateFirstGainAverage()  {
        double avg = 0.0;
        for (int i = 1; i <= period; i++)
        {
            avg += prices.get(i).getGain();
        }

        return avg/period;
    }

    private double calculateFirstLossAverage()  {
        double avg = 0.0;
        for (int i = 1; i <= period; i++)
        {
        	double val = prices.get(i).getLoss();
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

    	
    	double currGainAvg = ((prevGainAvg * (period - 1)) + prices.get(dayIdx).getGain()) / period;
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

		double currLossAvg = ((prevLossAvg * (period - 1)) + prices.get(dayIdx).getLoss()) / period;
    	prevLossAvg = currLossAvg;
		return currLossAvg;
	}
}
