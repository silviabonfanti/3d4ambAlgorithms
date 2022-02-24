package p3d4amb.algorithms;



import org.apache.log4j.Logger;



/**
 * The score is computed as the best 3 right guesses. It keeps asking a new
 * shape till there is still room to improve the final score.
 *
 * @author garganti
 */
public class BestNThresholdCertifier extends ThresholdCertifier {

	static final int ANSWERS_TO_CERTIFY = 2;

	/** The ansstore: register all the answers for the test */
	private AnswersStore ansStore;
	/** The logger. */
	private final Logger logger = Logger.getLogger(BestNThresholdCertifier.class);
	/** The target threshold. */
	private int targetThreshold;
	/** Number of maximum answers for each threshold */
	int answersToCertify;

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 * @param answersToCertify: number of maximum answers for each threshold
	 */
	public BestNThresholdCertifier(int initThreshold, int maxThreshold, int targetThreshold, int rightAnswersToCertify, int answersToCertify) {
		super(initThreshold, maxThreshold, rightAnswersToCertify);
		this.targetThreshold=targetThreshold;
		this.maxThreshold = maxThreshold;
		ansStore = new AnswersStore(maxThreshold, rightAnswersToCertify, logger);
		this.answersToCertify=answersToCertify;
		logger.debug("creating Best3Threshold certifier with init " + initThreshold + " and max threshold " + maxThreshold);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * targetThreshold: TARGET_THRESHOLD
	 * rightAnswersToCertify: RIGHT_ANSWERS_TO_CERTIFY
	 * answersToCertify: ANSWERS_TO_CERTIFY
	 */
	public BestNThresholdCertifier(int initThreshold) {
		this(initThreshold, initThreshold, TARGET_THRESHOLD, RIGHT_ANSWERS_TO_CERTIFY, ANSWERS_TO_CERTIFY);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * targetThreshold: TARGET_THRESHOLD
	 * rightAnswersToCertify: RIGHT_ANSWERS_TO_CERTIFY
	 * answersToCertify: ANSWERS_TO_CERTIFY
	 */
	public BestNThresholdCertifier(int initThreshold, int maxThreshold) {
		this(initThreshold, maxThreshold, TARGET_THRESHOLD, RIGHT_ANSWERS_TO_CERTIFY, ANSWERS_TO_CERTIFY);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 * @param answersToCertify: number of maximum answers for each threshold
	 */
	public BestNThresholdCertifier(int initThreshold, int targetThreshold, int rightAnswersToCertify, int answersToCertify) {
		this(initThreshold, initThreshold, targetThreshold, rightAnswersToCertify, answersToCertify);
	}

	/**
	 * Compute the next threshold.
	 *
	 * @param solution
	 */
	@Override
	public void computeNextThreshold(ThresholdCertifier.Solution solution) {
		assert solution != null;
		if (solution == ThresholdCertifier.Solution.NULL) {
			// nothing to do
		} else {
			// register current answer
			ansStore.register(certifierStatus.currentThreshold, solution);
			if (solution == ThresholdCertifier.Solution.RIGHT) {
				// check if going to target
				if (certifierStatus.currentThreshold > targetThreshold) {
					certifierStatus.currentThreshold--;
					certifierStatus.currentResult = Result.CONTINUE;
				} else {
					if (certifyRightAnsw()) {
						certifierStatus.currentResult = Result.FINISH_CERTIFIED;
					} else if (ansStore.getAnswers(certifierStatus.currentThreshold) <= 1) {
						certifierStatus.currentResult = Result.CONTINUE;
					} else {
						certifierStatus.currentResult = Result.CONTINUE;
						certifierStatus.currentThreshold++;
						targetThreshold = certifierStatus.currentThreshold;
					}
				}
			} else {
				if (ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.WRONG) == WRONG_ANSWERS_TO_STOP) {
					if (certifierStatus.currentThreshold < maxThreshold) {
						certifierStatus.currentResult = Result.CONTINUE;
						certifierStatus.currentThreshold++;
						targetThreshold = certifierStatus.currentThreshold;
					} else
						certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
				} else if (ansStore.getAnswers(certifierStatus.currentThreshold) == answersToCertify) {
					if (certifierStatus.currentThreshold < maxThreshold) {
						if (ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.RIGHT) + ansStore
								.getAnswers(certifierStatus.currentThreshold + 1, ThresholdCertifier.Solution.RIGHT) >= rightAnswersToCertify) {
							certifierStatus.currentResult = Result.FINISH_CERTIFIED;
						}else {
							certifierStatus.currentResult = Result.CONTINUE;
							certifierStatus.currentThreshold++;
							targetThreshold = certifierStatus.currentThreshold;
						}
					}else
						certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
				}

			}
		}

	}

	private boolean certifyRightAnsw() {
		if (certifierStatus.currentThreshold==1)
			return ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.RIGHT) == rightAnswersToCertify
			|| (ansStore.getAnswers(certifierStatus.currentThreshold) == answersToCertify
					&& (ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.RIGHT)
							 >= rightAnswersToCertify));
		else
			return ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.RIGHT) == rightAnswersToCertify
				|| (ansStore.getAnswers(certifierStatus.currentThreshold) == answersToCertify
						&& (ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.RIGHT)
								+ ansStore.getAnswers(certifierStatus.currentThreshold - 1,
										ThresholdCertifier.Solution.RIGHT) >= rightAnswersToCertify));
	}

	@Override
	public CertifierStatus getCurrentStatus() {
		return certifierStatus;
	}



}
