package mlsp.cs.cmu.edu.tmm.training;

import optimization.Fzero_methods;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TDistribution;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public class TMMTrainingUtil {

  private static double REALLY_SMALL = TrainingConfig.REALLY_SMALL_NUMBER.getDblValue();

  public static double solveForEta(double etaConstant, TDistribution tDistribution) {
    Fzero_methods fToZero = new ZeroClass(etaConstant, tDistribution);
    double a = 1;
    /* Force f(a) to be negative */
    while(fToZero.f_to_zero(a) > 0) {
      a *= 2;
    }
    double b = a;
    /* Force f(b) to be positive */
    while(fToZero.f_to_zero(b) < 0) {
      b /= 2;
    }
    /* binary search */
    double newEta = 0;
    double tolerance = REALLY_SMALL;
    while(Math.abs(a - b) > tolerance) {
      newEta = (a + b) / 2;
      if(fToZero.f_to_zero(newEta) < 0) {
        a = newEta;
      } else {
        b = newEta;
      }
    }
    return newEta;
  }

  public static void getTrainingIteration(TrainingIteration iteration) {
    logMixtureProbability(iteration);
  }

  /**
   * log mixture probability for an individual student-t mixture model with respect to a single MFCC
   * vector
   * 
   * @param vec
   * @param tMixtureModel
   * @return
   */
  private static void logMixtureProbability(TrainingIteration iteration) {
    /* TMixtureModel and MFCCVector */
    TMixtureModel tMixtureModel = iteration.getTMM();
    /* mixture components */
    int K = tMixtureModel.getNumComponents();
    /* keep track of the max */
    double maxLogP = Double.NEGATIVE_INFINITY;
    /* log probabilities */
    double logProb = 0;
    double[] logP = new double[K];
    /* cacluate log probs for vector */
    /* u -- who knows what it is??? */
    iteration.setUVec(new double[K]);
    for (int i = 0; i < K; i++) {
      logProb = logProbability(iteration, i);
      logP[i] = logProb;
      logP[i] += tMixtureModel.getLogMixtureWeight(i);
      /* keep track of the max */
      maxLogP = Math.max(maxLogP, logP[i]);
    }
    /* calc total probability */
    double totalProbability = 0;
    for (int i = 0; i < K; i++) {
      logP[i] = Math.exp(logP[i] - maxLogP);
      totalProbability += logP[i];
    }
    iteration.setPosterior(new double[K]);
    for (int i = 0; i < K; i++) {
      iteration.setPosterior(i, logP[i] / totalProbability);
    }
    /* log prob is also returned */
    logProb = maxLogP + Math.log(totalProbability);
    iteration.setLogProbability(logProb);
  }

  /**
   * log probability for a single MFCC vector with respect to a single student-t distribution
   * 
   * @param vec
   * @param pdf
   * @return
   */
  private static double logProbability(TrainingIteration iteration, int index) {
    /* TMixtureModel and MFCCVector */
    MFCCVector vec = iteration.getMFCC();
    TMixtureModel tMixtureModel = iteration.getTMM();
    /* TDistribution */
    TDistribution pdf = tMixtureModel.getTDistribution(index);
    /* vector dimension */
    int D = vec.getDimensionality();
    /* return probability */
    double logProb = pdf.getLogScalingConstant();
    /*
     * Had no idea what this was until now because Bhiksha said to ignore his slides that day :-)
     */
    double mahalanobisDistance = 0;
    for (int i = 0; i < D; i++) {
      double diff = vec.getCoefficient(i) - pdf.getMean(i);
      mahalanobisDistance += diff * diff * pdf.getInverseVariance(i);
    }
    double u = 0.0;
    if (TrainingConfig.DISTRIBUTION.getIntValue() == TrainingConfig.GAUSSIAN.getIntValue()) {
      u = 1.0;
      logProb -= 0.5 * mahalanobisDistance;
    } else if (TrainingConfig.DISTRIBUTION.getIntValue() == TrainingConfig.STUDENT_T.getIntValue()) {
      u = (pdf.getEta() + pdf.getDimension()) / (pdf.getEta() + mahalanobisDistance);
      logProb -= (pdf.getEta() + D) * 0.5 * Math.log(1 + mahalanobisDistance * pdf.getInverseEta());
    }
    iteration.setUVec(index, u);
    return logProb;
  }

}
