package p3d4amb.algorithms.comparator;


import p3d4amb.algorithms.PEST_NThresholdCertifier;

public class Pest3Sim extends ThresholdCertifierSim{

	public PEST_NThresholdCertifier dp;
	public int stepsPestNew;


	public Pest3Sim(int startingLevel) {
		dp = new PEST_NThresholdCertifier(startingLevel);
		stepsPestNew=0;
	}

	public PEST_NThresholdCertifier getDp() {
		return dp;
	}

	public int getStepsPestNew() {
		return stepsPestNew;
	}
}
