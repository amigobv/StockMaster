package model;

public enum Recommandation {
	UNDEFINED ("UNDEFINED"),
	SELL ("SELL"),
	BUY ("BUY");
	
	private final String name;       

    private Recommandation(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
    
    public static Recommandation fromString(String strEnum) {
        if (strEnum == null) 
        	return null;
        
        for (Recommandation r : Recommandation.values()) {
          if (strEnum.equalsIgnoreCase(r.name)) {
            return r;
          }
        }
        
        return null;
      }
}
