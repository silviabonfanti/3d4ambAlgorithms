package p3d4amb.algorithms.comparator;

import java.util.Random;

public class Patient {

	int levelCert;
	int id;
	static int counterpatient = 0;

	public Patient() {
		this.id = counterpatient;
		levelCert = getRandomNumberInRange(0, 10);
		// levelCert=0;
		counterpatient++;
	}

	public Patient(int startinglevel) {
		this.id = counterpatient;
		levelCert = getRandomNumberInRange(0, startinglevel);
		// levelCert=0;
		counterpatient++;
	}

	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public int getId() {
		return id;
	}

	public int getLevelCert() {
		return levelCert;
	}
}
