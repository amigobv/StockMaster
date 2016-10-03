package core;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import algorithms.average.Average;
import algorithms.average.ExponentialMovingAverage;
import algorithms.rsi.Rsi;
import model.Entry;
import model.Ticker;

/**
 * @author KurtOddvar
 */

public class DataManager {

	String firstEncounteredDate;
	String dataSplitBy = ",";
	int counter = 0;
	BufferedReader br = null;
	String line = "";
	File temp = new File("temp.txt");
	String newFileName = "";

	ArrayList<String> ratings11 = new ArrayList<>();
	ArrayList<String> ratings12 = new ArrayList<>();
	ArrayList<String> ratings13 = new ArrayList<>();
	ArrayList<String> ratings14 = new ArrayList<>();
	ArrayList<String> ratings15 = new ArrayList<>();
	ArrayList<String> ratings21 = new ArrayList<>();
	ArrayList<String> ratings22 = new ArrayList<>();
	ArrayList<String> ratings23 = new ArrayList<>();
	ArrayList<String> ratings24 = new ArrayList<>();
	ArrayList<String> ratings25 = new ArrayList<>();

	ArrayList<String> easyRatings11 = new ArrayList<>();
	ArrayList<String> easyRatings12 = new ArrayList<>();
	ArrayList<String> easyRatings13 = new ArrayList<>();
	ArrayList<String> easyRatings14 = new ArrayList<>();
	ArrayList<String> easyRatings15 = new ArrayList<>();
	ArrayList<String> easyRatings21 = new ArrayList<>();
	ArrayList<String> easyRatings22 = new ArrayList<>();
	ArrayList<String> easyRatings23 = new ArrayList<>();
	ArrayList<String> easyRatings24 = new ArrayList<>();
	ArrayList<String> easyRatings25 = new ArrayList<>();

	int weight1, weight2, weight3, currentRating, previousRating;

	/**
	 * Method used for getting price "Live" (15 mins delay).
	 * 
	 * @param ticker
	 *            The ticker of the stock.
	 * @return Return last price the stock has been traded for. Gets this value
	 *         from type LiveFile.
	 */
	public double getLastPriceLive(String ticker) {

		String fileName = "LiveTickers/" + ticker + ".txt";
		double lastPrice = 0;

		// Finding last price of stock (last line of file)
		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // skipping first line because it contains text only

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] stockTable = line.split(dataSplitBy);
				lastPrice = Double.parseDouble(stockTable[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lastPrice;

	} // End class getLastPrice

	/**
	 * Method for finding last change of rating from a given date. E.g the
	 * rating was 4 at 20150215. When did it change to that? Compares last price
	 * to weights moving average (WMA).
	 * 
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL etc.
	 * @param weight1
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight2
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight3
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @return Date the rating changed to its current value
	 */
	public String getRatingChangeDate(String ticker, int weight1, int weight2, int weight3, String date) {

		// Algorithm: string -> date -> cal -> cal sdf = argument for
		// givenRating()
		Date currentDate = new Date();
		Calendar cal = new GregorianCalendar();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			currentDate = format.parse(date);
		} catch (ParseException e) {
			System.out.println("This date could not be parsed!");
			e.printStackTrace();
		}

		String changeDate = new String(); // The date of change
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		if ((dayOfWeek == Calendar.SUNDAY)) {
			cal.add(Calendar.DAY_OF_MONTH, -2);
		}
		if ((dayOfWeek == Calendar.SATURDAY)) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		changeDate = format.format(cal.getTime()); // Setting date of change to
		// currentDate-1 (The day
		// before).

		int nowRating = evaluateAtDate(ticker, weight1, weight2, weight3, date); // Find
		// rating
		// at
		// currentDate
		int oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);

		if (nowRating == 0) {
			return "0";
		} // If rating at today's date is zero, we just give up and return null.

		while (oldRating == nowRating) { // While the ratings are equal, we keep
			// subtracting one day from
			// changeDate.

			cal.add(Calendar.DAY_OF_MONTH, -1);
			changeDate = format.format(cal.getTime());
			oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);

			if (oldRating == 0) {
				try {
					while (oldRating == 0) {
						cal.add(Calendar.DAY_OF_MONTH, -1);
						changeDate = format.format(cal.getTime());
						oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);
					}
				} catch (Exception e) {
					return "0";
				}
			}
		}
		if (oldRating != nowRating) { // If we are out of the loop and the
			// ratings are different, add one day.
			// It changed the day after.
			dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (!(((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)))) {
				cal.add(Calendar.DAY_OF_MONTH, +1);
				changeDate = format.format(cal.getTime());
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			}
			if ((dayOfWeek == Calendar.SUNDAY)) {
				cal.add(Calendar.DAY_OF_MONTH, +1);
				changeDate = format.format(cal.getTime());
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			}
			if ((dayOfWeek == Calendar.SATURDAY)) {
				cal.add(Calendar.DAY_OF_MONTH, +2);
				changeDate = format.format(cal.getTime());
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			}
		}

		return changeDate;
	}

	/**
	 * Method for finding last change of rating from today's date. Compares last
	 * price to weights moving average (WMA).
	 * 
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL etc.
	 * @param weight1
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight2
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight3
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @return Date the rating changed to its current value
	 */
	public String getRatingChangeDate(String ticker, int weight1, int weight2, int weight3) {

		// TODO string -> date -> cal -> cal sdf = argument for givenRating()

		// Evaluating to set first encounter date
		evaluate(ticker, weight1, weight2, weight3);
		String date = firstEncounteredDate;
		Date myDate = new Date();
		Calendar cal = new GregorianCalendar();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			myDate = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("This date could not be parsed!");
			e.printStackTrace();
		}

		// System.out.println("Evaluating: " +ticker +" " +date);
		String prosessedDate = new String();

		// System.out.println("NowRating is: " +nowRating);
		cal.setTime(myDate);
		int nowRating = evaluate(ticker, weight1, weight2, weight3);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		prosessedDate = format.format(cal.getTime());
		int oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, prosessedDate);
		if (nowRating == 0) {
			return "0";
		}
		// System.out.println("Old rating at top of loop; " +oldRating);
		// System.out.println("Now rating at top of loop; " +nowRating);

		while (oldRating == nowRating) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

			// System.out.println("\nStarting on top! Date is now: "
			// +prosessedDate);
			if (!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
				// System.out.println("It is a weekday!");
				// System.out.println("Old rating at top of loop; " +oldRating);
				// System.out.println("Now rating at top of loop; " +nowRating);
				// System.out.println("Evaluating again at " +prosessedDate + "
				// to get new oldRating!");
				oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, prosessedDate);
				// System.out.println("Old rating after evaluating at "
				// +prosessedDate
				// +" : " +oldRating + " !");
			}
			if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
				// System.out.println("There was a weekend!");
			}

			if (oldRating == nowRating) {
				// System.out.println("Subtracting one day!");
				cal.add(Calendar.DAY_OF_MONTH, -1);
				prosessedDate = format.format(cal.getTime());
				// System.out.println("New date is : " +prosessedDate);
			}
		}
		if (oldRating != nowRating) {
			cal.add(Calendar.DAY_OF_MONTH, +1);
			prosessedDate = format.format(cal.getTime());
		}
		return prosessedDate;
	}
	
	/**
	 * Method for finding last change of rating from a given date. E.g the
	 * rating was 4 at 20150215. When did it change to that? Compares last price
	 * to weights moving average (WMA).
	 * 
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL etc.
	 * @param weight1
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight2
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight3
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @return Date the rating changed to its current value
	 */
	public String getPreviousRatingDate(String ticker, int weight1, int weight2, int weight3, String date) {

		// Algorithm: string -> date -> cal -> cal sdf = argument for
		// givenRating()
		Date currentDate = new Date();
		Calendar cal = new GregorianCalendar();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			currentDate = format.parse(date);
		} catch (ParseException e) {
			System.out.println("This date could not be parsed!");
			e.printStackTrace();
		}

		String changeDate = new String(); // The date of change
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		if ((dayOfWeek == Calendar.SUNDAY)) {
			cal.add(Calendar.DAY_OF_MONTH, -2);
		}
		if ((dayOfWeek == Calendar.SATURDAY)) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		changeDate = format.format(cal.getTime()); // Setting date of change to
		// currentDate-1 (The day
		// before).

		int nowRating = evaluateAtDate(ticker, weight1, weight2, weight3, date); // Find
		// rating
		// at
		// currentDate
		int oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);

		if (nowRating == 0) {
			return "0";
		} // If rating at today's date is zero, we just give up and return null.

		while (oldRating == nowRating) { // While the ratings are equal, we keep
			// subtracting one day from
			// changeDate.

			cal.add(Calendar.DAY_OF_MONTH, -1);
			changeDate = format.format(cal.getTime());
			oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);

			if (oldRating == 0) {
				try {
					while (oldRating == 0) {
						cal.add(Calendar.DAY_OF_MONTH, -1);
						changeDate = format.format(cal.getTime());
						oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);
					}
				} catch (Exception e) {
					return "0";
				}
			}
		}
		if (oldRating != nowRating) { // If we are out of the loop and the
			// ratings are different, add one day.
			// It changed the day after.
			dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			
		}

		return changeDate;
	}
	
	/**
	 * Method for finding last change of rating from a given date. E.g the
	 * rating was 4 at 20150215. When did it change to that? Compares last price
	 * to weights moving average (WMA).
	 * 
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL etc.
	 * @param weight1
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight2
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight3
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @return Date the rating changed to its current value
	 */
	public int getPreviousRating(String ticker, int weight1, int weight2, int weight3, String date) {

		// Algorithm: string -> date -> cal -> cal sdf = argument for
		// givenRating()
		Date currentDate = new Date();
		Calendar cal = new GregorianCalendar();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			currentDate = format.parse(date);
		} catch (ParseException e) {
			System.out.println("This date could not be parsed!");
			e.printStackTrace();
		}

		String changeDate = new String(); // The date of change
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		if ((dayOfWeek == Calendar.SUNDAY)) {
			cal.add(Calendar.DAY_OF_MONTH, -2);
		}
		if ((dayOfWeek == Calendar.SATURDAY)) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		changeDate = format.format(cal.getTime()); // Setting date of change to
		// currentDate-1 (The day
		// before).

		int nowRating = evaluateAtDate(ticker, weight1, weight2, weight3, date); // Find
		// rating
		// at
		// currentDate
		int oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);

		if (nowRating == 0) {
			return 0;
		} // If rating at today's date is zero, we just give up and return null.

		while (oldRating == nowRating) { // While the ratings are equal, we keep
			// subtracting one day from
			// changeDate.

			cal.add(Calendar.DAY_OF_MONTH, -1);
			changeDate = format.format(cal.getTime());
			oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);

			if (oldRating == 0) {
				try {
					while (oldRating == 0) {
						cal.add(Calendar.DAY_OF_MONTH, -1);
						changeDate = format.format(cal.getTime());
						oldRating = evaluateAtDate(ticker, weight1, weight2, weight3, changeDate);
					}
				} catch (Exception e) {
					return 0;
				}
			}
		}
		if (oldRating != nowRating) { // If we are out of the loop and the
			// ratings are different, add one day.
			// It changed the day after.
			dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);			
		}
		return oldRating;
	}

	public int getWeight1() {
		return weight1;
	}

	public void setWeight1(int weight1) {
		this.weight1 = weight1;
	}

	public int getWeight2() {
		return weight2;
	}

	public void setWeight2(int weight2) {
		this.weight2 = weight2;
	}

	public int getWeight3() {
		return weight3;
	}

	public void setWeight3(int weight3) {
		this.weight3 = weight3;
	}

	public void clearEasyRatings() {
		easyRatings11.clear();
		easyRatings12.clear();
		easyRatings13.clear();
		easyRatings14.clear();
		easyRatings15.clear();
		easyRatings21.clear();
		easyRatings22.clear();
		easyRatings23.clear();
		easyRatings24.clear();
		easyRatings25.clear();
	}

	public void clearRatings() {
		ratings11.clear();
		ratings12.clear();
		ratings13.clear();
		ratings14.clear();
		ratings15.clear();
		ratings21.clear();
		ratings22.clear();
		ratings23.clear();
		ratings24.clear();
		ratings25.clear();
	}

	/**
	 * Method for generating a BUY or SELL recommendation for a given stock.
	 * Uses last date the stock was traded. Compares last price to weights moving average
	 * (WMA).
	 * 
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL etc.
	 * @param weight1
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight2
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @param weight3
	 *            Weight with values typical 5 , 10, 20, 100...
	 * @return Rating 11 to 15 and 21 to 25
	 */
	public int evaluate(String ticker, int weight1, int weight2, int weight3) {

		double myWeight1 = getWeightedMovingAverage(ticker, weight1);
		double myWeight2 = getWeightedMovingAverage(ticker, weight2);
		double myWeight3 = getWeightedMovingAverage(ticker, weight3);
		int rating = 0;

		String fileName = "Tickers/" + ticker + ".txt";
		double lastPrice = 0;
		ArrayList<Double> elements = new ArrayList<>();
		ArrayList<String> dtgs = new ArrayList<>();
		String dtg;
		String output;
		String easyOutput;

		// Finding last price of stock

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // skipping first line because it contains text only
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] stockTable = line.split(dataSplitBy);

				// Putting last price in temp
				lastPrice = Double.parseDouble(stockTable[6]);
				dtg = stockTable[0];

				// Adding last price to elements
				// Today will be day [0], yesterday [1]..
				elements.add(lastPrice);
				dtgs.add(dtg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Saving firstEncounteredDate so it can be used later in finding date
		// of change
		firstEncounteredDate = dtgs.get(0);

		// We now have weight1, weight2, weight3 and last closing price...
		// Will use these to create different scenarios...

		if (elements.get(0) > myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 11;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings11.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings11.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 < myWeight2 && myWeight2 > myWeight3) {
			rating = 12;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings12.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings12.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 13;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings13.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings13.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 < myWeight2 && myWeight2 > myWeight3) {
			rating = 14;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings14.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings14.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 15;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings15.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings15.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 > myWeight2 && myWeight2 < myWeight3) {
			rating = 21;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings21.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings21.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && elements.get(0) > myWeight2 && myWeight2 < myWeight3) {
			rating = 22;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings22.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings22.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && elements.get(0) < myWeight2 && myWeight2 < myWeight3) {
			rating = 23;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings23.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings23.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 < myWeight2 && myWeight2 < myWeight3) {
			rating = 24;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings24.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings24.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 < myWeight3) {
			rating = 25;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings25.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings25.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 < myWeight2 && myWeight2 < myWeight3) {
			rating = 25;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings25.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings25.add(easyOutput);
			return rating;
		}
		return rating;
	}

	/**
	 * Method for generating a rating for a given stock at a given date!.
	 * Compares last price to weights moving averages.
	 * 
	 * @return Rating 1 to 5
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL..
	 * @param weight1
	 *            Days to calculate over. Typical 10
	 * @param weight2
	 *            Days to calculate over. Typical 20
	 * @param weight3
	 *            Days to calculate over. Typical 100
	 * @param date
	 *            The date (a string) in format 20150514 YYYYmmDD
	 * @return Rating 1 to 5 and a string is sent to functions printRatingList
	 */
	public int evaluateAtDate(String ticker, int weight1, int weight2, int weight3, String date) {

		double myWeight1 = getWeightedMovingAverage(ticker, weight1, date);
		double myWeight2 = getWeightedMovingAverage(ticker, weight2, date);
		double myWeight3 = getWeightedMovingAverage(ticker, weight3, date);
		int rating = 0;
		double lastPrice = 0;
		ArrayList<Double> elements = new ArrayList<>();
		ArrayList<String> dtgs = new ArrayList<>();
		String output;
		String easyOutput;

		// Finding last price of stock
		lastPrice = getLastPrice(ticker, date);
		elements.add(lastPrice);
		dtgs.add(date);

		// We now have weight1, weight2, weight3 and last closing price...
		// Will use these to create different scenarios...

		if (elements.get(0) == 0) {
			rating = 0;
			return rating;
		}

		if (elements.get(0) > myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 11;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings11.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings11.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 < myWeight2 && myWeight2 > myWeight3) {
			rating = 12;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings12.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings12.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 13;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings13.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings13.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 < myWeight2 && myWeight2 > myWeight3) {
			rating = 14;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings14.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings14.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 15;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings15.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings15.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 > myWeight2 && myWeight2 < myWeight3) {
			rating = 21;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings21.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings21.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && elements.get(0) > myWeight2 && myWeight2 < myWeight3) {
			rating = 22;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings22.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings22.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && elements.get(0) < myWeight2 && myWeight2 < myWeight3) {
			rating = 23;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings23.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings23.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 < myWeight2 && myWeight2 < myWeight3) {
			rating = 24;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings24.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings24.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 < myWeight3) {
			rating = 25;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings25.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings25.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 < myWeight2 && myWeight2 < myWeight3) {
			rating = 25;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings25.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings25.add(easyOutput);
			return rating;
		}
		return rating;
	}

	/**
	 * Method for generating a rating for a given stock LIVE!. Compares LIVE
	 * price to weights moving averages.
	 * 
	 * @return Rating 1 to 5
	 * @param ticker
	 *            The stock's ticker. Typical NAS, STL..
	 * @param weight1
	 *            Days to calculate over. Typical 10
	 * @param weight2
	 *            Days to calculate over. Typical 20
	 * @param weight3
	 *            Days to calculate over. Typical 100
	 * @param date
	 *            The date (a string) in format 20150514 YYYYmmDD
	 * @return Rating 1 to 5 and a string is sent to functions printRatingList
	 */
	public int evaluateAtDateLive(String ticker, int weight1, int weight2, int weight3, String date) {

		double myWeight1 = getWeightedMovingAverageLive(ticker, weight1, date);
		double myWeight2 = getWeightedMovingAverageLive(ticker, weight2, date);
		double myWeight3 = getWeightedMovingAverageLive(ticker, weight3, date);
		int rating = 0;
		double lastPrice = 0;
		ArrayList<Double> elements = new ArrayList<>();
		ArrayList<String> dtgs = new ArrayList<>();
		String output;
		String easyOutput;

		// Finding last price of stock
		lastPrice = getLastPriceLive(ticker);
		elements.add(lastPrice);
		dtgs.add(date);

		// We now have weight1, weight2, weight3 and live Price...
		// Will use these to create different scenarios...

		if (elements.get(0) == 0) {
			rating = 0;
			return rating;
		}

		if (elements.get(0) > myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 11;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings11.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings11.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 < myWeight2 && myWeight2 > myWeight3) {
			rating = 12;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings12.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings12.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 13;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings13.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings13.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 < myWeight2 && myWeight2 > myWeight3) {
			rating = 14;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings14.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings14.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 > myWeight3) {
			rating = 15;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings15.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings15.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 > myWeight2 && myWeight2 < myWeight3) {
			rating = 21;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings21.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings21.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && elements.get(0) > myWeight2 && myWeight2 < myWeight3) {
			rating = 22;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings22.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings22.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && elements.get(0) < myWeight2 && myWeight2 < myWeight3) {
			rating = 23;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings23.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings23.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 < myWeight2 && myWeight2 < myWeight3) {
			rating = 24;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings24.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings24.add(easyOutput);
			return rating;
		}
		if (elements.get(0) < myWeight1 && myWeight1 > myWeight2 && myWeight2 < myWeight3) {
			rating = 25;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings25.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings25.add(easyOutput);
			return rating;
		}
		if (elements.get(0) > myWeight1 && myWeight1 < myWeight2 && myWeight2 < myWeight3) {
			rating = 25;
			output = ("\nHere is today's situation in " + ticker + "!\n" + ticker + " has rating " + rating
					+ "\nLast price: " + elements.get(0) + " (at " + dtgs.get(0) + ")"
					+ "\nWeighted moving average last " + weight1 + " days: " + myWeight1
					+ "\nWeighted moving average last " + weight2 + " days: " + myWeight2
					+ "\nWeighted moving average last " + weight3 + " days: " + myWeight3);
			ratings25.add(output);
			easyOutput = ("\n" + dtgs.get(0) + " - " + ticker + "    \t " + elements.get(0) + "\t - Rating " + rating);
			easyRatings25.add(easyOutput);
			return rating;
		}
		return rating;
	}

	/**
	 * Method for printing rating list with date of change of rating.
	 * 
	 * @throws ParseException
	 */

	public void printAdvancedRatingList(String ticker, int weight1, int weight2, int weight3) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();

		try {
			Date date = sdf.parse(sdf.format(cal.getTime()));
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String easyDate = sdf.format(cal.getTime());
		double price = getLastPrice(ticker, easyDate);
		String changeDate = getRatingChangeDate(ticker, weight1, weight2, weight3);
		int rating = evaluate(ticker, weight1, weight2, weight3);

		System.out.print("\nDTG\tTICKER\tLAST PRICE\tRATING\tRATE DATE\t");
		if (changeDate.equals(easyDate)) {
			if (rating == 11 || rating == 12) {
				System.out.print("NEW POSITIVE TREND BUY SIGNAL!");
			}
			if (rating == 13 || rating == 14) {
				System.out.print("NEW POSITIVE TREND SELL SIGNAL!");
			}
			if (rating == 15) {
				System.out.print("NEW POSITIVE TREND KEEP SIGNAL!");
			}
			if (rating == 21) {
				System.out.print("NEW NEGATIVE TREND BUY SIGNAL!");
			}
			if (rating == 22 || rating == 23 || rating == 24 || rating == 25) {
				System.out.print("NEW NEGATIVE TREND SELL SIGNAL!");
			}
		}
		System.out.println();
		System.out.print(easyDate);
		System.out.print("\t");
		System.out.print(ticker);
		System.out.print("\t");
		if (price == 0) {
			System.out.print("No data found!\t");
		} else {
			System.out.print(price + "\t");
		}
		// System.out.print("\t\t\t");
		System.out.print(rating);
		System.out.print("\t");
		System.out.print(changeDate);
		System.out.println("\n");
	}

	/**
	 * Method for printing the easyRating list.
	 */

	public void printEasyRatingList() {
		System.out.print("\n  DTG\t  -TICKER     LAST PRICE  -RATING");

		for (int i = 0; i < easyRatings11.size(); i++) {
			System.out.print(easyRatings11.get(i));
		}
		for (int i = 0; i < easyRatings12.size(); i++) {
			System.out.print(easyRatings12.get(i));
		}
		for (int i = 0; i < easyRatings13.size(); i++) {
			System.out.print(easyRatings13.get(i));
		}
		for (int i = 0; i < easyRatings14.size(); i++) {
			System.out.print(easyRatings14.get(i));
		}
		for (int i = 0; i < easyRatings15.size(); i++) {
			System.out.print(easyRatings15.get(i));
		}
		for (int i = 0; i < easyRatings21.size(); i++) {
			System.out.print(easyRatings21.get(i));
		}
		for (int i = 0; i < easyRatings22.size(); i++) {
			System.out.print(easyRatings22.get(i));
		}
		for (int i = 0; i < easyRatings23.size(); i++) {
			System.out.print(easyRatings23.get(i));
		}
		for (int i = 0; i < easyRatings24.size(); i++) {
			System.out.print(easyRatings24.get(i));
		}
		for (int i = 0; i < easyRatings25.size(); i++) {
			System.out.print(easyRatings25.get(i));
		}

	}// end printEasyRatingList()

	public void printRatingList() {
		for (int i = 0; i < ratings11.size(); i++) {
			System.out.println(ratings11.get(i));
		}
		for (int i = 0; i < ratings12.size(); i++) {
			System.out.println(ratings12.get(i));
		}
		for (int i = 0; i < ratings13.size(); i++) {
			System.out.println(ratings13.get(i));
		}
		for (int i = 0; i < ratings14.size(); i++) {
			System.out.println(ratings14.get(i));
		}
		for (int i = 0; i < ratings15.size(); i++) {
			System.out.println(ratings15.get(i));
		}
		for (int i = 0; i < ratings21.size(); i++) {
			System.out.println(ratings21.get(i));
		}
		for (int i = 0; i < ratings22.size(); i++) {
			System.out.println(ratings22.get(i));
		}
		for (int i = 0; i < ratings23.size(); i++) {
			System.out.println(ratings23.get(i));
		}
		for (int i = 0; i < ratings24.size(); i++) {
			System.out.println(ratings24.get(i));
		}
		for (int i = 0; i < ratings25.size(); i++) {
			System.out.println(ratings25.get(i));
		}

	}// end printRatingList

	/**
	 * Get the last known price of a stock. Put in ticker.
	 * 
	 * @param ticker
	 *            Ticker, typical "NAS", "STL", etc
	 * @return Last known price of a stock
	 */

	public double getLastPrice(String ticker) {

		String fileName = "Tickers/" + ticker + ".txt";
		double lastPrice = 0;

		// Finding last price of stock at given date

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // skipping first line because it contains text only
			int counter = 0;
			while ((line = br.readLine()) != null && counter < 1) {
				// use comma as separator
				String[] stockTable = line.split(dataSplitBy);
				lastPrice = Double.parseDouble(stockTable[6]);
				counter++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lastPrice;

	} // End class getLastPrice

	/**
	 * Get the last price of a stock at a given date. Put in ticker and date.
	 * 
	 * @param ticker
	 *            Ticker, typical "NAS", "STL", etc
	 * @param date
	 *            Date in format YYYYmmDD
	 * @return Last price of a stock
	 */
	public double getLastPrice(String ticker, String date) {

		String fileName = "Tickers/" + ticker + ".txt";
		double lastPrice = 0;
		String dtg = date;

		// Finding last price of stock at given date

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // skipping first line because it contains text only

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] stockTable = line.split(dataSplitBy);
				if (stockTable[0].equals(dtg)) {
					lastPrice = Double.parseDouble(stockTable[6]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lastPrice;

	} // End class getLastPrice

	/**
	 * Get the weighted moving average of a stock at a given date. Put in
	 * ticker, number of days and date.
	 * 
	 * @param ticker
	 *            Ticker, typical "NAS", "STL", etc
	 * @param numberOfDays
	 *            Number of days is typical 5, 10, 20, 100, 200...
	 * @param date
	 *            Date in format 20151201 YYYYmmDD
	 * @return WMA
	 */
	public double getWeightedMovingAverage(String ticker, int numberOfDays, String date) {

		String fileName = "Tickers/" + ticker + ".txt";
		String dtg = date;
		String findDate = "";
		String[] stockTable = null;
		ArrayList<Double> elements = new ArrayList<>();
		ArrayList<String> dtgs = new ArrayList<String>();
		double weighted = 0;
		double temp = 0;
		double[] sum = new double[numberOfDays];
		double sumOfDays = 0;
		boolean match = false;
		int counter = 0;
		int lineShifts = 0;

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // JUMPING 1 LINES!!! MUST ONLY BE 1!

			while ((line = br.readLine()) != null && (!match)) {
				stockTable = line.split(dataSplitBy); // Use comma as separator
				findDate = stockTable[0];
				match = (findDate.equals(date));
				lineShifts++;
			}

			if (!match) {
				return 0;
			}

			br = new BufferedReader(new FileReader(fileName));

			for (int i = 0; i < lineShifts; i++) {
				br.readLine(); // JUMPING 1 LINES!!!
			}

			while ((line = br.readLine()) != null && counter < numberOfDays) {

				// Use comma as separator
				// String[] stockTable = line.split(dataSplitBy);
				stockTable = line.split(dataSplitBy);

				// Putting last price in temp
				dtg = stockTable[0];
				temp = Double.parseDouble(stockTable[6]);

				// Adding all last prices in elements array list
				elements.add(temp);
				dtgs.add(dtg);

				counter++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (elements.size() == numberOfDays) {
			// calculating sum of days
			for (int i = numberOfDays; i > 0; i--) {
				sumOfDays = (i + sumOfDays);
			}

			// find the weighted moving average of the last X days!
			for (int i = 0; i < numberOfDays; i++) {
				sum[i] = elements.get(i) * ((numberOfDays - i) / sumOfDays);
			}
			// sum the values in the array into the variable total
			for (int i = 0; i < numberOfDays; i++) {
				weighted = weighted + sum[i];
			}
			return weighted;
		}
		weighted = 0;
		return weighted;
	} // END method getMovingAverage
	
	

	/**
	 * LIVE LIVE LIVE Get the weighted moving average of a stock at a given
	 * date. Put in ticker, number of days and date.
	 * 
	 * @param ticker
	 *            Ticker, typical "NAS", "STL", etc
	 * @param numberOfDays
	 *            Number of days is typical 5, 10, 20, 100, 200...
	 * @param date
	 *            Date in format 20151201 YYYYmmDD
	 * @return WMA
	 */
	public double getWeightedMovingAverageLive(String ticker, int numberOfDays, String date) {

		String fileName = "Tickers/" + ticker + ".txt";
		String dtg = date;
		String findDate = "";
		String[] stockTable = null;
		ArrayList<Double> elements = new ArrayList<>();
		ArrayList<String> dtgs = new ArrayList<String>();
		double weighted = 0;
		double temp = 0;
		double[] sum = new double[numberOfDays];
		double sumOfDays = 0;
		boolean match = false;
		int counter = 0;
		int lineShifts = 0;

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // JUMPING 1 LINES!!! MUST ONLY BE 1!

			while ((line = br.readLine()) != null && (!match)) {
				stockTable = line.split(dataSplitBy); // Use comma as separator
				findDate = stockTable[0];
				match = (findDate.equals(date));
				lineShifts++;
			}

			if (!match) {
				return 0;
			}

			br = new BufferedReader(new FileReader(fileName));

			for (int i = 0; i < lineShifts; i++) {
				br.readLine(); // JUMPING 1 LINES!!!
			}

			while ((line = br.readLine()) != null && counter < numberOfDays) {

				// Use comma as separator
				// String[] stockTable = line.split(dataSplitBy);
				stockTable = line.split(dataSplitBy);

				// Putting last price in temp
				dtg = stockTable[0];
				temp = Double.parseDouble(stockTable[6]);

				// Adding all last prices in elements array list
				elements.add(temp);
				dtgs.add(dtg);

				counter++;

				if (counter == (numberOfDays)) {
					temp = getLastPriceLive(ticker);
					elements.add(0, temp);
				}
			}
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (elements.size() == numberOfDays) {
			// calculating sum of days
			for (int i = numberOfDays; i > 0; i--) {
				sumOfDays = (i + sumOfDays);
			}

			// find the weighted moving average of the last X days!
			for (int i = 0; i < numberOfDays; i++) {
				sum[i] = elements.get(i) * ((numberOfDays - i) / sumOfDays);
			}
			// sum the values in the array into the variable total
			for (int i = 0; i < numberOfDays; i++) {
				weighted = weighted + sum[i];
			}
			return weighted;
		}
		weighted = 0;
		return weighted;
	} // END method getMovingAverage

	/**
	 * Get the weighted moving average of a stock from last entry in file,
	 * typical yesterday.
	 * 
	 * @param ticker
	 *            Stockticker from OSE, typical ANS, DNB etc
	 * @param numberOfDays
	 *            Number of days the average is calculated back in time. Typical
	 *            numbers are 10, 20, 100...
	 * @return Weighted Moving Average. If there is not enough days to
	 *         calculate, return 0.
	 */

	public double getWeightedMovingAverage(String ticker, int numberOfDays) {

		String fileName = "Tickers/" + ticker + ".txt";
		String dtg;

		ArrayList<Double> elements = new ArrayList<>();
		ArrayList<String> dtgs = new ArrayList<String>();
		double weighted = 0;
		double temp = 0;
		double[] sum = new double[numberOfDays];
		double sumOfDays = 0;
		String[] stockTable = null;

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // JUMPING 1 LINES!!! MUST ONLY BE 1!
			int counter = 0;
			while ((line = br.readLine()) != null && counter < numberOfDays) {

				// Use comma as separator
				// String[] stockTable = line.split(dataSplitBy);
				stockTable = line.split(dataSplitBy);

				// Putting last price in temp
				dtg = stockTable[0];
				temp = Double.parseDouble(stockTable[6]);

				// Adding all last prices in elements array list
				elements.add(temp);
				dtgs.add(dtg);

				counter++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (elements.size() == numberOfDays) {
			// calculating sum of days
			for (int i = numberOfDays; i > 0; i--) {
				sumOfDays = (i + sumOfDays);
			}

			// find the weighted moving average of the last X days!
			for (int i = 0; i < numberOfDays; i++) {
				sum[i] = elements.get(i) * ((numberOfDays - i) / sumOfDays);
				// System.out.println("Kurs " +dtgs.get(i) + " : "
				// +elements.get(i));
			}
			// sum the values in the array into the variable total
			for (int i = 0; i < numberOfDays; i++) {
				weighted = weighted + sum[i];
			}
			return weighted;
		}
		weighted = 0;
		return weighted;

	} // END method getMovingAverage

	public String getRecommendation(int rating) {

		if (rating == 11) {
			return "Buy";
		}
		if (rating == 12) {
			return "Buy";
		}
		if (rating == 13) {
			return "Stay";
		}
		if (rating == 14) {
			return "Sell";
		}
		if (rating == 15) {
			return "Stay";
		}
		if (rating == 21) {
			return "Buy";
		}
		if (rating == 22 || rating == 23 || rating == 24 || rating == 25) {
			return "Sell";
		}
		return null;
	}
	
	/**
	 * Get the RSI of a stock at a given date. Put in
	 * ticker, number of days and date.
	 * 
	 * THIS IS NOT WORKING PROPERLY!!! The output is not as expected!
	 * 
	 * @param ticker
	 *            Ticker, typical "NAS", "STL", etc
	 * @param numberOfDays
	 *            Number of days is typically 14
	 * @param date
	 *            Date in format 20151201 YYYYmmDD
	 * @return RSI smoothed
	 */
	public double calculateRSI(Ticker ticker, int numberOfDays, Date date) {
		List<Entry> entries = new ArrayList<Entry>();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		
		String fileName = "Tickers/" + ticker + ".txt";
		String[] stockTable = null;
		
		boolean match = false;
		int counter = 0;
		int lineShifts = 0;

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // JUMPING 1 LINES!!! MUST ONLY BE 1!

			while ((line = br.readLine()) != null && (!match)) {
				stockTable = line.split(dataSplitBy); // Use comma as separator
				Date findDate = format.parse(stockTable[0]);
				match = date.equals(findDate);
				lineShifts++;
			}

			if (!match) {
				return 0;
			}

			br = new BufferedReader(new FileReader(fileName));

			for (int i = 0; i < lineShifts; i++) {
				br.readLine(); // JUMPING 1 LINES!!!
			}

			while ((line = br.readLine()) != null && counter < 100+1) {

				// Use comma as separator
				stockTable = line.split(dataSplitBy);
				entries.add(new Entry(ticker, format.parse(stockTable[0]), Double.parseDouble(stockTable[6])));
				counter++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch ( ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Collections.reverse(entries);
		
		Average algo = new ExponentialMovingAverage(entries, numberOfDays);
		Rsi rsi = new Rsi(algo);
		
		return rsi.calculate(date);
	}
	
	/**
	 * Get the change of a stock at a given date. Put in ticker and date.
	 * 
	 * @param ticker
	 *            Ticker, typical "NAS", "STL", etc
	 * @param date
	 *            Date in format YYYYmmDD
	 * @return Change in percent
	 */
	public double getChangeAtDate(String ticker, String date) {

		String fileName = "Tickers/" + ticker + ".txt";
		double lastPrice = 0;
		double prevPrice = 0;
		double change = 0;
		String dtg = date;

		// Finding last price of stock at given date

		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine(); // skipping first line because it contains text only

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] stockTable = line.split(dataSplitBy);
				if (stockTable[0].equals(dtg)) {
					lastPrice = Double.parseDouble(stockTable[6]);	
					line = br.readLine();
					String[] stockTable2 = line.split(dataSplitBy);
					prevPrice = Double.parseDouble(stockTable2[6]);		
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("Last price: "+lastPrice);
		//System.out.println("Previous price: "+prevPrice);
		change = (lastPrice-prevPrice)/prevPrice;
		double percentage = change*100;
		return percentage;

	} // End class getLastPrice
	
	

}// end class DataManager