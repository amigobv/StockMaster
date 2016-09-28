package algorithms.rsi;

public abstract class RelativeStrengthIndexAbstract implements RelativeStrengthIndex {
	
	public RelativeStrengthIndexAbstract() {
	}
	
	protected double calculateRelativStrength(double gainAverage, double lossAverage) {
		if (lossAverage == 0)
			return 0;
		
		return gainAverage / lossAverage;
	}
	
	protected double calculateRelativeStrengthIndex(double relativStrength) {
		return 100 - (100.0 / (1 + relativStrength));
	}
}
