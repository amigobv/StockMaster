package dal;

import java.util.Collection;

public interface Entity {
	public int getCount() throws DataAccessException;
	public Object getById(int id) throws DataAccessException;
	public Object getByName(String name) throws DataAccessException;
	public Collection<Object> getAll() throws DataAccessException;
	public void store(Object o) throws DataAccessException;
	public void update(Object o) throws DataAccessException;
	public void delete(int id) throws DataAccessException;
}
