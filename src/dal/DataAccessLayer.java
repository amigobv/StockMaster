package dal;

import java.util.Collection;
import java.util.Date;

import model.Entry;
import model.Model;
import model.Ticker;

public class DataAccessLayer implements DataLayer {
    private final String CONNECTION_STRING = "jdbc:derby:D:\\Private\\Dropbox\\Freelancing\\StockRSI\\StockMasterDb;create=true";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    
	private EntryDao entryDao;
	private TickerDao tickerDao;
	
	public DataAccessLayer() {
		entryDao = new EntryDao(CONNECTION_STRING, USERNAME, PASSWORD);
		tickerDao = new TickerDao(CONNECTION_STRING, USERNAME, PASSWORD);
	}

	@Override
	public void store(Model model, Class<?> c) {
		if (model == null || c == null)
			throw new IllegalArgumentException();

		Entity dao = tickerDao;
		
		if (c == Entry.class)
			dao = entryDao;
		
		dao.store(model);
	}

	@Override
	public void update(Model model, Class<?> c) {
		if (model == null || c == null)
			throw new IllegalArgumentException();

		Entity dao = tickerDao;

		if (c == Entry.class)
			dao = entryDao;
		
		dao.update(model);
	}

	@Override
	public void delete(Model model, Class<?> c) {
		if (model == null || c == null)
			throw new IllegalArgumentException();
		
		Entity dao = tickerDao;

		if (c == Entry.class)
			dao = entryDao;
		
		dao.delete(model.getId());
	}

	@Override
	public int getCount(Class<?> c) {
		if (c == null)
			throw new IllegalArgumentException();
		
		Entity dao = tickerDao;

		if (c == Entry.class)
			dao = entryDao;
		
		return dao.getCount();
	}
	
	@Override
	public Entry getEntryById(int id) {
		return (Entry)entryDao.getById(id);
	}

	@Override
	public Collection<Entry> getAllEntries(Ticker ticker) {
		if (ticker == null)
			throw new IllegalArgumentException();
		
		return entryDao.getAllByTickerId(ticker.getId());
	}

	@Override
	public Collection<Entry> getAllEntriesBetween(Ticker ticker, Date start, Date end) {
		if (ticker == null || start == null || end == null)
			throw new IllegalArgumentException();
		
		return entryDao.getByTickerBetween(ticker.getId(), start, end);
	}

	@Override
	public Collection<Entry> getLastEntries(Ticker ticker, Date start) {
		if (ticker == null || start == null)
			throw new IllegalArgumentException();
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Ticker getTickerByName(String name) {
		if (name == null)
			throw new IllegalArgumentException();
		
		return (Ticker)tickerDao.getByName(name);
	}

	@Override
	public Collection<Ticker> getAllTickers() {
		return tickerDao.getAll();
	}

	@Override
	public Ticker getTickerById(int id) {
		return (Ticker)tickerDao.getById(id);
	}

}
