package p3d4amb.algorithms.comparator;

public class ElementsType {


	private int idPatient;
	private int targetLevel;
	PestSim pest1, pest2;
	Pest3Sim pestNew1, pestNew2;
	Best3DSim best3d1, best3d2;
	StrictSim strict1, strict2;


	public ElementsType() {
		// TODO Auto-generated constructor stub
	}

	/*public ElementsType(int idPatient, int stepPest, int stepPestNew, int stepBest3D, int stepStrict, int targetLevel, int levelPest, int levelPestNew, int levelBest3D, int levelStrict, String resPest, String resPestNew, String resBest3D, String resStrict) {
		this.idPatient=idPatient;
		this.stepPest=stepPest;
		this.stepPestNew=stepPestNew;
		this.stepBest3D=stepBest3D;
		this.stepStrict=stepStrict;
		this.targetLevel=targetLevel;
		this.levelPest=levelPest;
		this.levelPestNew=levelPestNew;
		this.levelBest3D=levelBest3D;
		this.levelStrict=levelStrict;
		this.resPest=resPest;
		this.resPestNew=resPestNew;
		this.resBest3D=resBest3D;
		this.resStrict=resStrict;
	}*/

	public ElementsType(int idPatient, int targetLevel, PestSim pest1, PestSim pest2, Pest3Sim pestNew1, Pest3Sim pestNew2,
			Best3DSim best3d1, Best3DSim best3d2, StrictSim strict1, StrictSim strict2) {
		this.idPatient=idPatient;
		this.targetLevel=targetLevel;
		this.pest1=pest1;
		this.pest2=pest2;
		this.pestNew1=pestNew1;
		this.pestNew2=pestNew2;
		this.best3d1=best3d1;
		this.best3d2=best3d2;
		this.strict1=strict1;
		this.strict2=strict2;

	}

	public ElementsType(int idPatient, int targetLevel, PestSim pest1, Pest3Sim pestNew1, Best3DSim best3d1,
			StrictSim strict1) {
		this.idPatient=idPatient;
		this.targetLevel=targetLevel;
		this.pest1=pest1;
		this.pestNew1=pestNew1;
		this.best3d1=best3d1;
		this.strict1=strict1;
	}

	public ElementsType(int idPatient, int targetLevel, Pest3Sim pestNew1, Pest3Sim pestNew2, Best3DSim best3d1,
			Best3DSim best3d2, StrictSim strict1, StrictSim strict2) {
		this.idPatient=idPatient;
		this.targetLevel=targetLevel;
		this.pestNew1=pestNew1;
		this.pestNew2=pestNew2;
		this.best3d1=best3d1;
		this.best3d2=best3d2;
		this.strict1=strict1;
		this.strict2=strict2;
	}

	public PestSim getPest1() {
		return pest1;
	}

	public PestSim getPest2() {
		return pest2;
	}

	public Pest3Sim getPestNew1() {
		return pestNew1;
	}

	public Pest3Sim getPestNew2() {
		return pestNew2;
	}

	public Best3DSim getBest3d1() {
		return best3d1;
	}

	public Best3DSim getBest3d2() {
		return best3d2;
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