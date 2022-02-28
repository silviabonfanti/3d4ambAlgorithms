package p3d4amb.algorithms.comparator;

import p3d4amb.algorithms.BestNThresholdCertifier;

public class BestNSim extends ThresholdCertifierSim{

	BestNThresholdCertifier dp;
	int stepsBestN;

	public BestNSim(int startingLevel) {
		dp = new BestNThresholdCertifier(startingLevel);
		setStepsBestN(0);
	}

	public BestNThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsBestN() {
		return stepsBestN;
	}

	public void setStepsBestN(int stepsBestN) {
		this.stepsBestN = stepsBestN;
	}
}
