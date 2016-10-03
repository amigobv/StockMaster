package core;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.UIManager;
import javax.swing.JEditorPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.SwingConstants;
import java.util.Locale;
import javax.swing.JTabbedPane;
import java.awt.Font;
import java.awt.Frame;

/**
 * 
 * @author KurtOddvar Regarding ratings: RATING ARE BASED ON WMA 10 + 20 + 100 1
 *         - IS UPTREND. 1.1 IS THE BEST RATING. BUY! 1.2 BUY 1.3 STAY IN/OUT
 *         1.4 SELL 1.5 STAY IN/OUT
 *
 *         2 - IS DOWNTREND 2.1 BUY 2.2 -2-5 SELL
 */

@SuppressWarnings("unused")
public class StockMaster2000 {

	private JFrame frmStockmaster;
	private JTable table_analysis;
	private JTable table_portfolio;
	private JTable table_live;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SplashScreenMain mySplash = new SplashScreenMain();
		mySplash.showSplash();
		try {
			mySplash.setProgress("Hello", 0);
			Thread.sleep(1000);
			mySplash.setProgress("Program loading", 15);
			Thread.sleep(1000);
			mySplash.setProgress("Please wait", 35);
			Thread.sleep(1000);
			mySplash.setProgress("This may take a while", 65);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StockMaster2000 window = new StockMaster2000();
					window.frmStockmaster.setExtendedState(Frame.MAXIMIZED_BOTH);
					mySplash.hideSplash();
					window.frmStockmaster.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StockMaster2000() {

		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		ArrayList<String> tickers = new ArrayList<String>();
		FileManager myFile = new FileManager();
		DataManager myData = new DataManager();
		TimeManager myTime = new TimeManager();
		int weight1 = 10, weight2 = 20, weight3 = 100;

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setRoundingMode(RoundingMode.CEILING);

		myFile.readFileToArray("tickerList.txt", tickers);

		// Downloading and saving files belonging to list of tickers
		for (int i = 0; i < tickers.size(); i++) {
			if (!myFile.checkIfFileIsUpdated("Tickers", tickers.get(i) + ".txt")) {
				System.out.println(myFile.getWhenFileWasUpdated("Tickers", tickers.get(i) + ".txt"));
				myFile.downloadAndSaveFile(tickers.get(i));
				System.out.println("Downloading " + tickers.get(i));
			} else {
				System.out.println(tickers.get(i) + " is up to date and will not be downloaded.\n");
			}
		}

		// Downloading and saving Live-files belonging to list of tickers
		/*for (int i = 0; i < tickers.size(); i++) {
			myFile.downloadAndSaveFileLive(tickers.get(i));
			System.out.println("Downloading " + tickers.get(i) + " Live-file");
		}*/

		Object[][] analysisData = new Object[tickers.size()][13];

		/*for (int i = 0; i < tickers.size(); i++) {
			String rateChangeDate = myData.getRatingChangeDate(tickers.get(i), weight1, weight2, weight3,
					myTime.getTodaysDate());
			analysisData[i][0] = myTime.getTodaysDate();
			analysisData[i][1] = tickers.get(i);
			analysisData[i][2] = myData.getLastPrice(tickers.get(i));
			analysisData[i][3] = myData.evaluateAtDate(tickers.get(i), weight1, weight2, weight3,
					myTime.getTodaysDate());
			analysisData[i][4] = myData.getRatingChangeDate(tickers.get(i), weight1, weight2, weight3,
					myTime.getTodaysDate());
			analysisData[i][5] = myData.getLastPrice(tickers.get(i),
					myData.getRatingChangeDate(tickers.get(i), weight1, weight2, weight3, myTime.getTodaysDate()));
			analysisData[i][6] = decimalFormat
					.format(myData.getWeightedMovingAverage(tickers.get(i), weight1, myTime.getTodaysDate()));
			analysisData[i][7] = decimalFormat
					.format(myData.getWeightedMovingAverage(tickers.get(i), weight2, myTime.getTodaysDate()));
			analysisData[i][8] = decimalFormat
					.format(myData.getWeightedMovingAverage(tickers.get(i), weight3, myTime.getTodaysDate()));
			analysisData[i][9] = myData.getRecommendation(
					myData.evaluateAtDate(tickers.get(i), weight1, weight2, weight3, myTime.getTodaysDate()));
			analysisData[i][10] = decimalFormat
					.format((myData.getLastPrice(tickers.get(i), myTime.getADate())
							/ myData.getLastPrice(tickers.get(i), rateChangeDate) - 1) * 100);
			
			analysisData[i][11] = myData.getPreviousRating(tickers.get(i), weight1, weight2, weight3, myTime.getTodaysDate());
			
			analysisData[i][12] = myData.getPreviousRatingDate(tickers.get(i), weight1, weight2, weight3, myTime.getTodaysDate());
		}*/

		Object[] analysisColumnNames = { "DTG", "TICKER", "PRICE", "RATING", "DATE OF CHANGE", "PRICE AT CHANGE",
				"WMA 1", "WMA 2", "WMA 3", "RECOMMEND", "% SINCE RECOMMENDATION", "P Rating", "P R Date" };
		DefaultTableModel analysisModel = new DefaultTableModel(analysisData, analysisColumnNames);

		/////////////////////////////////////////////

		Object[][] liveData = new Object[tickers.size()][9];

		for (int i = 0; i < tickers.size(); i++) {
			liveData[i][0] = myTime.getTodaysDate();
			liveData[i][1] = tickers.get(i);
			// liveData[i][2] = myData.getLastPriceLive(tickers.get(i));
			// liveData[i][3] = myData.evaluateAtDateLive(tickers.get(i),
			// weight1, weight2, weight3, myTime.getTodaysDate());
			liveData[i][4] = myData.getRatingChangeDate(tickers.get(i), weight1, weight2, weight3,
					myTime.getTodaysDate());
			// liveData[i][5] =
			// decimalFormat.format(myData.getWeightedMovingAverageLive(tickers.get(i),
			// weight1, myTime.getTodaysDate()));
			// liveData[i][6] = decimalFormat.format(
			// myData.getWeightedMovingAverageLive(tickers.get(i), weight2,
			// myTime.getTodaysDate()));
			// liveData[i][7] =
			// decimalFormat.format(myData.getWeightedMovingAverageLive(tickers.get(i),
			// weight3, myTime.getTodaysDate()));
			liveData[i][8] = myData.getRecommendation(
					myData.evaluateAtDate(tickers.get(i), weight1, weight2, weight3, myTime.getTodaysDate()));
		}

		Object[] liveColumnNames = { "DTG", "TICKER", "PRICE", "RATING", "DATE OF CHANGE", "WMA 1", "WMA 2", "WMA 3",
				"RECOMMEND" };
		DefaultTableModel liveModel = new DefaultTableModel(liveData, liveColumnNames);

		/////////////////////////////////////////////////////////////

		frmStockmaster = new JFrame();
		frmStockmaster.setTitle("StockMaster2000");
		frmStockmaster.setBounds(100, 100, 1920, 960);
		frmStockmaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStockmaster.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 11, 1900, 860);
		frmStockmaster.getContentPane().add(tabbedPane);

		JPanel panel_welcome = new JPanel();
		tabbedPane.addTab("Welcome", null, panel_welcome, null);
		panel_welcome.setLayout(null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 11, 1860, 806);
		panel_welcome.add(scrollPane_3);
		
		JEditorPane dtrpnWelcome = new JEditorPane();
	    scrollPane_3.setViewportView(dtrpnWelcome);
	    dtrpnWelcome.setEditable(false);
	    dtrpnWelcome.setContentType("text/html");
	    File file = new File("HTML/welcome.htm");
	    
	    try
	    {
	    	dtrpnWelcome.setPage(file.toURI().toURL());
	    }
	    catch (IOException e1)
	    {
	      dtrpnWelcome.setText("<html>Could not load</html>");
	      e1.printStackTrace();
	    }

		JPanel panel_analysis = new JPanel();
		tabbedPane.addTab("Analysis", null, panel_analysis, null);
		panel_analysis.setLayout(null);

		JScrollPane scrollPane_analysis = new JScrollPane();
		scrollPane_analysis.setBounds(10, 42, 1398, 546);
		panel_analysis.add(scrollPane_analysis);
		scrollPane_analysis.setBorder(UIManager.getBorder("ScrollPane.border"));

		table_analysis = new JTable();
		scrollPane_analysis.setViewportView(table_analysis);
		table_analysis.setName("marketTable");
		table_analysis.setAutoCreateRowSorter(true);
		table_analysis.setBackground(Color.WHITE);
		table_analysis.setModel(analysisModel);
		TableColumn analysisColumn = null;
		analysisColumn = table_analysis.getColumnModel().getColumn(0); // DTG-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(1); // TICKER-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(2); // PRICE-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(3); // RATING-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(4); // CHANGE
		// DATE-column
		analysisColumn.setPreferredWidth(75);
		analysisColumn = table_analysis.getColumnModel().getColumn(5); // PRICE AT CHANGE column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(6); // WMA
		// 1-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(7); // WMA
		// 2-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(8); // WMA
		// 3-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(9); // Recommend-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(10); // Recommend-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(11); // Recommend-column
		analysisColumn.setPreferredWidth(30);
		analysisColumn = table_analysis.getColumnModel().getColumn(12); // Recommend-column
		analysisColumn.setPreferredWidth(30);

		table_analysis.setAutoCreateColumnsFromModel(false);

		JTextPane txtpnInformationGoesHereAnalysis = new JTextPane();
		txtpnInformationGoesHereAnalysis.setBounds(1420, 257, 166, 166);
		panel_analysis.add(txtpnInformationGoesHereAnalysis);
		txtpnInformationGoesHereAnalysis.setText("Information goes here");

		JSpinner spinner_day_analysis = new JSpinner();
		spinner_day_analysis.setBounds(1524, 42, 62, 20);
		spinner_day_analysis.setModel(new SpinnerNumberModel(myTime.getADateDay(), 1, 31, 1));
		panel_analysis.add(spinner_day_analysis);

		JLabel lblSelectDateAnalysis = new JLabel("Select date");
		lblSelectDateAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectDateAnalysis.setBounds(1420, 42, 94, 20);
		panel_analysis.add(lblSelectDateAnalysis);

		JButton btnUpdateAnalysis = new JButton("Update table");
		btnUpdateAnalysis.setBounds(1420, 223, 166, 23);
		panel_analysis.add(btnUpdateAnalysis);

		JLabel lblSelectMonthAnalysis = new JLabel("Select month");
		lblSelectMonthAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectMonthAnalysis.setBounds(1420, 72, 94, 20);
		panel_analysis.add(lblSelectMonthAnalysis);

		JSpinner spinner_month_analysis = new JSpinner();
		spinner_month_analysis.setBounds(1524, 72, 62, 20);
		panel_analysis.add(spinner_month_analysis);
		spinner_month_analysis.setLocale(new Locale("no"));
		spinner_month_analysis.setModel(new SpinnerNumberModel(myTime.getADateMonth() + 1, 1, 12, 1));

		JSpinner spinner_year_analysis = new JSpinner();
		spinner_year_analysis.setBounds(1524, 102, 62, 20);
		panel_analysis.add(spinner_year_analysis);
		spinner_year_analysis.setModel(new SpinnerNumberModel(myTime.getADateYear(), 1999, 2016, 1));

		JLabel lblSelectYearAnalysis = new JLabel("Select year");
		lblSelectYearAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectYearAnalysis.setBounds(1420, 102, 94, 20);
		panel_analysis.add(lblSelectYearAnalysis);

		JLabel lblAnalysis = new JLabel("Analysis");
		lblAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblAnalysis.setForeground(Color.BLACK);
		lblAnalysis.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAnalysis.setBounds(10, 11, 135, 20);
		panel_analysis.add(lblAnalysis);

		JEditorPane dtrpnRatingsExplanationAnalysis = new JEditorPane();
		dtrpnRatingsExplanationAnalysis.setText(
				"RATINGS EXPLANATION\r\n\r\n1x is upward trend\r\n2x is downward trend\r\n\r\nx1 is confirmed buy-signal\r\nx4 is confirmed sell-signal\r\n\r\n11 - Best possible rating - BUY\r\n12 - BUY\r\n13 - STAY IN / OUT\r\n14 - SELL\r\n15 - STAY IN / OUT\r\n\r\n21 - BUY\r\n22 - SELL\r\n23 - SELL\r\n24 - SELL\r\n25 - SELL");
		dtrpnRatingsExplanationAnalysis.setBounds(1598, 42, 285, 565);
		panel_analysis.add(dtrpnRatingsExplanationAnalysis);

		JLabel lblWMA1_analysis = new JLabel("WMA 1");
		lblWMA1_analysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblWMA1_analysis.setBounds(1420, 132, 94, 20);
		panel_analysis.add(lblWMA1_analysis);

		JLabel lblWMA2_analysis = new JLabel("WMA 2");
		lblWMA2_analysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblWMA2_analysis.setBounds(1420, 162, 94, 20);
		panel_analysis.add(lblWMA2_analysis);

		JLabel lblWMA3_analysis = new JLabel("WMA 3");
		lblWMA3_analysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblWMA3_analysis.setBounds(1420, 192, 94, 20);
		panel_analysis.add(lblWMA3_analysis);

		JSpinner spinner_WMA_1_analysis = new JSpinner();
		spinner_WMA_1_analysis.setModel(new SpinnerNumberModel(10, 0, 200, 1));
		spinner_WMA_1_analysis.setBounds(1524, 132, 62, 20);
		panel_analysis.add(spinner_WMA_1_analysis);

		JSpinner spinner_WMA_2_analysis = new JSpinner();
		spinner_WMA_2_analysis.setModel(new SpinnerNumberModel(20, 0, 200, 1));
		spinner_WMA_2_analysis.setLocale(new Locale("no"));
		spinner_WMA_2_analysis.setBounds(1524, 162, 62, 20);
		panel_analysis.add(spinner_WMA_2_analysis);

		JSpinner spinner_WMA_3_analysis = new JSpinner();
		spinner_WMA_3_analysis.setModel(new SpinnerNumberModel(100, 0, 200, 1));
		spinner_WMA_3_analysis.setBounds(1524, 192, 62, 20);
		panel_analysis.add(spinner_WMA_3_analysis);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 634, 1398, 183);
		panel_analysis.add(scrollPane_2);

		Object[][] portfolioData = new Object[tickers.size()][11];

		String rateChangeDate = myData.getRatingChangeDate(tickers.get(0), weight1, weight2, weight3,
				myTime.getTodaysDate());

		/*portfolioData[0][0] = myTime.getTodaysDate();
		portfolioData[0][1] = tickers.get(0);
		portfolioData[0][2] = myData.getLastPrice(tickers.get(0));
		portfolioData[0][3] = myData.evaluateAtDate(tickers.get(0), weight1, weight2, weight3, myTime.getTodaysDate());
		portfolioData[0][4] = myData.getRatingChangeDate(tickers.get(0), weight1, weight2, weight3,
				myTime.getTodaysDate());
		portfolioData[0][5] = myData.getLastPrice(tickers.get(0),
				myData.getRatingChangeDate(tickers.get(0), weight1, weight2, weight3, myTime.getTodaysDate()));
		portfolioData[0][6] = decimalFormat
				.format(myData.getWeightedMovingAverage(tickers.get(0), weight1, myTime.getTodaysDate()));
		portfolioData[0][7] = decimalFormat
				.format(myData.getWeightedMovingAverage(tickers.get(0), weight2, myTime.getTodaysDate()));
		portfolioData[0][8] = decimalFormat
				.format(myData.getWeightedMovingAverage(tickers.get(0), weight3, myTime.getTodaysDate()));
		portfolioData[0][9] = myData.getRecommendation(
				myData.evaluateAtDate(tickers.get(0), weight1, weight2, weight3, myTime.getTodaysDate()));
		portfolioData[0][10] = decimalFormat.format((myData.getLastPrice(tickers.get(0), myTime.getTodaysDate())
				/ myData.getLastPrice(tickers.get(0), rateChangeDate) - 1) * 100);*/

		Object[] portfolioColumnNames = { "THIS", "IS", "ONLY", "A", "DEMO", "PRICE AT CHANGE",
				"WMA 1", "WMA 2", "WMA 3", "RECOMMEND", "% SINCE RECOMMENDATION" };
		DefaultTableModel portfolioModel = new DefaultTableModel(portfolioData, portfolioColumnNames);

		/////////////////////////////////////////////////
		table_portfolio = new JTable();
		scrollPane_2.setViewportView(table_portfolio);
		table_portfolio.setName("portfolioTable");
		table_portfolio.setAutoCreateRowSorter(true);
		table_portfolio.setBackground(Color.WHITE);
		table_portfolio.setModel(portfolioModel);
		TableColumn portfolioColumn = null;
		portfolioColumn = table_portfolio.getColumnModel().getColumn(0); // DTG-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(1); // TICKER-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(2); // PRICE-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(3); // RATING-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(4); // CHANGE
		// DATE-column
		portfolioColumn.setPreferredWidth(75);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(5); // PRICE
		// - AT
		// -
		// CHANGE
		// -
		// column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(6); // WMA
		// 1-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(7); // WMA
		// 2-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(8); // WMA
		// 3-column
		portfolioColumn.setPreferredWidth(30);
		portfolioColumn = table_portfolio.getColumnModel().getColumn(9); // Recommend-column
		portfolioColumn.setPreferredWidth(30);

		table_portfolio.setAutoCreateColumnsFromModel(false);
		
		JLabel lblPortfolio = new JLabel("Portfolio");
		lblPortfolio.setHorizontalAlignment(SwingConstants.LEFT);
		lblPortfolio.setForeground(Color.BLACK);
		lblPortfolio.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPortfolio.setBounds(10, 601, 135, 20);
		panel_analysis.add(lblPortfolio);
		//////////////////////////////////////////////////

		// UPDATING THE ANALYSIS_TABLE
		btnUpdateAnalysis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				// start swingworker code
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// DO THE WORK!
						frmStockmaster.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						try {
							for (int i = 0; i < tickers.size(); i++) {
								String number = Integer.toString(i);
								String total = Integer.toString(tickers.size());

								txtpnInformationGoesHereAnalysis
										.setText("Processing " + number + " of " + total + "\nPlease wait");
								// The date-spinners
								int day = (int) spinner_day_analysis.getValue();
								int month = (int) spinner_month_analysis.getValue() - 1;
								int year = (int) spinner_year_analysis.getValue();
								myTime.setADate(year, month, day);
								// The weight-spinners
								int weight1 = (int) spinner_WMA_1_analysis.getValue();
								int weight2 = (int) spinner_WMA_2_analysis.getValue();
								int weight3 = (int) spinner_WMA_3_analysis.getValue();
								int rating = myData.evaluateAtDate(tickers.get(i), weight1, weight2, weight3,
										myTime.getADate());
								String rateChangeDate = myData.getRatingChangeDate(tickers.get(i), weight1, weight2,
										weight3, myTime.getADate());
								analysisData[i][0] = myTime.getADate();
								analysisData[i][1] = tickers.get(i);
								analysisData[i][2] = myData.getLastPrice(tickers.get(i), myTime.getADate());
								analysisData[i][3] = rating;
								analysisData[i][4] = rateChangeDate;
								analysisData[i][5] = myData.getLastPrice(tickers.get(i), myData.getRatingChangeDate(
										tickers.get(i), weight1, weight2, weight3, myTime.getADate()));
								analysisData[i][6] = decimalFormat.format(
										myData.getWeightedMovingAverage(tickers.get(i), weight1, myTime.getADate()));
								analysisData[i][7] = decimalFormat.format(
										myData.getWeightedMovingAverage(tickers.get(i), weight2, myTime.getADate()));
								analysisData[i][8] = decimalFormat.format(
										myData.getWeightedMovingAverage(tickers.get(i), weight3, myTime.getADate()));
								analysisData[i][9] = myData.getRecommendation(myData.evaluateAtDate(tickers.get(i),
										weight1, weight2, weight3, myTime.getADate()));
								analysisData[i][10] = decimalFormat
										.format((myData.getLastPrice(tickers.get(i), myTime.getADate())
												/ myData.getLastPrice(tickers.get(i), rateChangeDate) - 1) * 100);
								analysisData[i][11] = myData.getPreviousRating(tickers.get(i), weight1, weight2, weight3, myTime.getADate());
								analysisData[i][12] = myData.getPreviousRatingDate(tickers.get(i), weight1, weight2, weight3, myTime.getADate());
							}
							DefaultTableModel analysisModel = new DefaultTableModel(analysisData, analysisColumnNames);
							table_analysis.setModel(analysisModel);
							analysisModel.fireTableDataChanged();
							frmStockmaster.setCursor(Cursor.getDefaultCursor());
							txtpnInformationGoesHereAnalysis.setText("Finished! ");
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// End work
						return null;
					}
				};

				worker.execute();
				// end swing worker code
			}
		});

		JPanel panel_Live = new JPanel();
		panel_Live.setLayout(null);
		tabbedPane.addTab("Live analysis", null, panel_Live, null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 844, 565);
		panel_Live.add(scrollPane);

		table_live = new JTable();
		table_live.setEnabled(false);
		table_live.setName("liveTable");
		table_live.setAutoCreateRowSorter(true);
		table_live.setName("liveTable");
		table_live.setBackground(Color.WHITE);
		table_live.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table_live);
		table_live.setModel(liveModel);

		TableColumn liveColumn = null;
		liveColumn = table_live.getColumnModel().getColumn(0); // DTG-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(1); // TICKER-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(2); // PRICE-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(3); // RATING-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(4); // CHANGE
		// DATE-column
		liveColumn.setPreferredWidth(75);
		liveColumn = table_live.getColumnModel().getColumn(5); // WMA 1-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(6); // WMA 2-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(7); // WMA 3-column
		liveColumn.setPreferredWidth(30);
		liveColumn = table_live.getColumnModel().getColumn(8); // Recommend-column
		liveColumn.setPreferredWidth(30);

		table_live.setAutoCreateColumnsFromModel(false);

		JTextPane txtpnInformationGoesHereLive = new JTextPane();
		txtpnInformationGoesHereLive.setText("Information goes here");
		txtpnInformationGoesHereLive.setBounds(864, 257, 166, 166);
		panel_Live.add(txtpnInformationGoesHereLive);

		JSpinner spinner_day_live = new JSpinner();
		spinner_day_live.setBounds(968, 42, 62, 20);
		spinner_day_live.setModel(new SpinnerNumberModel(myTime.getADateDay(), 1, 31, 1));
		panel_Live.add(spinner_day_live);

		JLabel lblSelectDateLive = new JLabel("Select date");
		lblSelectDateLive.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectDateLive.setBounds(864, 42, 94, 20);
		panel_Live.add(lblSelectDateLive);

		JButton btnUpdateLive = new JButton("Update table");
		btnUpdateLive.setBounds(864, 223, 166, 23);
		panel_Live.add(btnUpdateLive);

		JLabel label_1 = new JLabel("Select month");
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		label_1.setBounds(864, 72, 94, 20);
		panel_Live.add(label_1);

		JSpinner spinner_month_live = new JSpinner();
		spinner_month_live.setLocale(new Locale("no"));
		spinner_month_live.setBounds(968, 72, 62, 20);
		spinner_month_live.setModel(new SpinnerNumberModel(myTime.getADateMonth() + 1, 1, 12, 1));
		panel_Live.add(spinner_month_live);

		JSpinner spinner_year_live = new JSpinner();
		spinner_year_live.setBounds(968, 102, 62, 20);
		spinner_year_live.setModel(new SpinnerNumberModel(myTime.getADateYear(), 1999, 2016, 1));
		panel_Live.add(spinner_year_live);

		JLabel lblSelectMonthLive = new JLabel("Select year");
		lblSelectMonthLive.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectMonthLive.setBounds(864, 102, 94, 20);
		panel_Live.add(lblSelectMonthLive);

		JLabel lblLiveAnalysis = new JLabel("Live Analysis");
		lblLiveAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
		lblLiveAnalysis.setForeground(Color.RED);
		lblLiveAnalysis.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLiveAnalysis.setBounds(10, 11, 135, 20);
		panel_Live.add(lblLiveAnalysis);

		JEditorPane dtrpnRatingsExplanationLive = new JEditorPane();
		dtrpnRatingsExplanationLive.setText(
				"RATINGS EXPLANATION\r\n\r\n1x is upward trend\r\n2x is downward trend\r\n\r\nx1 is confirmed buy-signal\r\nx4 is confirmed sell-signal\r\n\r\n11 - Best possible rating - BUY\r\n12 - BUY\r\n13 - STAY IN/OUT\r\n14 - SELL\r\n15 - STAY IN/OUT OWN\r\n\r\n21 - BUY\r\n22 - SELL\r\n23 - SELL\r\n24 - SELL\r\n25 - SELL");
		dtrpnRatingsExplanationLive.setBounds(1040, 42, 285, 565);
		panel_Live.add(dtrpnRatingsExplanationLive);

		JLabel lblWMA1_live = new JLabel("WMA 1");
		lblWMA1_live.setHorizontalAlignment(SwingConstants.LEFT);
		lblWMA1_live.setBounds(864, 132, 94, 20);
		panel_Live.add(lblWMA1_live);

		JLabel lblWMA2_live = new JLabel("WMA 2");
		lblWMA2_live.setHorizontalAlignment(SwingConstants.LEFT);
		lblWMA2_live.setBounds(864, 162, 94, 20);
		panel_Live.add(lblWMA2_live);

		JLabel lblWMA3_live = new JLabel("WMA 3");
		lblWMA3_live.setHorizontalAlignment(SwingConstants.LEFT);
		lblWMA3_live.setBounds(864, 192, 94, 20);
		panel_Live.add(lblWMA3_live);

		JSpinner spinner_WMA_1_live = new JSpinner();
		spinner_WMA_1_live.setModel(new SpinnerNumberModel(10, 0, 200, 1));
		spinner_WMA_1_live.setBounds(968, 132, 62, 20);
		panel_Live.add(spinner_WMA_1_live);

		JSpinner spinner_WMA_2_live = new JSpinner();
		spinner_WMA_2_live.setModel(new SpinnerNumberModel(20, 0, 200, 1));
		spinner_WMA_2_live.setLocale(new Locale("no"));
		spinner_WMA_2_live.setBounds(968, 162, 62, 20);
		panel_Live.add(spinner_WMA_2_live);

		JSpinner spinner_WMA_3_live = new JSpinner();
		spinner_WMA_3_live.setModel(new SpinnerNumberModel(100, 0, 200, 1));
		spinner_WMA_3_live.setBounds(968, 192, 62, 20);
		panel_Live.add(spinner_WMA_3_live);

		// UPDATING THE LIVE_TABLE
		btnUpdateLive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				// start swingworker code
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// DO THE WORK!
						frmStockmaster.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						try {
							for (int i = 0; i < tickers.size(); i++) {
								String number = Integer.toString(i);
								String total = Integer.toString(tickers.size());

								txtpnInformationGoesHereLive
										.setText("Processing " + number + " of " + total + "\nPlease wait");
								// The date-spinners
								int day = (int) spinner_day_live.getValue();
								int month = (int) spinner_month_live.getValue() - 1;
								int year = (int) spinner_year_live.getValue();
								myTime.setADate(year, month, day);
								// The weight-spinners
								int weight1 = (int) spinner_WMA_1_live.getValue();
								int weight2 = (int) spinner_WMA_2_live.getValue();
								int weight3 = (int) spinner_WMA_3_live.getValue();
								int rating = myData.evaluateAtDate(tickers.get(i), weight1, weight2, weight3,
										myTime.getADate());
								liveData[i][0] = myTime.getADate();
								liveData[i][1] = tickers.get(i);
								liveData[i][2] = myData.getLastPriceLive(tickers.get(i));
								liveData[i][3] = rating;
								liveData[i][4] = myData.getRatingChangeDate(tickers.get(i), weight1, weight2, weight3,
										myTime.getADate());
								// liveData[i][5] =
								// decimalFormat.format(myData.getWeightedMovingAverageLive(tickers.get(i),
								// weight1, myTime.getADate()));
								// liveData[i][6] =
								// decimalFormat.format(myData.getWeightedMovingAverageLive(tickers.get(i),
								// weight2, myTime.getADate()));
								// liveData[i][7] =
								// decimalFormat.format(myData.getWeightedMovingAverageLive(tickers.get(i),
								// weight3, myTime.getADate()));
								liveData[i][8] = myData.getRecommendation(rating);
							}
							DefaultTableModel liveModel = new DefaultTableModel(liveData, analysisColumnNames);
							table_live.setModel(liveModel);
							liveModel.fireTableDataChanged();
							frmStockmaster.setCursor(Cursor.getDefaultCursor());
							txtpnInformationGoesHereLive.setText("Finished! ");
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// End work
						return null;
					}
				};

				worker.execute();
				// end swing worker code
			}
		});

		JPanel panel_netfonds = new JPanel();
		panel_netfonds.setLayout(null);
		tabbedPane.addTab("Market", null, panel_netfonds, null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 1315, 596);
		panel_netfonds.add(scrollPane_1);

		JEditorPane editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		scrollPane_1.setViewportView(editorPane);
		editorPane.setEditable(false);
		try {
			editorPane.setPage("http://www.netfonds.no/quotes/market.php");
		} catch (IOException e1) {
			editorPane.setText("<html>Could not load</html>");
			e1.printStackTrace();
		}

		JPanel panel_simulator = new JPanel();
		tabbedPane.addTab("Simulator", null, panel_simulator, null);
		panel_simulator.setLayout(null);

		JEditorPane dtrpnwelcomeToStockmaster = new JEditorPane();
		dtrpnwelcomeToStockmaster.setEditable(false);
		dtrpnwelcomeToStockmaster.setContentType("text/html");
		dtrpnwelcomeToStockmaster.setText(
				"<html>\r\n\r\n<h1 style=\"color:blue;\">Coming soon: StockSimulator 2000!</h1>\r\n<h2>This is where the world famous StockSimulator 2000 will be!</h2>\r\n<p> This is the introduction text for StockSimulator 2000.\r\n<p>\r\n\r\n</html>");

		dtrpnwelcomeToStockmaster.setBounds(10, 11, 1315, 596);
		panel_simulator.add(dtrpnwelcomeToStockmaster);

		JMenuBar menuBar = new JMenuBar();
		frmStockmaster.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		mnNewMenu.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnNewMenu);

		JMenuItem mntmClose = new JMenuItem("Exit");
		mntmClose.setHorizontalAlignment(SwingConstants.LEFT);
		mntmClose.setAlignmentX(Component.LEFT_ALIGNMENT);
		mntmClose.setMinimumSize(new Dimension(20, 0));
		mnNewMenu.add(mntmClose);
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setHorizontalAlignment(SwingConstants.LEFT);
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JOptionPane.showMessageDialog(frmStockmaster, "StockMaster2000!");
			}
		});
	}
}

/*
 * // start swingworker code SwingWorker<Void, Void> worker = new
 * SwingWorker<Void, Void>() {
 * 
 * @Override protected Void doInBackground() throws Exception { // DO THE WORK!
 * 
 * // End work return null; } };
 * 
 * worker.execute(); // end swing worker code
 */
