package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Daniel Rotaru
 * 
 * This class handles the database connection
 *
 */
public abstract class AbstractDao implements AutoCloseable {
    private Connection con;
    protected String conString;
    protected String username;
    protected String password;
    
    public AbstractDao(String conString, String user, String password) {
        this.conString = conString;
        this.username = user;
        this.password = password;    
    }
    
    @Override
    public void close() throws Exception {
        try { 
            if (con != null) {
                con.close();
            }
            
            con = null;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    
    public Connection getConnection() throws DataAccessException {
        try {
            if (con == null) {
                con = DriverManager.getConnection(conString, username, password);
            }
            
            return con;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    
    protected int getCount(String table) throws DataAccessException {
        int count = 0;
        
        try (Statement stmt = this.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery("select count(id" + table + ") as count from " + table);
            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        
        return 0;
    }

}
