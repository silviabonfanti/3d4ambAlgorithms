package p3d4amb.algorithms;

import static org.junit.Assert.assertEquals;

import p3d4amb.algorithms.ThresholdCertifier.Result;

public class ThresholdCertifierTest {

	/**
	 * Check goto.
	 *
	 * @param dp        the dp
	 * @param sol       the sol
	 * @param nextDepth the next depth
	 * @param result    the result
	 */
	// check that given solution it goes to
	static void checkGoTo(ThresholdCertifier dp, ThresholdCertifier.Solution sol, int nextDepth, Result result) {
		dp.computeNextThreshold(sol);
		assertEquals(nextDepth, dp.getCurrentThreshold());
		assertEquals(result, dp.getCurrentStatus().currentResult);
	}

}
