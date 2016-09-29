package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import model.Ticker;

public class TickerDao extends AbstractDao implements Entity {

	public TickerDao(String conString, String user, String password) {
		super(conString, user, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() throws DataAccessException {
		return getCount("Ticker");
	}

	@Override
	public Object getById(int id) throws DataAccessException {
		Collection<Ticker> tickers = getTickersFromWhere("WHERE idTicker = ?", id);
		Iterator<Ticker> it = tickers.iterator();
		
		return it.hasNext() ? it.next() : null;
	}

	public Object getByName(String name) throws DataAccessException {
		Collection<Ticker> tickers = getTickersFromWhere("WHERE name LIKE ?", name);
		Iterator<Ticker> it = tickers.iterator();
		
		return it.hasNext() ? it.next() : null;
	}

	public Collection<Ticker> getAll() throws DataAccessException {
		return getTickersFromWhere("");
	}

	@Override
	public void store(Object o) throws DataAccessException {
		Ticker ticker = (Ticker)o;
		
		 if (ticker == null)
	            throw new DataAccessException("The ticker object is null");
	        
	        if (ticker.getId() != -1) {
	            throw new DataAccessException("Ticker cannot be store twice!");
	        }
	        
	        try (PreparedStatement pstmt = this.getConnection().prepareStatement(
	                "insert into Ticker " + 
	                "(name, symbol, exchange) " + 
	                "values (?, ?, ?)",
	                Statement.RETURN_GENERATED_KEYS
	                )) {
	            
	            // set parameters
	            pstmt.setString(1, ticker.getName());
	            pstmt.setString(2, ticker.getSymbol());
	            pstmt.setString(3, ticker.getExchange());
	           
	            // insert data set
	            pstmt.executeUpdate();
	            
	            try (ResultSet rs = pstmt.getGeneratedKeys()) {
	                if (rs != null && rs.next()) {
	                	ticker.setId(rs.getInt(1));
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
	public void update(Object o) throws DataAccessException {
		Ticker ticker = (Ticker)o;
		
        if (ticker == null)
            throw new DataAccessException("The ticker object is null");
        
        if (ticker.getId() == -1) {
            throw new DataAccessException("Ticker is not stored in DB!");
        }
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement(
                "update Ticker " +
                "SET name = ?, symbol = ?, exchange = ?" +
                "WHERE idTicker = ?")){
            
            // Set parameters
            pstmt.setString(1, ticker.getName());
            pstmt.setString(2, ticker.getSymbol());
            pstmt.setString(3, ticker.getExchange());
            pstmt.setInt(4, ticker.getId());
            
            // save to DB
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
	}
	
	@Override
	public void delete(int id) throws DataAccessException {
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("delete from Ticker where idTicker = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
	}
	
	 /**
     * Method to query data from the ticker table
     * 
     * @param query
     * @param args
     * @return
     * @throws DataAccessException
     */
    private Collection<Ticker> getTickersFromWhere(String query, Object... args) throws DataAccessException {
        Collection<Ticker> tickers = new ArrayList<Ticker>();
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("select * from Ticker " + query)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	Ticker ticker = new Ticker();
                	
                	ticker.setId(rs.getInt("idTicker"));
                    ticker.setName(rs.getString("name"));
                    ticker.setSymbol(rs.getString("symbol"));
                    ticker.setExchange(rs.getString("exchange"));
                    
                    tickers.add(ticker);
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }  
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        
        return tickers;
    }

}
