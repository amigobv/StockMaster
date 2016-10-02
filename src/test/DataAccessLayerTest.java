package test;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dal.DataAccessLayer;
import helpers.DateUtils;
import model.Entry;
import model.Ticker;

public class DataAccessLayerTest {
	private DataAccessLayer dal;
	private Ticker testTicker;
	
	@Before
	public void setUp() {
		System.out.println("Setup test infrastructure");
		dal = new DataAccessLayer();
		
		testTicker = new Ticker("Test ticker");
		
		dal.store(testTicker, Ticker.class);
		dal.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 27)), 11.2), Entry.class);
		dal.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 28)), 14.5), Entry.class);
		dal.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 29)), 13.6), Entry.class);
		dal.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 30)), 19.8), Entry.class);
		
		Assert.assertEquals(4, dal.getCount(Entry.class));
		Assert.assertEquals(1, dal.getCount(Ticker.class));
	}
	
	@After
	public void tearDown() {
		System.out.println("tear down test infrastructure");
		
		
		Collection<Ticker> tickers = dal.getAllTickers();
		for (Ticker ticker : tickers) {
			dal.delete(ticker, Ticker.class);
		}

		Assert.assertEquals(0, dal.getCount(Entry.class)); 
		Assert.assertEquals(0, dal.getCount(Entry.class)); 
	}
	
	@Test
	public void storeTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void updateTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void deleteTest() {
		fail("Not yet implemented");
	}

	@Test
	public void getEntryByIdTest() {
		
	}
	
	@Test
	void getAllEntriesTest() {
		
	}
	
	@Test
	void getAllEntriesBetweenTest() {
		
	}
	
	@Test
	void getLastEntriesTest() {
		
	}
	
	@Test
	void getTickerByNameTest() {
		
	}
	
	@Test
	void getAllTickers() {
		
	}
	
	@Test
	void getTickerById() {
		
	}
}
