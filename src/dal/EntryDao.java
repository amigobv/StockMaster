
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import model.Entry;
import model.Model;
import model.Ticker;

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
        Collection<Entry> entries = getEntriesFromWhere("WHERE idEntry = ?", id);
        Iterator<Entry> it = entries.iterator();

        return it.hasNext() ? it.next() : null;
	}
	
	public Collection<Entry> getAllByTickerId(int tickerId) throws DataAccessException {
		return getEntriesFromJoin(", Ticker where ticker = ? and ticker = idTicker", tickerId);
	}

	public Collection<Entry> getAll() throws DataAccessException {
		return getEntriesFromWhere("");
	}
	
	public Collection<Entry> getByTickerBetween(int id, Date start, Date end) throws DataAccessException {
		return getEntriesFromWhere("WHERE ticker = ? and date between ? and ?", id, start, end);
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
                "(date, openPrice, highPrice, lowPrice, closePrice, volume, value, rs, rsi, ticker) " + 
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
                )) {
            
            // set parameters
            pstmt.setDate(1, new java.sql.Date(entry.getDate().getTime()));
            pstmt.setDouble(2, entry.getOpen());
            pstmt.setDouble(3, entry.getHigh());
            pstmt.setDouble(4, entry.getLow());
            pstmt.setDouble(5, entry.getClose());
            pstmt.setDouble(6, entry.getVolume());
            pstmt.setDouble(7, entry.getValue());
            pstmt.setDouble(8, entry.getRs());
            pstmt.setDouble(9, entry.getRsi());
            pstmt.setInt(10, entry.getTicker() != null ? entry.getTicker().getId() : 0);
           
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
                "SET date = ?, openPrice = ?, highPrice = ?, lowPrice = ?, closePrice = ?, volume = ?, value = ?, rs = ?, rsi = ?, ticker = ? " +
                "WHERE idEntry = ?")){
            
            // Set parameters
            pstmt.setDate(1, new java.sql.Date(entry.getDate().getTime()));
            pstmt.setDouble(2, entry.getOpen());
            pstmt.setDouble(3, entry.getHigh());
            pstmt.setDouble(4, entry.getLow());
            pstmt.setDouble(5, entry.getClose());
            pstmt.setDouble(6, entry.getVolume());
            pstmt.setDouble(7, entry.getValue());
            pstmt.setDouble(8, entry.getRs());
            pstmt.setDouble(9, entry.getRsi());
            pstmt.setInt(10, entry.getTicker() != null ? entry.getTicker().getId() : 0);
            pstmt.setInt(11, entry.getId());
            
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
                    entry.setOpen(rs.getDouble("openPrice"));
                    entry.setHigh(rs.getDouble("highPrice"));
                    entry.setLow(rs.getDouble("lowPrice"));
                    entry.setClose(rs.getDouble("closePrice"));
                    entry.setVolume(rs.getDouble("volume"));
                    entry.setValue(rs.getDouble("value"));
                    entry.setRs(rs.getDouble("rs"));
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
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("select * from Entry " + query)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	Entry entry = new Entry();
                	
                	entry.setId(rs.getInt("idEntry"));
                    entry.setDate(rs.getDate("date"));
                    entry.setOpen(rs.getDouble("openPrice"));
                    entry.setHigh(rs.getDouble("highPrice"));
                    entry.setLow(rs.getDouble("lowPrice"));
                    entry.setClose(rs.getDouble("closePrice"));
                    entry.setVolume(rs.getDouble("volume"));
                    entry.setValue(rs.getDouble("value"));
                    entry.setRs(rs.getDouble("rs"));
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
