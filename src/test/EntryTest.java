package test;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dal.EntryDao;
import dal.TickerDao;
import helpers.DateUtils;
import model.Entry;
import model.Ticker;

public class EntryTest {
	private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/StockMasteTestDb";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private EntryDao dao;
	private TickerDao daoTicker;
	Ticker testTicker;
	
	
	@Before
	public void setUp() {
		System.out.println("Setup test infrastructure");
		dao = new EntryDao(CONNECTION_STRING, USERNAME, PASSWORD);
		daoTicker = new TickerDao(CONNECTION_STRING, USERNAME, PASSWORD);
		testTicker = new Ticker("Test ticker");
		daoTicker.store(testTicker);
		
		dao.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 27)), 11.2));
		dao.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 28)), 14.5));
		dao.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 29)), 13.6));
		dao.store(new Entry(testTicker, DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 30)), 19.8));
		Assert.assertEquals(4, dao.getCount()); 
	}
	
	@After
	public void tearDown() {
		System.out.println("tear down test infrastructure");
		Collection<Entry> entries = dao.getAll();
		for (Entry entry : entries) {
			dao.delete(entry.getId());
		}
		Assert.assertEquals(0, dao.getCount()); 
	}
	
	@Test
	public void connectionTest() {
		Assert.assertEquals(4, dao.getCount()); 
	}
	
	@Test
	public void insertTest() {
		Entry entry = new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 30)), 20.8);
		dao.store(entry);
		Assert.assertEquals(5, dao.getCount()); 	
	}
	
	@Test
	public void getByIdTest() {
		Entry entry = new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 25)), 138);
		dao.store(entry);
		Entry dbEntry = (Entry)dao.getById(entry.getId());
		Assert.assertEquals(entry.getId(), dbEntry.getId()); 	
	}
	
	@Test
	public void getUpdateTest() {
		Entry entry = new Entry(DateUtils.LocalDateToDate(LocalDate.of(2016, 9, 23)), 20.8);
		dao.store(entry);
		entry.setOpen(14.5);
		dao.update(entry);
		Entry dbEntry = (Entry)dao.getById(entry.getId());
		Assert.assertEquals((int)entry.getOpen(), (int)dbEntry.getOpen()); 	
	}

	@Test
	public void getByTickerBetween() {
		
	}
	
	@Test
	public void getByTickerId() {
		Collection<Entry> entries = dao.getByTickerId(testTicker.getId());
		Assert.assertEquals(4, entries); 
	}
}
