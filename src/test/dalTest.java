package test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dal.DataAccessLayer;
import helpers.DateUtils;
import model.Entry;
import model.Ticker;

public class dalTest {

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
		dal.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1), Entry.class);
		Assert.assertEquals(5,  dal.getCount(Entry.class));
	}
	
	@Test
	public void updateTest() {
		Entry entry = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1);
		dal.store(entry, Entry.class);
		entry.setClose(21.2);
		Assert.assertEquals(5,  dal.getCount(Entry.class));
		
		dal.update(entry,  Entry.class);
		Entry dbEntry = dal.getEntryById(entry.getId());
		Assert.assertEquals(Math.round(entry.getClose()), Math.round(dbEntry.getClose()));
	}
	
	@Test
	public void deleteTest() {
		Entry entry = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1);
		dal.store(entry, Entry.class);
		Assert.assertEquals(5,  dal.getCount(Entry.class));
		
		dal.delete(entry, Entry.class);
		Assert.assertEquals(4,  dal.getCount(Entry.class));
	}

	@Test
	public void getEntryByIdTest() {
		Entry entry = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1);
		dal.store(entry, Entry.class);
		Assert.assertEquals(5,  dal.getCount(Entry.class));
		
		Entry dbENtry = dal.getEntryById(entry.getId());
		Assert.assertEquals(entry,  dbENtry);
	}
	
	@Test
	public void getAllEntriesTest() {
		Entry entry = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1);
		dal.store(entry, Entry.class);
		Assert.assertEquals(5,  dal.getCount(Entry.class));
		
		Collection<Entry> entries = dal.getAllEntries(testTicker);
		Assert.assertEquals(5,  entries.size());
	}
	
	@Test
	public void getAllEntriesBetweenTest() {
		Entry entry = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1);
		dal.store(entry, Entry.class);
		Assert.assertEquals(5,  dal.getCount(Entry.class));
		
		Date start = DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 27));
		Date end = DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 30));
		Collection<Entry> entries = dal.getAllEntriesBetween(testTicker, start, end);
		Assert.assertEquals(4,  entries.size());
	}
	
	@Test
	public void getLastEntriesTest() {
		Entry entryOld = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 26)), 20,1);
		Entry entry = new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 20,1);
		dal.store(entryOld, Entry.class);
		dal.store(entry, Entry.class);
		Collection<Entry> entries = dal.getLastEntries(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 10, 3)), 3);
		Assert.assertEquals(3,  entries.size());
	}
	
	@Test
	public void getTickerByNameTest() {
		Ticker ticker = new Ticker("Ticker 556");
		dal.store(ticker, Ticker.class);
		Ticker dbTicker = (Ticker)dal.getTickerByName(ticker.getName());
		Assert.assertEquals(ticker.getId(), dbTicker.getId()); 	
	}
	
	@Test
	public void getAllTickers() {
		Ticker ticker = new Ticker("Ticker 556");
		dal.store(ticker, Ticker.class);
		
		Collection<Ticker> entries = dal.getAllTickers();
		Assert.assertEquals(2,  entries.size());
	}
	
	@Test
	public void getTickerById() {
		Ticker ticker = new Ticker("Ticker 556");
		dal.store(ticker, Ticker.class);
	}
}
