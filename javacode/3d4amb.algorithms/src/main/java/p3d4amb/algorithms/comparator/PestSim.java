package p3d4amb.algorithms.comparator;

import p3d4amb.algorithms.PESTThresholdCertifier;

public class PestSim extends ThresholdCertifierSim {

	PESTThresholdCertifier dp;
	int stepsPest;

	public PestSim(int startingLevel) {
		dp = new PESTThresholdCertifier(startingLevel);
		stepsPest=0;
	}

	public PESTThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsPest() {
		return stepsPest;
	}
}
