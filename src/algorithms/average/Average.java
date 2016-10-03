package algorithms.average;

import java.util.Date;

import model.Entry;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Interface which has to be implemented by all average calculation algorithms
 *  
 */
public interface Average {
	/**
	 * Calculate the gain average of a date
	 * @param date
	 * @return gain average
	 */
	public double calculateGainAverage(Date date);
	
	/**
	 * Calculate the loss average of a date
	 * @param date
	 * @return loss average
	 */
	public double calculateLossAverage(Date date);
	
	/**
	 * Iterates through all entries and returns the entry
	 * with the provided date 
	 * 
	 * @param date
	 */
	public Entry getEntryByDay(Date date);
}
