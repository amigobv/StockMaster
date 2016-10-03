package core;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * @author KurtOddvar <br>
 *         Class that has methods for:<br>
 *         - Creating a file on disk <br>
 *         - Creating a file on disk with chosen name from constructor's
 *         argument<br>
 *         - Write predetermined data to default file ("newFile")<br>
 *         - Write predetermined data to chosen file<br>
 *         - Delete content of a file<br>
 *         - Download a default file<br>
 *         - Download a chosen file<br>
 *         TO DO<br>
 * 
 *         - Download a chosen file with ticker as only argument<br>
 */

public class FileManager {
	String dataSplitBy = ",";
	int counter = 0;
	BufferedReader br = null;
	String line = "";
	String fileName;
	File file;
	long time;
	Calendar changeDate = Calendar.getInstance();
	Calendar todaysDate = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("HH");

	/**
	 * Method for creating a default file on the hard drive. A folder named
	 * Tickers will be created. Default file is "newFile.txt". Path for this
	 * file will be YourProjectName\MyFolder\fileName.txt This is a test-method
	 * for testing of class functionality.
	 */

	public static void createFile() {
		// Create folder "myFolder"
		File file = new File("MyFolder");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		File newFile = new File("MyFolder/newFile.txt");

		if (newFile.exists()) {
			System.out.println("File " + newFile.getName() + " already exists!");
			System.out.println(newFile.getAbsolutePath() + "\n");
		}

		else {
			try {
				newFile.createNewFile();
				System.out.println("File " + newFile.getName() + " has been created!");
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(newFile.getAbsolutePath());
		}
	}

	/**
	 * Method for creating a file on the hard drive with chosen name and in
	 * chosen folder. If the file already exists, it will not be created.
	 * 
	 * @param fileName
	 *            Filename of your choice e.g "fileName.txt"
	 * @param folderName
	 *            Folders name e.g "MyFolder". <br>
	 *            Path is : YourProjectName\MyFolder\fileName.txt
	 */
	public static void createFile(String folderName, String fileName) {

		// Creating the folder
		File file = new File(folderName);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		// Creating the file
		File newFile = new File(folderName + "/" + fileName);
		if (newFile.exists()) {
			System.out.println("File " + newFile.getName() + " already exists!");
			System.out.println(newFile.getAbsolutePath());
		} else {
			try {
				newFile.createNewFile();
				System.out.println("File " + newFile.getName() + " has been created!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(newFile.getAbsolutePath());
		}
	}

	/**
	 * Method for checking if a file is up to date.
	 * 
	 * @param folderName
	 *            Typical: "Tickers", "Files", etc
	 * @param fileName
	 *            Typical: "NAS", "DNB", "STL" etc *
	 * @return False if folder does not exist. False if file does not exist.
	 *         False if file has not been updated today. False if file has not
	 *         been updated after 17:00. True if file has been updated today
	 *         after 17:00.
	 */
	public boolean checkIfFileIsUpdated(String folderName, String fileName) {
		// Creating the file
		File myFile = new File(folderName + "/" + fileName);
		if (!myFile.exists()) {
			return false;
		}
		// Finding the time file was last modified
		int fileChangeDate = Integer.parseInt(sdf.format(myFile.lastModified()));
		int fileChangeHour = Integer.parseInt(sdf2.format(myFile.lastModified()));

		// File is not up to date:
		// IF change of file YEAR and todaysDate YEAR is different
		// OR change of file getTime() is different from todays getTime
		// OR Hour of day is after 17 and change of file is less than 17
		if ((changeDate.get(Calendar.YEAR) != todaysDate.get(Calendar.YEAR)
				|| (fileChangeDate != todaysDate.get(Calendar.DATE)
						|| (fileChangeHour < 17 && 17 <= todaysDate.get(Calendar.HOUR_OF_DAY))))) {
			System.out.println(myFile + " is not up to date.");
			return false; // Not up to date
		} else {
			return true; // Up to date
		}
	}

	/**
	 * Method that checks for the last time a file was updated.
	 * 
	 * @param fileName
	 *            The filename with *.txt. Typical NAS.txt, MHG.txt, etc.
	 * @param folderName
	 *            Typical: "Tickers", "Files", etc
	 * @return A string output telling you when the file was last updated.
	 */
	public String getWhenFileWasUpdated(String folderName, String fileName) {

		File myFile = new File(folderName + "/" + fileName);
		boolean fileExists;
		String output;
		long time = myFile.lastModified();
		changeDate.setTimeInMillis(time);

		// true if the file path is a file, else false
		fileExists = myFile.exists();

		// if path exists
		if (fileExists) {
			// returns the time file was last modified
			// date and time
			output = (changeDate.getTime().toString());
			return output;
		} else {
			output = ("File " + myFile.getAbsolutePath() + " does not exist...");
			return output;
		}
	}

	/**
	 * Method for deleting content of default file. Default file is
	 * "newFile.txt"
	 */

	public static void deleteContent() {
		System.out.println("\nDeleting content of default file... ");

		try {
			FileWriter reader = new FileWriter("newFile.txt");
			reader.flush();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for deleting content of chosen file *
	 * 
	 * @param fileName
	 *            Filename in format "myFile.txt"
	 */

	public static void deleteContent(String folderName, String fileName) {

		System.out.println("Deleting content of file " + fileName);

		try {
			FileWriter reader = new FileWriter(folderName + "/" + fileName);
			reader.flush();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for downloading and saving a file with ticker as argument.<br>
	 * Ticker will be converted to correct URL and download will start.<br>
	 * File will be saved as ticker + ".txt", in folder Tickers (folder will be created)..
	 * 
	 * @param ticker
	 *            Stock's ticker. MUST be from OSE. Typical "MHG"
	 */
	public void downloadAndSaveFile(String ticker) {

		// Create folder "Tickers", if it does not exist already.
		File file = new File("Tickers");
		if (!file.exists()) {
			if (file.mkdir()) {
				// System.out.println("Directory is created!");
			}
		}

		String downloadLinkPart1 = "http://www.netfonds.no/quotes/paperhistory.php?paper=";
		String missingPart = ticker;
		String downloadLinkPart2 = ".OSE&csv_format=csv";
		String downloadLink = downloadLinkPart1 + missingPart + downloadLinkPart2;

		try {
			String fileName = "Tickers/" + ticker + ".txt";
			// The file that will be saved on your computer

			URL link = new URL(downloadLink); // The file that you want to
			// download

			// Code to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();

			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();
			// System.out.println("\nDownload successful!");
			// End download code

		} catch (IOException e) {
			e.printStackTrace();
			// System.out.println("\nDownload failed!\n");
		}
	} // END downloadAndSaveFile
	
	/**
	 * Method for downloading and saving a "LiveFile" from OSE. These are the files that give you todays trades.
	 * File will be saved as ticker + ".txt", in folder LiveTickers (Folder gets created automatically).
	 * 
	 * @param ticker
	 *            Stock's ticker. MUST be from OSE. Typical "MHG"
	 */
	public void downloadAndSaveFileLive(String ticker) {

		// Create folder "Tickers", if it does not exist already.
		File file = new File("LiveTickers");
		if (!file.exists()) {
			if (file.mkdir()) {
				// System.out.println("Directory is created!");
			}
		}
		// http://www.netfonds.no/quotes/tradedump.php?paper=FUNCOM.OSE&csv_format=csv

		String downloadLinkPart1 = "http://www.netfonds.no/quotes/tradedump.php?paper=";
		String missingPart = ticker;
		String downloadLinkPart2 = ".OSE&csv_format=csv";
		String downloadLink = downloadLinkPart1 + missingPart + downloadLinkPart2;

		try {
			String fileName = "LiveTickers/" + ticker + ".txt";
			// The file that will be saved on your computer

			URL link = new URL(downloadLink); // The file that you want to
			// download

			// Code to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();

			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();
			// System.out.println("\nDownload successful!");
			// End download code

		} catch (IOException e) {
			e.printStackTrace();
			// System.out.println("\nDownload failed!\n");
		}
	} // END downloadAndSaveFile

	/**
	 * Method for reading data from a default file Default file is "newFile.txt"
	 */

	public void readFile() {
		System.out.println("\nTrying to read from newFile.txt...");
		try {
			FileReader reader = new FileReader("newFile.txt");
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for reading data from a chosen file. Remember to add foldername if this is a tickerFile.
	 * E.g "Tickers/NAS.txt".
	 * 
	 * @param fileName
	 *            in format "FILENAME.txt". "Tickers/NAS.txt" etc
	 */

	public void readFile(String fileName) {
		System.out.println("\nTrying to read from " + fileName + "\n");
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("\nNo file with name " + fileName + " exists :(");
			System.out.println("Did you forget to use the *.txt format?");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for reading a file into an ArrayList of Strings. This method can
	 * not split what it reads. This method is excellent for reading a list of
	 * tickers into memory, and then downloading that list with a download
	 * method. It will read line by line. There should be 1 word per line.
	 * 
	 * @param fileName
	 *            *.txt file with list of tickers to load into an arrayList
	 * @param array
	 *            The arrayList that will be loaded with tickers
	 */

	public void readFileToArray(String fileName, ArrayList<String> array) {

		String temp;
		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {

				// Putting last priceNow in temp
				temp = line.trim();

				// Adding all last prices in elements array list
				array.add(temp);
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
	} // End readFileToArray
	/**
	 * Method for reading a stock names into an ArrayList of Strings. This method reads a list with format<br>
	 * ticker, name<br>
	 * ticker, name<br>
	 * The names are loaded into arrayList
	 * @param fileName
	 *            *.txt file with list of tickers and names to load into an arrayList
	 * @param array
	 *            The arrayList that will be loaded with names
	 */
	public void readNamesToArray(String fileName, ArrayList<String> array) {

		String temp;
		
		try {
			br = new BufferedReader(new FileReader(fileName));

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] StringArray = line.split(dataSplitBy);
				temp =StringArray[1];
				array.add(temp.trim());
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

	} // End class readNamesToArray

	/**
	 * Method for writing two lines of predetermined text to a chosen file.<br>
	 * This method is just for demonstration and test purposes.
	 * 
	 * @param fileName
	 *            Filename in format "myFile.txt"
	 */
	
	/**
	 * Method for reading a stock names into an ArrayList of Strings. This method reads a list with format<br>
	 * ticker, name<br>
	 * ticker, name<br>
	 * The names are loaded into arrayList
	 * @param fileName
	 *            *.txt file with list of tickers and names to load into an arrayList
	 * @param array
	 *            The arrayList that will be loaded with names
	 */
	public void readTickersToArray(String fileName, ArrayList<String> array) {

		String temp;
		
		try {
			br = new BufferedReader(new FileReader(fileName));

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] StringArray = line.split(dataSplitBy);
				temp =StringArray[0];
				array.add(temp.trim());
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

	} // End class readNamesToArray

	/**
	 * Method for writing two lines of predetermined text to a chosen file.<br>
	 * This method is just for demonstration and test purposes.
	 * 
	 * @param fileName
	 *            Filename in format "myFile.txt"
	 */


	public void writeFile(String fileName) {

		System.out.println("\nI am in " + fileName + "newFile.txt now...");
		System.out.println("Will try to add text to file...");

		try {
			FileWriter writer = new FileWriter(fileName, true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write("Line of text in " + fileName);
			bufferedWriter.newLine();
			bufferedWriter.write("Doing it again in " + fileName);
			bufferedWriter.newLine();

			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for downloading a default file. This method is just for
	 * demonstration and test purposes.
	 * 
	 * @throws IOException
	 *             Throws IO
	 */

	public void downloadFile() throws IOException {
		System.out.println("\nTrying to download default file...");
		try {
			String fileName = "downloadFile.txt"; // The file that will be saved
			// on your computer
			URL link = new URL("_blank"); // The file that you want to download

			// Code to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();

			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();
			System.out.println("\nDownload successful!");
			// End download code

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nDownload failed!\n");
		}
	}

	/**
	 * Method for downloading a file of your choice.<br>
	 * You also choose filename.
	 * 
	 * @param name
	 *            Name of file to be created in format "myFile.txt"
	 * @param url
	 *            Link to file that will be downloaded and saved
	 * @throws IOException
	 *             Throws IO
	 */
	public void downloadFile(String name, String url) throws IOException {
		System.out.println("\nTrying to download " + name + " from " + url);
		try {
			String fileName = name; // The file that will be saved on your
			// computer
			URL link = new URL(url); // The file that you want to download

			// Code to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();

			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();
			System.out.println("\nDownload successful!");
			// End download code

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nDownload failed!\n");
		}
	}
} // End class