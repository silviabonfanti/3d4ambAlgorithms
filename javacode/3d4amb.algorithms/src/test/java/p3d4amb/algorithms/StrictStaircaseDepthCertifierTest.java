package p3d4amb.algorithms;

import static p3d4amb.algorithms.ThresholdCertifier.*;
import static p3d4amb.algorithms.ThresholdCertifier.Result.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * The Class DepthCertifierTest.
 */
public class StrictStaircaseDepthCertifierTest extends DepthCertifierTest {

	/**
	 * Test scenario all right to1.
	 */
	@Test
	public void testScenarioAllRightTo1() {
		StrictStaircaseThresholdCertifier dp = new StrictStaircaseThresholdCertifier(5, 5);
		assertEquals(5, dp.getCurrentThreshold());
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 4, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 3, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		assertEquals(0, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		// giusta la prima volat
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// giusta 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// giusta 3
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario one mistake reaching target.
	 */
	@Test
	public void testScenarioOneMistakeReachingTarget() {
		StrictStaircaseThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 3);
		assertEquals(3, dp.getCurrentThreshold());
		// 1 wrong is tolerated
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 3, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		assertEquals(0, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		// giusta 1
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// giusta 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// giusta 3
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario one mistake intarget.
	 */
	@Test
	public void testScenarioOneMistakeIntarget() {
		ThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 3);
		assertEquals(3, dp.getCurrentThreshold());
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// giusta 1
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// giusta 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// first error
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 1, CONTINUE);
		// giusta 3
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario some null.
	 */
	@Test
	public void testScenarioSomeNull() {
		ThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 3);
		assertEquals(3, dp.getCurrentThreshold());
		checkGoTo(dp, ThresholdCertifier.Solution.NULL, 3, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.NULL, 2, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario certified but notconsecutive.
	 */
	@Test
	public void testScenarioCertifiedButNotconsecutive() {
		StrictStaircaseThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 3);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// first mistake
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 1, CONTINUE);
		// now error again, goto 2
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 2, CONTINUE);
		// in target
		assertEquals(1, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		assertEquals(0, dp.guessesInTarget(ThresholdCertifier.Solution.WRONG));
		// right at 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		// right at 2 -> CERTIFIED
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario double mistake notconsecutive.
	 */
	@Test
	public void testScenarioDoubleMistakeNotconsecutive() {
		StrictStaircaseThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 3);
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		// first error at 2
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 2, CONTINUE);
		// rught the second one
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// first mistake
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 1, CONTINUE);
		// now error again goto 2
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 2, CONTINUE);
		// in target
		assertEquals(1, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		assertEquals(1, dp.guessesInTarget(ThresholdCertifier.Solution.WRONG));
		// right at 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		// first error at 2
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 3, CONTINUE);
	}

	/**
	 * Test scenario several mistakes.
	 */
	@Test
	public void testScenarioSeveralMistakes() {
		StrictStaircaseThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 6);
		assertEquals(3, dp.getCurrentThreshold());
		// first error at 3
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 3, CONTINUE);
		// second error at 3
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 4, CONTINUE);
		// first error in 4
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 4, CONTINUE);
		// second error in 4
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 5, CONTINUE);
		// now try to certify 5
		checkGoTo(dp, ThresholdCertifier.Solution.NULL, 5, CONTINUE);
		// 5 is certified
		// giusta 1
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, CONTINUE);
		assertEquals(1, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		// giusta 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, CONTINUE);
		assertEquals(2, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		// giusta 3
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, FINISH_CERTIFIED);
		assertEquals(3, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
	}

	/**
	 * Test scenario some mistakes increase target.
	 */
	@Test
	public void testScenarioSomeMistakesIncreaseTarget() {
		StrictStaircaseThresholdCertifier dp = new StrictStaircaseThresholdCertifier(6, 6);
		// rigth at 6 -> 5
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, CONTINUE);
		// error at 5 -> 5
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 5, CONTINUE);
		// right at 5
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 4, CONTINUE);
		// error in 4
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 4, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 5, CONTINUE);
		// now try to certify 5
		// giusta 2 (la prima in discea)
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, CONTINUE);
		assertEquals(2, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
		// giusta 3
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, FINISH_CERTIFIED);
		assertEquals(3, dp.guessesInTarget(ThresholdCertifier.Solution.RIGHT));
	}

	/**
	 * Test scenario immeditely not certified.
	 */
	@Test
	public void testScenarioImmeditelyNotCertified() {
		ThresholdCertifier dp = new StrictStaircaseThresholdCertifier(6, 6);
		// error at 6 -> 6
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 6, CONTINUE);
		// error at 6 -> 6, stop not certified
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 6, FINISH_NOT_CERTIFIED);
	}

	/**
	 * Test scenario not certified.
	 */
	@Test
	public void testScenarioNotCertified() {
		ThresholdCertifier dp = new StrictStaircaseThresholdCertifier(6, 6);
		// rigth at 6 -> 5
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 5, CONTINUE);
		// error at 5 -> 5
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 5, CONTINUE);
		// error at 5 -> 6
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 6, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 6, CONTINUE);
		checkGoTo(dp, ThresholdCertifier.Solution.WRONG, 6, FINISH_NOT_CERTIFIED);
	}

	/**
	 * Test scenario certified with fewer tries.
	 */
	@Test
	public void testScenarioCertified() {
		ThresholdCertifier dp = new StrictStaircaseThresholdCertifier(3, 3, 1, 1);
		// rigth at 3 -> 2
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 2, CONTINUE);
		// right at 2 -> 1
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, CONTINUE);
		// right at 1 -> 1 + FINISHED CERTIFIED
		checkGoTo(dp, ThresholdCertifier.Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

}
