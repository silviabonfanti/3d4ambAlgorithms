package p3d4amb.algorithms.comparator;


import p3d4amb.algorithms.StrictStaircaseThresholdCertifier;

public class StrictSim extends ThresholdCertifierSim{

	StrictStaircaseThresholdCertifier dp;
	private int stepsStrict;

	public StrictSim(int startingLevel) {
		dp = new StrictStaircaseThresholdCertifier(startingLevel,startingLevel);
		setStepsStrict(0);
	}

	public StrictStaircaseThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsStrict() {
		return stepsStrict;
	}

	public void setStepsStrict(int stepsStrict) {
		this.stepsStrict = stepsStrict;
	}
}
