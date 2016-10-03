package model;

/**
 * 
 * @author Daniel Rotaru
 * 
 * Base class for every domain object class
 *
 */
public abstract class Model {
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Model(int id) {
		this.id = id;
	}
}
