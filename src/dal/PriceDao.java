
package dal;

import java.util.Collection;

public class PriceDao extends AbstractDao implements Entity {

	public PriceDao(String conString, String user, String password) {
		super(conString, user, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getById(int id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getByName(String name) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> getAll() throws DataAccessException {
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
		// TODO Auto-generated method stub
		
	}

}
