package p3d4amb.algorithms;

import static p3d4amb.algorithms.ThresholdCertifier.*;
import static p3d4amb.algorithms.ThresholdCertifier.Result.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import p3d4amb.algorithms.ThresholdCertifier.Solution;

/**
 * The Class PestDepthCertifierTest.
 */
public class PestThresholdCertifierTestWrong extends ThresholdCertifierTest{

	/**
	 * Test scenario all right to1.
	 */
	@Test
	public void testScenarioAllRightTo1() {
		PestThresholdCertifier dp = new PestThresholdCertifier(5);
		assertEquals(5, dp.getCurrentThreshold());
		checkGoTo(dp, Solution.RIGHT, 4, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 3, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// assertEquals(0, dp.guessesInTarget(Solution.RIGHT));
		// giusta la prima volta
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 1, ThresholdCertifier.Result.FINISH_CERTIFIED);
	}

	/**
	 * Test scenario one mistake reaching target.
	 */
	@Test
	public void testScenarioOneMistakeReachingTarget() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		assertEquals(3, dp.getCurrentThreshold());
		// 1 wrong is tolerated
		checkGoTo(dp, Solution.WRONG, 3, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// assertEquals(0, dp.guessesInTarget(Solution.RIGHT));
		// giusta 1
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// giusta 3
		checkGoTo(dp, Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario one mistake intarget.
	 */
	@Test
	public void testScenarioOneMistakeIntarget() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		assertEquals(3, dp.getCurrentThreshold());
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// giusta 1
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// first error
		checkGoTo(dp, Solution.WRONG, 1, FINISH_CERTIFIED);
		// giusta 3
		// checkGoto(dp, Solution.RIGHT, 1, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario certified but notconsecutive.
	 */
	@Test
	public void testScenarioCertifiedButNotconsecutive() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// first mistake
		checkGoTo(dp, Solution.WRONG, 1, CONTINUE);
		// now error again, goto 2
		checkGoTo(dp, Solution.WRONG, 2, CONTINUE);
		// right at 2 -> CERTIFIED
		checkGoTo(dp, Solution.RIGHT, 2, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario double mistake notconsecutive.
	 */
	@Test
	public void testScenarioDoubleMistakeNotconsecutive() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		// first error at 2
		checkGoTo(dp, Solution.WRONG, 2, CONTINUE);
		// rught the second one
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		// first mistake
		checkGoTo(dp, Solution.WRONG, 1, CONTINUE);
		// now error again goto 2
		// checkGoto(dp, Solution.WRONG, 2, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 1, FINISH_CERTIFIED);
		// in target
		// assertEquals(1, dp.guessesInTarget(Solution.RIGHT));
		// assertEquals(1, dp.guessesInTarget(Solution.WRONG));
		// right at 2
		// checkGoto(dp, Solution.RIGHT, 2, FINISH_CERTIFIED);
		// certifica il 2
	}

	/**
	 * Test scenario immeditely not certified.
	 */
	@Test
	public void testScenarioMistakesAtMaxDepth() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		assertEquals(3, dp.getCurrentThreshold());
		// first error at 3
		checkGoTo(dp, Solution.WRONG, 3, CONTINUE);
		// second error at 3
		checkGoTo(dp, Solution.WRONG, 3, FINISH_NOT_CERTIFIED);
	}

	/**
	 * Test scenario several mistakes.
	 */
	@Test
	public void testScenario2MistakesAtMaxLevel() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		assertEquals(3, dp.getCurrentThreshold());
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 2, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 3, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 3, FINISH_NOT_CERTIFIED);
	}

	@Test
	public void testScenario1MistakesAtMaxLevel() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		assertEquals(3, dp.getCurrentThreshold());
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 2, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 3, CONTINUE);
		checkGoTo(dp, Solution.RIGHT, 3, FINISH_CERTIFIED);
	}

	/**
	 * Test scenario some mistakes increase target.
	 */
	@Test
	public void testScenarioSomeMistakesIncreaseTarget() {
		PestThresholdCertifier dp = new PestThresholdCertifier(6);
		// rigth at 6 -> 5
		checkGoTo(dp, Solution.RIGHT, 5, CONTINUE);
		// error at 5 -> 5
		checkGoTo(dp, Solution.WRONG, 5, CONTINUE);
		// right at 5
		checkGoTo(dp, Solution.RIGHT, 4, CONTINUE);
		// error in 4
		checkGoTo(dp, Solution.WRONG, 4, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 4, FINISH_CERTIFIED);
		// now try to certify 5
		// giusta 2 (la prima in discea)
		// checkGoto(dp, Solution.RIGHT, 5, FINISH_CERTIFIED);
	}

	@Test
	public void testScenarioTwoErrorsSameLevel() {
		PestThresholdCertifier dp = new PestThresholdCertifier(3);
		checkGoTo(dp, Solution.RIGHT, 2, CONTINUE);
		//
		checkGoTo(dp, Solution.RIGHT, 1, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 1, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 2, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 2, FINISH_CERTIFIED);
		// certifica con media tra due e 3
	}

	/**
	 * Test scenario not certified.
	 */
	@Test
	public void testScenarioNotCertified() {
		PestThresholdCertifier dp = new PestThresholdCertifier(6);
		// rigth at 6 -> 5
		checkGoTo(dp, Solution.RIGHT, 5, CONTINUE);
		// error at 5 -> 5
		checkGoTo(dp, Solution.WRONG, 5, CONTINUE);
		// error at 5 -> 6
		checkGoTo(dp, Solution.WRONG, 6, CONTINUE);
		checkGoTo(dp, Solution.WRONG, 6, FINISH_NOT_CERTIFIED);
	}

	/**
	 */
	@Test
	public void testScenarioWrongFromBeginning() {
		PestThresholdCertifier dp = new PestThresholdCertifier(6);
		// wrong at 6 -> 6
		checkGoTo(dp, Solution.WRONG, 6, CONTINUE);
		// wrong at 6 -> 6
		checkGoTo(dp, Solution.WRONG, 6, FINISH_NOT_CERTIFIED);
		// ATTENZIONE INDEX OUT OF BOUND EXCEPTION
		checkGoTo(dp, Solution.WRONG, 6, CONTINUE);

	}
}
