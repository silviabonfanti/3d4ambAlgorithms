package p3d4amb.algorithms;


/**
 * PEST Algorithm
 */

public class PEST_NThresholdCertifier extends ThresholdCertifier {

	private int limitL;
	private int limitR;
	private int answers[];
	private int weight;
	private boolean firstPhase;

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 */
	public PEST_NThresholdCertifier(int initThreshold, int maxThreshold, int targetThreshold, int rightAnswersToCertify) {
		super(initThreshold, maxThreshold, rightAnswersToCertify);
		this.maxThreshold = maxThreshold;
		limitL = initThreshold;
		limitR = targetThreshold;
		answers = new int[maxThreshold];
		weight = 1;
		firstPhase = true;
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * targetThreshold: TARGET_THRESHOLD
	 * rightAnswersToCertify: RIGHT_ANSWERS_TO_CERTIFY
	 */
	public PEST_NThresholdCertifier(int initThreshold) {
		this(initThreshold, initThreshold, TARGET_THRESHOLD, RIGHT_ANSWERS_TO_CERTIFY);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * targetThreshold: TARGET_THRESHOLD
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 */
	public PEST_NThresholdCertifier(int initThreshold, int rightAnswersToCertify) {
		this(initThreshold, initThreshold, TARGET_THRESHOLD, rightAnswersToCertify);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 */
	public PEST_NThresholdCertifier(int initThreshold, int targetThreshold, int answersToCertify) {
		this(initThreshold, initThreshold, targetThreshold, answersToCertify);
	}

	@Override
	public void computeNextThreshold(ThresholdCertifier.Solution solution) {
		if (firstPhase && (solution != ThresholdCertifier.Solution.NULL)) {
			// PART ONE
			// Quickly and approximately identify the range in which the threshold can be
			// contained
			firstPart(solution);
		} else if (!firstPhase && (solution != ThresholdCertifier.Solution.NULL)) {
			// PART TWO
			// Focus on the lower threshold identified until it reaches
			// a +2 that certifies the level, or a -2 that shift to an easier level
			// Added weight to avoid loop like [OK | NO | OK | NO]
			secondPart(solution);
		} else {
			assert solution == ThresholdCertifier.Solution.NULL;
		}

		/*
		String vet = " [";

		for (int i = 0; i < answers.length; i++) {
			if (i != 0)
				vet = vet.concat(",");

			vet = vet.concat("" + answers[i]);
		}

		vet = vet.concat("]");*/

	}

	private void firstPart(ThresholdCertifier.Solution solution) {
		if (solution == ThresholdCertifier.Solution.WRONG && answers[limitL - 1] == 0
				&& certifierStatus.currentThreshold == maxThreshold) {
			// First wrong attempt to maxThreshold
			answers[limitL - 1] -= 1;
		} else if (solution == ThresholdCertifier.Solution.WRONG && answers[limitL - 1] == -1
				&& certifierStatus.currentThreshold == maxThreshold) {
			// Second wrong attempt to maxThreshold
			certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
		} else {
			double halfInt;
			if (solution == ThresholdCertifier.Solution.RIGHT) {
				limitL = certifierStatus.currentThreshold;
				halfInt = ((double) limitL + limitR) / 2;
				certifierStatus.currentThreshold = (int) (Math.floor(halfInt));
				answers[limitL - 1] += 1;
				if ((limitL - limitR) == 1) {
					firstPhase = false;
					certifierStatus.currentThreshold = limitR;
					// Weight because the rightLimit was wrong
					if (limitR != 1)
						weight = weight * 3;
				}
			} else if (solution == ThresholdCertifier.Solution.WRONG) {
				limitR = certifierStatus.currentThreshold;
				halfInt = ((double) limitL + limitR) / 2;
				// Numerical rounding (Ceil: round up)
				certifierStatus.currentThreshold = (int) (Math.ceil(halfInt));
				answers[limitR - 1] -= 1;
				if ((limitL - limitR) == 1) {
					firstPhase = false;
					certifierStatus.currentThreshold = limitR;
					// Weight because the rightLimit was wrong
					if (limitR != 1)
						weight = weight * 3;
				}
			}
		}
	}

	private void secondPart(ThresholdCertifier.Solution solution) {
		if (solution == ThresholdCertifier.Solution.RIGHT) {
			answers[certifierStatus.currentThreshold - 1] += 1;
		} else if (solution == ThresholdCertifier.Solution.WRONG) {
			// Subtract with weight
			answers[certifierStatus.currentThreshold - 1] -= weight;
			weight = weight * 3;
		}

		// Vector control and currentThreshold control
		if (answers[certifierStatus.currentThreshold - 1] >= rightAnswersToCertify) {
			// Reached the certification
			certifierStatus.currentResult = Result.FINISH_CERTIFIED;
		} else if ((answers[certifierStatus.currentThreshold - 1] <= -2) && (certifierStatus.currentThreshold < maxThreshold)) {
			// Shift the currentThreshold and reset weight
			weight = 1;
			certifierStatus.currentThreshold++;
		} else if ((answers[certifierStatus.currentThreshold - 1] <= -2) && (certifierStatus.currentThreshold == maxThreshold)) {
			// Returned to the initial maxThreshold without certification
			certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
		}
	}

	@Override
	public CertifierStatus getCurrentStatus() {
		return certifierStatus;
	}

}