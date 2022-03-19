package p3d4amb.algorithms;


import org.apache.log4j.Logger;

/**
 * PEST Algorithm
 */

public class PestThresholdCertifier extends ThresholdCertifier {
	private int limitL;
	private int limitR;
	private int chance;
	private double value;
	private boolean certify=false;

	// Logger
	private final Logger logger = Logger.getLogger(PestThresholdCertifier.class);

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 */
	public PestThresholdCertifier(int initThreshold, int targetThreshold) {
		super(initThreshold,initThreshold,RIGHT_ANSWERS_TO_CERTIFY);
		limitL = initThreshold;
		limitR = targetThreshold;
		this.maxThreshold=initThreshold;
		chance = 1;
	}


	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 */
	public PestThresholdCertifier(int initThreshold) {
		this(initThreshold,1);
	}


	@Override
	public void computeNextThreshold(ThresholdCertifier.Solution solution) {
		if (solution == ThresholdCertifier.Solution.WRONG) {
			if (chance > 0 && certifierStatus.currentThreshold == maxThreshold) {
				chance--;
			} else if (chance == 0 && certifierStatus.currentThreshold == maxThreshold) {
				certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
			} else {
				limitR = certifierStatus.currentThreshold;
				// Numerical rounding (Ceil: round up)
				value = ((double) limitL + limitR) / 2;
				// Next threshold
				certifierStatus.currentThreshold = (int) (Math.ceil(value));

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
			// Next threshold
			certifierStatus.currentThreshold = (int) (Math.floor(value));
			
			if ((limitL - limitR) == 1) {
				certifierStatus.currentResult = Result.FINISH_CERTIFIED;
				certifierStatus.currentThreshold = limitR;
			}
		}
	}

	@Override
	public CertifierStatus getCurrentStatus() {
		return certifierStatus;
	}

}