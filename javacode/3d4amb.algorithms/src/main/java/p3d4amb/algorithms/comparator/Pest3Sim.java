package p3d4amb.algorithms.comparator;


import p3d4amb.algorithms.PestNThresholdCertifier;

public class Pest3Sim extends ThresholdCertifierSim{

	public PestNThresholdCertifier dp;
	public int stepsPestNew;


	public Pest3Sim(int startingLevel) {
		dp = new PestNThresholdCertifier(startingLevel);
		stepsPestNew=0;
	}

	public PestNThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsPestNew() {
		return stepsPestNew;
	}
}
