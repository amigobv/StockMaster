package dal;

import model.Model;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Interface which should be implemented by all Data Access Objects DAO 
 *
 */
public interface Entity {
	public int getCount() throws DataAccessException;
	public Model getById(int id) throws DataAccessException;
	public void store(Model o) throws DataAccessException;
	public void update(Model o) throws DataAccessException;
	public void delete(int id) throws DataAccessException;
}
