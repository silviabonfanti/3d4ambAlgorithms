package p3d4amb.algorithms.comparator;


import p3d4amb.algorithms.PestNThresholdCertifier;

public class PestNSim extends ThresholdCertifierSim{

	PestNThresholdCertifier dp;
	private int stepsPestN;


	public PestNSim(int startingLevel) {
		dp = new PestNThresholdCertifier(startingLevel);
		setStepsPestN(0);
	}

	public PestNThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsPestN() {
		return stepsPestN;
	}

	public void setStepsPestN(int stepsPestN) {
		this.stepsPestN = stepsPestN;
	}
}
