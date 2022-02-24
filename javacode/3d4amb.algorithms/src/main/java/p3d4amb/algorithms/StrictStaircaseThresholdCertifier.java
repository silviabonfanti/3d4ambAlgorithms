package p3d4amb.algorithms;



import org.apache.log4j.Logger;



/*! \page certifier Certificazione
*
* La certificazione della stero acuità si basa sul principio che un livello è certificato se una persona riesce ad indovinarne tre
* senza sbagliarne più di una (un errore è perdonato) per quel livello.
*
* Quindi l'applicazione inizialmente cerca di proporre immagini sempre più difficili. Inizialmente il target è la disparità minima ammessa dal monitor e la disparità iniziale
* è quella decisa dall'utente.
*
* In caso di risposta corretta, la depth viene decrementata a meno che sia quella il target o che sia il minimo.
* Dopo tre risposte corrette nel target, la depth viene certificata e si ha finito.
*
* In caso di duplice errore (anche non consecutivo) nel livello corrente, la depth viene incrementata e di cerca di certificare quella (target),
* a meno che non si raggiunga il massimo (per ora uguale al punto dipartenza).
*
*
*/

/**
 * Certifies a threshold Requirements: * if the solution is right, the threshold is
 * decreased (unless is already 1).
 *
 * @author garganti
 */
public class StrictStaircaseThresholdCertifier extends ThresholdCertifier{

	/** The Constant RIGHT_ANSWERS_TO_CERTIFY.
	 * Default value for number of right answers to certify*/
	static final int RIGHT_ANSWERS_TO_CERTIFY = 3;

	/** The target threshold. */
	private int targetThreshold;

	/** The ansstore: register all the answers for the test */
	private AnswersStore ansStore;

	/** The logger. */
	private final Logger logger = Logger.getLogger(StrictStaircaseThresholdCertifier.class);


	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 */
	public StrictStaircaseThresholdCertifier(int initThreshold, int maxThreshold, int targetThreshold, int rightAnswersToCertify) {
		super(initThreshold, maxThreshold, rightAnswersToCertify);
		logger.debug("creating certifier with init " + initThreshold + " and max threshold " + maxThreshold);
		this.maxThreshold = maxThreshold;
		ansStore = new AnswersStore(maxThreshold, rightAnswersToCertify, logger);
		this.targetThreshold=targetThreshold;
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * @param maxThreshold: finish maximum at
	 * targetThreshold: TARGET_THRESHOLD
	 * rightAnswersToCertify: RIGHT_ANSWERS_TO_CERTIFY
	 */
	public StrictStaircaseThresholdCertifier(int initThreshold, int maxThreshold) {
		this(initThreshold, maxThreshold, TARGET_THRESHOLD, RIGHT_ANSWERS_TO_CERTIFY);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * targetThreshold: TARGET_THRESHOLD
	 * rightAnswersToCertify: RIGHT_ANSWERS_TO_CERTIFY
	 */
	public StrictStaircaseThresholdCertifier(int initThreshold) {
		this(initThreshold, initThreshold, TARGET_THRESHOLD, RIGHT_ANSWERS_TO_CERTIFY);
	}

	/**
	 * Constructor
	 *
	 * @param initThreshold:  start from
	 * maxThreshold: = initThreshold
	 * @param targetThreshold: target threshold to be certified, 1 is the minimum value
	 * @param rightAnswersToCertify: number of right answers given at target threshold to certify it
	 */
	public StrictStaircaseThresholdCertifier(int initThreshold, int targetThreshold, int rightAnswersToCertify) {
		this(initThreshold, initThreshold, targetThreshold, rightAnswersToCertify);
	}

	/**
	 * Gets the current status.
	 *
	 * @return next threshold. Return -1 if finished
	 */
	@Override
	public CertifierStatus getCurrentStatus() {
		logger.debug("current status " + certifierStatus.currentResult + " (at threshold " + certifierStatus.currentThreshold
				+ ") target " + targetThreshold);
		assert certifierStatus.currentThreshold > 0;
		// finished certified -> target reached
		assert !(certifierStatus.currentResult == Result.FINISH_CERTIFIED)
				|| certifierStatus.currentThreshold == targetThreshold;
		// non certified / CONTINUE -> target not reached ( yet or queal to max)
		assert !(certifierStatus.currentResult == Result.CONTINUE
				|| certifierStatus.currentResult == Result.FINISH_NOT_CERTIFIED)
				|| (certifierStatus.currentThreshold >= targetThreshold || certifierStatus.currentThreshold == maxThreshold);
		// se continue, not reached
		assert !(certifierStatus.currentResult == Result.CONTINUE)
				|| (ansStore.getAnswers(targetThreshold, ThresholdCertifier.Solution.RIGHT) < rightAnswersToCertify);
		assert !(certifierStatus.currentResult == Result.CONTINUE)
				|| (ansStore.getAnswers(targetThreshold, ThresholdCertifier.Solution.WRONG) < WRONG_ANSWERS_TO_STOP);

		return certifierStatus;
	}

	/**
	 * Compute the next threshold.
	 *
	 * @param solution
	 */
	@Override
	public
	void computeNextThreshold(ThresholdCertifier.Solution solution) {
		assert solution != null;
		if (solution == ThresholdCertifier.Solution.NULL) {
			// nothing to do
		} else {
			// register current answer
			ansStore.register(certifierStatus.currentThreshold, solution);
			if (solution == ThresholdCertifier.Solution.RIGHT) {
				// check if going to target
				if (certifierStatus.currentThreshold > targetThreshold) {
					// still trying to reach target
					assert certifierStatus.currentThreshold > 0;
					certifierStatus.currentThreshold--;
					certifierStatus.currentResult = Result.CONTINUE;
				} else {
					assert certifierStatus.currentThreshold == targetThreshold;

					if (ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.RIGHT) >= rightAnswersToCertify) {
						certifierStatus.currentResult = Result.FINISH_CERTIFIED;
					} else {
						certifierStatus.currentResult = Result.CONTINUE;
					}
				}
			} else {
				assert (solution == ThresholdCertifier.Solution.WRONG);
				if (ansStore.getAnswers(certifierStatus.currentThreshold, ThresholdCertifier.Solution.WRONG) >= WRONG_ANSWERS_TO_STOP) {
					if (certifierStatus.currentThreshold < maxThreshold) {
						// go up one level
						// error and already an error in current threshold
						certifierStatus.currentThreshold++;
						targetThreshold = certifierStatus.currentThreshold;
						certifierStatus.currentResult = Result.CONTINUE;
					} else {
						// stop certifying
						certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
					}
				} else {
					certifierStatus.currentResult = Result.CONTINUE;
				}
			}
		}
	}

	/**
	 * Guesses in target threshold.
	 *
	 * @param type
	 *            the type
	 * @return the int
	 */
	public // useful for testing
	int guessesInTarget(ThresholdCertifier.Solution type) {
		return ansStore.getAnswers(targetThreshold, type);
	}



}
