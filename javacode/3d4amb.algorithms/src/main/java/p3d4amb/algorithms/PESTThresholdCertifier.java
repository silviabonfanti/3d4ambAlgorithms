package p3d4amb.algorithms;


import org.apache.log4j.Logger;

/**
 * PEST Algorithm
 */

public class PESTThresholdCertifier extends ThresholdCertifier {
	private int nextDepth;
	private int maxDepth;
	private int limitL;
	private int limitR;
	private int chance;
	private double value;

	// Logger
	private final Logger logger = Logger.getLogger(PESTThresholdCertifier.class);

	public PESTThresholdCertifier(int initThreshold) {
		super(initThreshold,initThreshold,RIGHT_ANSWERS_TO_CERTIFY);
		maxDepth = initThreshold;
		limitL = initThreshold;
		limitR = 1;
		chance = 1;
	}


	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 */
	public PESTThresholdCertifier(int initThreshold, int maxThreshold, int targetThreshold) {
		super(initThreshold, maxThreshold, 1);
		this.maxThreshold = maxThreshold;
		limitL = initThreshold;
		limitR = targetThreshold;
	}


	@Override
	public void computeNextThreshold(ThresholdCertifier.Solution solution) {
		logger.debug("Compute next depth");
		if (solution == ThresholdCertifier.Solution.WRONG) {
			if (chance > 0 && certifierStatus.currentThreshold == maxDepth) {
				chance--;
			} else if (chance == 0 && certifierStatus.currentThreshold == maxDepth) {
				certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
			} else {
				limitR = certifierStatus.currentThreshold;

				// Numerical rounding (Ceil: round up)
				value = ((double) limitL + limitR) / 2;
				nextDepth = (int) (Math.ceil(value));

				// Next depth
				certifierStatus.currentThreshold = nextDepth;

				if ((limitL - limitR) == 1) {
					certifierStatus.currentResult = Result.FINISH_CERTIFIED;
					certifierStatus.currentThreshold = limitL;
				}
			}

		} else if (solution == ThresholdCertifier.Solution.NULL){
			// Nothing (Skip button)
		} else if (solution == ThresholdCertifier.Solution.RIGHT) {
			limitL = certifierStatus.currentThreshold;

			// Numerical rounding (Floor: round down)v
			value = ((double) limitL + limitR) / 2;
			nextDepth = (int) (Math.floor(value));

			// Next depth
			certifierStatus.currentThreshold = nextDepth;

			if ((limitL - limitR) == 1) {
				certifierStatus.currentResult = Result.FINISH_CERTIFIED;
				certifierStatus.currentThreshold = limitL;
			}
		}
	}

	@Override
	public CertifierStatus getCurrentStatus() {
		return certifierStatus;
	}

}