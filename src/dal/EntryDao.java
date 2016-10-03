
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.Entry;
import model.Model;
import model.Recommandation;
import model.Ticker;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Class to access the Entry table. 
 * 
 * THIS CLASS IS ONLY USED BY THE DATA ACCESS LAYER. 
 * DO NOT USE THIS CLASS TO DIRECTLY ACCESS THE ENTRY TABLE!!! 
 *
 */
public class EntryDao extends AbstractDao implements Entity {

	public EntryDao(String conString, String user, String password) {
		super(conString, user, password);
	}

	@Override
	public int getCount() throws DataAccessException {
		return getCount("Entry");
	}

	@Override
	public Model getById(int id) throws DataAccessException {
        Collection<Entry> entries = getEntriesFromJoin("WHERE idEntry = ? and ticker = idTicker", id);
        Iterator<Entry> it = entries.iterator();

        return it.hasNext() ? it.next() : null;
	}
	
	public Collection<Entry> getAllByTickerId(int tickerId) throws DataAccessException {
		return getEntriesFromJoin("WHERE ticker = ? and ticker = idTicker", tickerId);
	}

	public Collection<Entry> getAll() throws DataAccessException {
		return getEntriesFromWhere("");
	}
	
	public Collection<Entry> getByTickerBetween(int id, Date start, Date end) throws DataAccessException {
		if (start == null || end == null)
			throw new IllegalArgumentException();

		return getEntriesFromJoin("WHERE ticker = ? and ticker = idTicker and date between ? and ?", id, start, end);
	}
	
	public Collection<Entry> getLastEntriesByTickerId(int id, Date start, int numOfEntries) {
		if (start == null)
			throw new IllegalArgumentException();
		
		List<Entry> entries = new ArrayList<Entry>(getEntriesFromJoin("WHERE ticker = ? and ticker = idTicker and date <= ? ORDER BY date DESC", id, start));
		return entries.subList(0, Math.min(entries.size(), numOfEntries));
	}


	@Override
	public void store(Model o) throws DataAccessException {
		Entry entry = (Entry)o;
        if (entry == null)
            throw new DataAccessException("The entry object is null");
        
        if (entry.getId() != -1) {
            throw new DataAccessException("Entry cannot be store twice!");
        }
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement(
                "insert into Entry " + 
                "(date, ticker, closingPrice, change, dateOfChange, priceAtChange, wma10, wma20, wma100, " +
                "recommandation, percentage, rating, prevRating, prevRatingDate, rsi)" +		
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
                )) {
            
            // set parameters
            pstmt.setDate(1, new java.sql.Date(entry.getDate().getTime()));
            pstmt.setDouble(2, entry.getTicker() != null ? entry.getTicker().getId() : 0);
            pstmt.setDouble(3, entry.getClose());
            pstmt.setDouble(4, entry.getChange());
            pstmt.setDate(5, new java.sql.Date(entry.getDateOfChange().getTime()));
            pstmt.setDouble(6, entry.getPriceAtChange());
            pstmt.setDouble(7, entry.getWma10());
            pstmt.setDouble(8, entry.getWma20());
            pstmt.setDouble(9, entry.getWma100());
            pstmt.setString(10, entry.getRecommandation().toString());
            pstmt.setDouble(11, entry.getPercentageSinceRecommandation());
            pstmt.setInt(12, entry.getRating());
            pstmt.setInt(13, entry.getPreviousRating());
            pstmt.setDate(14, new java.sql.Date(entry.getPreviousRatingDate().getTime()));
            pstmt.setDouble(15, entry.getRsi());
           
            // insert data set
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs != null && rs.next()) {
                	entry.setId(rs.getInt(1));
                } else {
                    throw new DataAccessException("Auto generated values not supported by DB");
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
            
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
	}

	@Override
	public void update(Model o) throws DataAccessException {
		Entry entry = (Entry)o;
        if (entry == null)
            throw new DataAccessException("The entry object is null");
        
        if (entry.getId() == -1) {
            throw new DataAccessException("Entry is not stored in DB!");
        }
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement(
                "update Entry " +
                "SET date = ?, ticker = ?, closingPrice = ?, change = ?, dateOfChange = ?, priceAtChange = ?, wma10 = ?, wma20 = ?, wma100 = ?, " +
                "recommandation = ?, percentage = ?, rating = ?, prevRating = ?, prevRatingDate = ?, rsi = ?" +
                "WHERE idEntry = ?")){
            
            // Set parameters
            pstmt.setDate(1, new java.sql.Date(entry.getDate().getTime()));
            pstmt.setInt(2, entry.getTicker() != null ? entry.getTicker().getId() : 0);
            pstmt.setDouble(3, entry.getClose());
            pstmt.setDouble(4, entry.getChange());
            pstmt.setDate(5, new java.sql.Date(entry.getDateOfChange().getTime()));
            pstmt.setDouble(6, entry.getPriceAtChange());
            pstmt.setDouble(7, entry.getWma10());
            pstmt.setDouble(8, entry.getWma20());
            pstmt.setDouble(9, entry.getWma100());
            pstmt.setString(10, entry.getRecommandation().toString());
            pstmt.setDouble(11, entry.getPercentageSinceRecommandation());
            pstmt.setInt(12, entry.getRating());
            pstmt.setInt(13, entry.getPreviousRating());
            pstmt.setDate(14, new java.sql.Date(entry.getPreviousRatingDate().getTime()));
            pstmt.setDouble(15, entry.getRsi());
            pstmt.setInt(16, entry.getId());
            
            // save to DB
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
		
	}
	
	@Override
	public void delete(int id) throws DataAccessException {
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("delete from Entry where idEntry = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
	}
	
	private Collection<Entry> getEntriesFromWhere(String query, Object... args) throws DataAccessException {
    	Collection<Entry> entries = new ArrayList<Entry>();
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("select * from Entry " + query)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	Entry entry = new Entry();

                	entry.setId(rs.getInt("idEntry"));
                    entry.setDate(rs.getDate("date"));
                    entry.setClose(rs.getDouble("closingPrice"));
                    entry.setChange(rs.getDouble("change"));
                    entry.setDateOfChange(rs.getDate("dateOfChange"));
                    entry.setChange(rs.getDouble("priceAtChange"));
                    entry.setWma10(rs.getDouble("wma10"));
                    entry.setWma20(rs.getDouble("wma20"));
                    entry.setWma100(rs.getDouble("wma100"));
                    entry.setRecommandation(Recommandation.fromString(rs.getString("recommandation")));
                    entry.setPercentageSinceRecommandation(rs.getDouble("percentage"));
                    entry.setRating(rs.getInt("rating"));
                    entry.setPreviousRating(rs.getInt("prevRating"));
                    entry.setPreviousRatingDate(rs.getDate("prevRatingDate"));
                    entry.setRsi(rs.getDouble("rsi"));
                    entries.add(entry);
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }  
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        
        return entries;
    }
	
	/**
     * Method to query data from the ticker table
     * 
     * @param query
     * @param args
     * @return
     * @throws DataAccessException
     */
    private Collection<Entry> getEntriesFromJoin(String query, Object... args) throws DataAccessException {
    	Collection<Entry> entries = new ArrayList<Entry>();
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("select * from Entry, Ticker " + query)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	Entry entry = new Entry();
                	
                	entry.setId(rs.getInt("idEntry"));
                    entry.setDate(rs.getDate("date"));
                    entry.setClose(rs.getDouble("closingPrice"));
                    entry.setChange(rs.getDouble("change"));
                    entry.setDateOfChange(rs.getDate("dateOfChange"));
                    entry.setChange(rs.getDouble("priceAtChange"));
                    entry.setWma10(rs.getDouble("wma10"));
                    entry.setWma20(rs.getDouble("wma20"));
                    entry.setWma100(rs.getDouble("wma100"));
                    entry.setRecommandation(Recommandation.fromString(rs.getString("recommandation")));
                    entry.setPercentageSinceRecommandation(rs.getDouble("percentage"));
                    entry.setRating(rs.getInt("rating"));
                    entry.setPreviousRating(rs.getInt("prevRating"));
                    entry.setPreviousRatingDate(rs.getDate("prevRatingDate"));
                    entry.setRsi(rs.getDouble("rsi"));
                    
                    Ticker ticker = new Ticker(rs.getInt("idTicker"));
                    ticker.setName(rs.getString("name"));
                    ticker.setSymbol(rs.getString("symbol"));
                    ticker.setExchange(rs.getString("exchange"));
                    entry.setTicker(ticker);
                    entries.add(entry);
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }  
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        
        return entries;
    }

}
