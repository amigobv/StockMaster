package core;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class with functions for time.<p>
 * It returns Calendar() dates in a simplified format sdf = "yyyyMMdd".
 * @author KurtOddvar
 *
 */
public class TimeManager {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	Calendar simulationStartDate = Calendar.getInstance();
	Calendar simulationCurrentDate = Calendar.getInstance();
	Calendar simulationEndDate = Calendar.getInstance();
	Calendar todaysDate = Calendar.getInstance();
	Calendar aDate = Calendar.getInstance();
	
	public String getADate() {
		return sdf.format(aDate.getTime());
	}
	
	public void setADate(int year, int month, int day) {	
		this.aDate.set(year, month, day);
	}
	
	/**
	 * Get the day of aDate
	 * @return Day 01 - 31
	 */
	public int getADateDay() {
		return aDate.get(Calendar.DATE);
	}
	
	/**
	 * Get the month of aDate
	 * @return Month 01 - 12
	 */
	public int getADateMonth() {
		return aDate.get(Calendar.MONTH);
	}
	
	/**
	 * Get the year of aDate
	 * @return Year 
	 */
	public int getADateYear() {
		return aDate.get(Calendar.YEAR);
	}
	
	public void aDateSubtractOneDay() {
		this.aDate.add(Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * Subtracting one day from simulationCurrentDate
	 */
	public void todaysDateSubtractOneDay() {
		this.todaysDate.add(Calendar.DAY_OF_MONTH, -1);
	}
	
	public String getTodaysDate() {
		return sdf.format(todaysDate.getTime());
	}

	public void setTodaysDate(int year, int month, int day) {	
		this.todaysDate.set(year, month, day);
	}

	/**
	 * Get the year of simulationCurrentDate
	 * @return YEAR in format 2015
	 */
	public int getSimulationCurrentYear() {
		return simulationCurrentDate.get(Calendar.YEAR);
	}
	
	/**
	 * Get the month of simulationCurrentDate
	 * @return Month in format 0, 1, 2...<br>
	 * 0 = January<br>
	 * 1 = February<br>
	 * 2 = ...
	 */
	public int getSimulationCurrentMonth() {
		return simulationCurrentDate.get(Calendar.MONTH);
	}
	
	/**
	 * Get the day of simulationCurrentDate
	 * @return Day 01 - 31
	 */
	public int getSimulationCurrentDay() {
		return simulationCurrentDate.get(Calendar.DATE);
	}
	
	/**
	 * Adding one day to simulationCurrentDate
	 */
	public void simulationCurrentDateAddOneDay() {
		this.simulationCurrentDate.add(Calendar.DAY_OF_MONTH, +1);
	}
	
	/**
	 * Subtracting one day from simulationCurrentDate
	 */
	public void simulationCurrentDateSubtractOneDay() {
		this.simulationCurrentDate.add(Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * Method for setting the starting date
	 * @param year Year, in format 2005, 2015 etc
	 * @param month Month in format 00 January, 11 December
	 * @param day Day in format 01, 31
	 *  
	 */
	public void setSimulationStartDate(int year, int month, int day) {	
		this.simulationStartDate.set(year, month, day);		
	}
	
	/**
	 * Method for setting the current date
	 * @param year Year, in format 2005, 2015 etc
	 * @param month Month in format 00 January, 11 December
	 * @param day Day in format 01, 31
	 */
	public void setSimulationCurrentDate(int year, int month, int day) {	
		this.simulationCurrentDate.set(year, month, day);		
	}
	
	/**
	 * Method for setting the ending date
	 * @param year Year, in format 2005, 2015 etc
	 * @param month Month in format 00 January, 11 December
	 * @param day Day in format 01, 31
	 */
	public void setSimulationEndDate(int year, int month, int day) {	
		this.simulationEndDate.set(year, month, day);		
	}
	
	/**
	 * Method for getting starting date
	 * @return The starting date in format 20150101
	 */
	public String getSimulationStartDate() {
		return sdf.format(simulationStartDate.getTime());
	}
	
	/**
	 * @return Current date in format 20150101
	 */
	public String getSimulationCurrentDate() {
		return sdf.format(simulationCurrentDate.getTime());
	}
	
	/**
	 * @return Ending date in format 20150101
	 */
	public String getSimulationEndDate() {
		return sdf.format(simulationEndDate.getTime());
	}
	
	/**
	 * Method will check a day and see if it is a weekday or a weekend
	 * @return Return true if this is a weekday. False if it is Saturday or Sunday.
	 */
	public boolean checkIfCurrentDateIsWeekday() {
		
		simulationCurrentDate.set(getSimulationCurrentYear(), getSimulationCurrentMonth(), getSimulationCurrentDay());
		int dayOfWeek = simulationCurrentDate.get(Calendar.DAY_OF_WEEK);
         if (dayOfWeek == Calendar.SATURDAY) { // If it's Saturday
             return false;
         }
         if (dayOfWeek == Calendar.SUNDAY) { // If it's Sunday
             return false;
         }
         return true;
	}
	
	/**
	 * Method will print the name of the current day.
	 * @return Monday, Tuesday, ... Sunday
	 */
	public String printCurrentWeekDay() {
		simulationCurrentDate.set(getSimulationCurrentYear(), getSimulationCurrentMonth(), getSimulationCurrentDay());
		int temp = simulationCurrentDate.get(Calendar.DAY_OF_WEEK);
		switch (temp) {
		case 1:
			return "Sunday";
		case 2:
			return "Monday";
		case 3:
			return "Tuesday";
		case 4:
			return "Wednesday";
		case 5:
			return "Thursday";
		case 6:
			return "Friday";
		case 7:
			return "Saturday";
		default:
			return "Unknown";
		}
	}
} // End class