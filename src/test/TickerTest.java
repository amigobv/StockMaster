package test;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dal.TickerDao;
import model.Ticker;

public class TickerTest {
	//private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/StockMasteTestDb";
	private static final String CONNECTION_STRING = "jdbc:derby:D:\\Private\\Dropbox\\Freelancing\\StockRSI\\StockTestDb;create=true";
	
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private TickerDao dao;
	
	
	@Before
	public void setUp() {
		System.out.println("Setup test infrastructure");
		dao = new TickerDao(CONNECTION_STRING, USERNAME, PASSWORD);
		
		dao.store(new Ticker("Ticker 1"));
		dao.store(new Ticker("Ticker 2"));
		dao.store(new Ticker("Ticker 3"));
		dao.store(new Ticker("Ticker 4"));
		Assert.assertEquals(4, dao.getCount()); 
	}
	
	@After
	public void tearDown() {
		System.out.println("tear down test infrastructure");
		Collection<Ticker> tickers = dao.getAll();
		for (Ticker ticker : tickers) {
			dao.delete(ticker.getId());
		}
		Assert.assertEquals(0, dao.getCount()); 
	}
	
	@Test
	public void connectionTest() {
		Assert.assertEquals(4, dao.getCount()); 
	}
	
	@Test
	public void insertTest() {
		Ticker ticker = new Ticker("Ticker 55");
		dao.store(ticker);
		Assert.assertEquals(5, dao.getCount()); 	
	}
	
	@Test
	public void getByIdTest() {
		Ticker ticker = new Ticker("Ticker 556");
		dao.store(ticker);
		Ticker dbTicker = (Ticker)dao.getById(ticker.getId());
		Assert.assertEquals(ticker.getId(), dbTicker.getId()); 	
	}
	
	@Test
	public void getByNameTest() {
		Ticker ticker = new Ticker("Ticker 556");
		dao.store(ticker);
		Ticker dbTicker = (Ticker)dao.getByName(ticker.getName());
		Assert.assertEquals(ticker.getId(), dbTicker.getId()); 	
	}
	
	@Test
	public void getUpdateTest() {
		Ticker ticker = new Ticker("Ticker 556");
		dao.store(ticker);
		ticker.setExchange("Home");
		dao.update(ticker);
		Ticker dbTicker = (Ticker)dao.getById(ticker.getId());
		Assert.assertEquals(ticker.getExchange(), dbTicker.getExchange()); 	
	}

}
