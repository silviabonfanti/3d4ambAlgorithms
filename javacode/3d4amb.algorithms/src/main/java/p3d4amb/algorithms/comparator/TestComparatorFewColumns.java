package p3d4amb.algorithms.comparator;

import static p3d4amb.algorithms.ThresholdCertifier.Result.CONTINUE;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import p3d4amb.algorithms.BestNThresholdCertifier;
import p3d4amb.algorithms.PESTThresholdCertifier;
import p3d4amb.algorithms.PEST_NThresholdCertifier;
import p3d4amb.algorithms.StrictStaircaseThresholdCertifier;
import p3d4amb.algorithms.ThresholdCertifier;
import p3d4amb.algorithms.ThresholdCertifier.Solution;

public class TestComparatorFewColumns {

	static final int numpatients = 10000;
	static final int startingLevel = 10;
	static final int maxTargetLevel = 9;

	// probabilità che faccia giusta se >= target e sbagliata se < target
	// 1 -> perfetto (test non sbaglia mai)
	// 0.98 -> sbaglia
	// probabilità che dia la risposta corretta (RIGHT) quando il livello >= target
	static final double[] probExpCorrectAnswL_GE_T = { 1, 0.9, 0.9};
	// probabilità che dia la risposta corretta (WRONG) quando il livello < target
	static final double[] probExpCorrectAnswL_LT_T = { 1, 0.9, 0.75};
	// probabilità che sbagli non avendo stereopsi
	static final double[] probExpCoorectAnswNotCert = { 1, 0.9, 0.75 };

	public static void main(String[] args) throws IOException {
		// when needed use teh file
		PrintStream fileOut = new PrintStream("data.csv");
		System.setOut(fileOut);
		//
		System.out.printf("scenario,idPatient,target,testType,time,steps,level,finalResult\n");
		// scenario
		for (int s = 0; s <=2; s++) {
			//
			for (int i = 0; i < numpatients; i++) {
				// create patient
				Patient p = new Patient(maxTargetLevel);
				for (int time = 1; time <= 2; time++) {
					// create certifiers
					ThresholdCertifier[] certifiers = {
							// PestDepthCertifier
							new PESTThresholdCertifier(startingLevel),
							// PestDepthCertifierNew
							new PEST_NThresholdCertifier(startingLevel),
							// Best3DepthCertifier
							new BestNThresholdCertifier(startingLevel),
							// StrictStaircaseDepthCertifier
							new StrictStaircaseThresholdCertifier(startingLevel, startingLevel) };
					for (ThresholdCertifier dc : certifiers) {
						runStrict(p, dc, time, s);
					}
				}
			}
		}
	}

	private static void runStrict(Patient p, ThresholdCertifier dc, int time, int scenario) {
		int steps = 0;
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (p.getLevelCert() == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpCoorectAnswNotCert[scenario]);
			else {
				if (dc.getCurrentThreshold() >= p.getLevelCert())
					solution = getSolutionRandom(Solution.RIGHT, probExpCorrectAnswL_GE_T[scenario]);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpCorrectAnswL_LT_T[scenario]);
			}
			dc.computeNextThreshold(solution);
			steps++;
		} while (dc.getCurrentStatus().currentResult.equals(CONTINUE));
		// print the results
		// patientID, test type, target,
		String testType = dc.getClass().getSimpleName().replaceAll("DepthCertifier", "");
		// data ininput
		System.out.printf("%d, %s,%s,%s,%d,", scenario, p.getId(), p.getLevelCert(), testType, time);
		// test results
		System.out.printf("   %d,%d,%s\n", steps, dc.getCurrentThreshold(), dc.getCurrentStatus().currentResult);
	}

	/**
	 * Generate a random answer between RIGHT and WRONG
	 */
	static Solution getSolutionRandom(Solution desiredSol, double d) {
		Random r = new Random();
		double rand = r.nextDouble();
		if (rand < d) {
			return desiredSol;
		} else {
			if (desiredSol.equals(Solution.RIGHT))
				return Solution.WRONG;
			else
				return Solution.RIGHT;
		}

	}
}
