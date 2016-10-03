package dal;

import java.util.Collection;
import java.util.Date;

import model.Entry;
import model.Model;
import model.Ticker;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Interface for the Data Access Layer
 *
 */
public interface DataLayer {
	void store(Model model, Class<?> c);
	void update(Model model, Class<?> c);
	void delete(Model model, Class<?> c);
	
	int getCount(Class<?> c);
	
	Entry getEntryById(int id);
	Collection<Entry> getAllEntries(Ticker ticker);
	Collection<Entry> getAllEntriesBetween(Ticker ticker, Date start, Date end);
	Collection<Entry> getLastEntries(Ticker ticker, Date start, int numOfRows);
	
	Ticker getTickerByName(String name);
	Collection<Ticker> getAllTickers();
	Ticker getTickerById(int id);
}
