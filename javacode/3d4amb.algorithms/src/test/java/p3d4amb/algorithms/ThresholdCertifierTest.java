package p3d4amb.algorithms;

import static org.junit.Assert.*;

import org.junit.Test;

import p3d4amb.algorithms.ThresholdCertifier.Result;

public class ThresholdCertifierTest {

	class MyThresholdCertifier extends ThresholdCertifier{
		
		public MyThresholdCertifier(int initThreshold, int maxThreshold, int rightAnswersToCertify) {
			super(initThreshold, maxThreshold, rightAnswersToCertify);
		}

		@Override
		public CertifierStatus getCurrentStatus() {
			return null;
		}
		
		@Override
		public void computeNextThreshold(Solution sol) {}
	};

	
	@Test(expected = Exception.class)
	public void testThresholdCertifierE1() {
		//init negative
		MyThresholdCertifier m = new MyThresholdCertifier(-1, 10, 1);
	}
	@Test(expected = Exception.class)
	public void testThresholdCertifierE2() {
		//init greater than max
		MyThresholdCertifier m = new MyThresholdCertifier(11, 10, 1);
	}
	@Test(expected = Exception.class)
	public void testThresholdCertifierE3() {
		// step <= 0 
		MyThresholdCertifier m = new MyThresholdCertifier(5, 10, 0);
	}

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
