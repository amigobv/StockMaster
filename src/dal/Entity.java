package dal;

public interface Entity {
	public int getCount() throws DataAccessException;
	public Object getById(int id) throws DataAccessException;
	public void store(Object o) throws DataAccessException;
	public void update(Object o) throws DataAccessException;
	public void delete(int id) throws DataAccessException;
}
