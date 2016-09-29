
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import model.Entry;

public class EntryDao extends AbstractDao implements Entity {

	public EntryDao(String conString, String user, String password) {
		super(conString, user, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() throws DataAccessException {
		return getCount("Entry");
	}

	@Override
	public Object getById(int id) throws DataAccessException {
        Collection<Entry> entry = getEntriesFromWhere("WHERE idEntry = ?", id);
        Iterator<Entry> it = entry.iterator();

        return it.hasNext() ? it.next() : null;
	}

	public Collection<Entry> getAll() throws DataAccessException {
		return getEntriesFromWhere("");
	}
	
	public Collection<Entry> getLast100() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void store(Object o) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Object o) throws DataAccessException {
		// TODO Auto-generated method stub
		
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
	
	/**
     * Method to query data from the ticker table
     * 
     * @param query
     * @param args
     * @return
     * @throws DataAccessException
     */
    private Collection<Entry> getEntriesFromWhere(String query, Object... args) throws DataAccessException {
        Collection<Entry> entries = new ArrayList<Entry>();
        
        try (PreparedStatement pstmt = this.getConnection().prepareStatement("select * from Entry " + query)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	Entry entry = new Entry();
                	
                	entry.setId(rs.getInt("idTickery"));
                    entry.setDate(rs.getDate("date"));
                    entry.setOpen(rs.getDouble("open"));
                    entry.setHigh(rs.getDouble("high"));
                    entry.setLow(rs.getDouble("low"));
                    entry.setClose(rs.getDouble("close"));
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

}
