package p3d4amb.algorithms.comparator;

import p3d4amb.algorithms.BestNThresholdCertifier;

public class Best3DSim extends ThresholdCertifierSim{

	BestNThresholdCertifier dp;
	int stepsBest3D;

	public Best3DSim(int startingLevel) {
		dp = new BestNThresholdCertifier(startingLevel);
		stepsBest3D=0;
	}

	public BestNThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsBest3D() {
		return stepsBest3D;
	}
}
