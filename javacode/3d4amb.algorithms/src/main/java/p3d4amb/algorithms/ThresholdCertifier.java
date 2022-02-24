package p3d4amb.algorithms;


import org.apache.log4j.Logger;

public abstract class ThresholdCertifier {

	/** The Constant RIGHT_ANSWERS_TO_CERTIFY.
	 * Default value for number of right answers to certify*/
	static final int RIGHT_ANSWERS_TO_CERTIFY = 2;

	/** The Constant WRONG_ANSWERS_TO_STOP.
	 * Default value of wrong answers at the same threshold to stop the certification*/
	static final int WRONG_ANSWERS_TO_STOP = 2;

	/** The Constant TARGET_THRESHOLD. */
	static final int TARGET_THRESHOLD = 1;

	/** Number of right answers to certify */
	int rightAnswersToCertify;

	/**
	 * maxThreshold - maximum threshold
	 */
	int maxThreshold;

	/**
	 * The Enum Solution.
	 */
	public enum Solution {
		/** The right. */
		RIGHT,
		/** The wrong. */
		WRONG,
		/** The null. */
		NULL
	}

	/**
	 * The Enum Result of the session at the end.
	 */
	public enum Result {
		/** The continue. */
		CONTINUE,
		/** The finish certified. */
		FINISH_CERTIFIED,
		/** The finish not certified.
		 * NO threshold could be certified*/
		FINISH_NOT_CERTIFIED
	}

	/**
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 */
	public ThresholdCertifier(int initThreshold, int maxThreshold, int rightAnswersToCertify) {
		this.rightAnswersToCertify = rightAnswersToCertify;
		certifierStatus = new CertifierStatus();
		certifierStatus.currentResult = Result.CONTINUE;
		if (initThreshold <= maxThreshold && maxThreshold >= 1 && initThreshold >= 1)
			certifierStatus.currentThreshold = initThreshold;
		else
			throw new IllegalArgumentException("The minimum threshold certifiable is 1. Starting threshold must be equal or less than the maximum threshold");
	}


	/** The certifier status. */
	protected CertifierStatus certifierStatus;

	/**
	 * The Class CertifierStatus.
	 */
	static public class CertifierStatus {

		/** The current threshold. */
		public int currentThreshold;

		/** The current result. */
		public Result currentResult;

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			switch (currentResult) {
			case FINISH_CERTIFIED:
				return "CERTIFIED at threshold: " + currentThreshold;
			case FINISH_NOT_CERTIFIED:
				return "FINISH BUT NOT CERTIFIED up to threshold: " + currentThreshold;
			case CONTINUE:
				return "INCONCLUSIVE (certifing " + currentThreshold + " threshold)";
			}
			return "";
		}
	}

	/**
	 * Gets the current threshold.
	 *
	 * @return the current threshold
	 */
	public int getCurrentThreshold() {
		return certifierStatus.currentThreshold;
	}

	/**
	 * Compute the next threshold.
	 *
	 * @param solution
	 */
	public abstract void computeNextThreshold(Solution sol);

	/**
	 *
	 * @return
	 */
	abstract public CertifierStatus getCurrentStatus();

	/**
	 * The Class AnswersStore.
	 */
	class AnswersStore {

		/** The right answers for certification. */
		private int[] rightAnswers;

		/** The wrong answers. */
		private int[] wrongAnswers;

		/** Numebr of right answers to certify */
		int rightAnswersToCertify;

		Logger logger;

		/**
		 * Instantiates a new answers store.
		 *
		 * @param initThreshold:  start from
		 */
		AnswersStore(int initThreshold, int rightAnswersToCertify, Logger logger) {
			this.rightAnswersToCertify = rightAnswersToCertify;
			this.logger=logger;
			rightAnswers = new int[initThreshold];
			wrongAnswers = new int[initThreshold];
		}

		/**
		 * Register.
		 *
		 * @param threshold
		 * @param solution
		 */
		void register(int threshold, ThresholdCertifier.Solution solution) {
			logger.debug("registering " + solution + " at threshold " + threshold);
			assert threshold > 0 && threshold <= rightAnswers.length;
			assert solution != ThresholdCertifier.Solution.NULL;
			if (solution == ThresholdCertifier.Solution.RIGHT) {
				rightAnswers[threshold - 1]++;
				assert rightAnswers[threshold - 1] <= rightAnswersToCertify;
			} else {
				wrongAnswers[threshold - 1]++;
				assert wrongAnswers[threshold - 1] <=  WRONG_ANSWERS_TO_STOP;
			}

		}

		/**
		 * Gets the answers.
		 *
		 * @param threshold
		 * @param solution
		 * @return the number of answers at threshold for the specified solution
		 */
		int getAnswers(int threshold, ThresholdCertifier.Solution solution) {
			assert solution != ThresholdCertifier.Solution.NULL;
			if (solution == ThresholdCertifier.Solution.RIGHT) {
				return rightAnswers[threshold - 1];
			} else {
				return wrongAnswers[threshold - 1];
			}
		}

		/**
		 *
		 * @param threshold
		 * @return the number of total answers at threshold
		 */
		int getAnswers(int threshold) {
			return rightAnswers[threshold - 1] + wrongAnswers[threshold - 1];
		}
	}

}
