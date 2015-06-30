package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public class TrainingIteration {

	private TMixtureModel tmm;

	private MFCCVector mfcc;

	private double[] posterior;

	private double[] uVec;

	private double logProbability;

	public TrainingIteration() {
		this.logProbability = 0;
		this.tmm = null;
		this.mfcc = null;
		this.posterior = null;
		this.uVec = null;
	}

	public TMixtureModel getTMM() {
		return tmm;
	}

	public void setTMM(TMixtureModel tmm) {
		this.tmm = tmm;
	}

	public MFCCVector getMFCC() {
		return mfcc;
	}

	public void setMFCC(MFCCVector mfcc) {
		this.mfcc = mfcc;
	}

	public double[] getPosterior() {
		return posterior;
	}

	public void setPosterior(double[] posterior) {
		this.posterior = posterior;
	}

	public void setPosterior(int index, double value) {
		if (index < posterior.length)
			posterior[index] = value;
	}

	public double[] getUVec() {
		return uVec;
	}

	public void setUVec(double[] uVec) {
		this.uVec = uVec;
	}

	public void setUVec(int index, double value) {
		if (index < uVec.length)
			uVec[index] = value;
	}

	public double getLogProbability() {
		return logProbability;
	}

	public void setLogProbability(double logProbability) {
		this.logProbability = logProbability;
	}
}