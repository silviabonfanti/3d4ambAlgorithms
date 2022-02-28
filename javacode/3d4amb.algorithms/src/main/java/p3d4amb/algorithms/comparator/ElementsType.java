package p3d4amb.algorithms.comparator;

public class ElementsType {


	private int idPatient;
	private int targetLevel;
	PestSim pest1, pest2;
	PestNSim pestN1, pestN2;
	BestNSim bestN1, bestN2;
	StrictSim strict1, strict2;


	public ElementsType() {
	}

	public ElementsType(int idPatient, int targetLevel, PestSim pest1, PestSim pest2, PestNSim pestN1, PestNSim pestN2,
			BestNSim bestN1, BestNSim bestN2, StrictSim strict1, StrictSim strict2) {
		this.idPatient=idPatient;
		this.targetLevel=targetLevel;
		this.pest1=pest1;
		this.pest2=pest2;
		this.pestN1=pestN1;
		this.pestN2=pestN2;
		this.bestN1=bestN1;
		this.bestN2=bestN2;
		this.strict1=strict1;
		this.strict2=strict2;

	}

	public ElementsType(int idPatient, int targetLevel, PestSim pest1, PestNSim pestN1, BestNSim bestN1,
			StrictSim strict1) {
		this.idPatient=idPatient;
		this.targetLevel=targetLevel;
		this.pest1=pest1;
		this.pestN1=pestN1;
		this.bestN1=bestN1;
		this.strict1=strict1;
	}

	public ElementsType(int idPatient, int targetLevel, PestNSim pestN1, PestNSim pestN2, BestNSim bestN1,
			BestNSim bestN2, StrictSim strict1, StrictSim strict2) {
		this.idPatient=idPatient;
		this.targetLevel=targetLevel;
		this.pestN1=pestN1;
		this.pestN2=pestN2;
		this.bestN1=bestN1;
		this.bestN2=bestN2;
		this.strict1=strict1;
		this.strict2=strict2;
	}

	public PestSim getPest1() {
		return pest1;
	}

	public PestSim getPest2() {
		return pest2;
	}

	public PestNSim getPestN1() {
		return pestN1;
	}

	public PestNSim getPestN2() {
		return pestN2;
	}

	public BestNSim getBestN1() {
		return bestN1;
	}

	public BestNSim getBestN2() {
		return bestN2;
	}

	public StrictSim getStrict1() {
		return strict1;
	}

	public StrictSim getStrict2() {
		return strict2;
	}

	public int getIdPatient() {
		return idPatient;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

}