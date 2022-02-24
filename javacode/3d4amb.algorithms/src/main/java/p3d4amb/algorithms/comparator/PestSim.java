package p3d4amb.algorithms.comparator;

import p3d4amb.algorithms.PestThresholdCertifier;

public class PestSim extends ThresholdCertifierSim {

	PestThresholdCertifier dp;
	int stepsPest;

	public PestSim(int startingLevel) {
		dp = new PestThresholdCertifier(startingLevel);
		stepsPest=0;
	}

	public PestThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsPest() {
		return stepsPest;
	}
}
