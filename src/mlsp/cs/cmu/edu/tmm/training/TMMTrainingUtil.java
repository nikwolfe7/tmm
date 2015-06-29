package mlsp.cs.cmu.edu.tmm.training;

import org.apache.commons.math3.special.Gamma;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TDistribution;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public class TMMTrainingUtil {
  
  public static double solveForEta(double etaConstant, TDistribution tDistribution) {
    double stepValue = 0.01;
    double loopMax = 500;
    double eta = tDistribution.getEta();
    double dim = tDistribution.getDimension();
    double etaPlusDimDivideByTwo = (eta + dim)/2.0;
    etaConstant = 1 + etaConstant + Gamma.digamma(etaPlusDimDivideByTwo) - Math.log(etaPlusDimDivideByTwo);
    double smallestTheta = 0; 
    double minCost = etaCost(smallestTheta, etaConstant);
    double aeta = 0;
    while(aeta <= loopMax) {
      double newTheta = etaCost(aeta, etaConstant);
      if(newTheta < minCost) {
        minCost = newTheta;
        smallestTheta = aeta;
      }
      aeta += stepValue;
    }
    return smallestTheta;
  }
  
  private static double etaCost(double eta, double etaConstant) {
    return Math.abs(Math.log(eta/2) - Gamma.digamma(eta/2) + etaConstant);
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
      logP[i] = logProbability(iteration, i);
      double tmmLogPrior = tMixtureModel.getLogMixtureWeight(i);
      logP[i] += tmmLogPrior;
      /* keep track of the max */
      maxLogP = Math.max(maxLogP, logP[i]);
    }
    /* calc total probability */
    double totalProbability = 0;
    double[] prob = new double[K];
    for (int i = 0; i < K; i++) {
      prob[i] = Math.exp(logP[i] - maxLogP);
      totalProbability += prob[i];
    }
    iteration.setPosterior(new double[K]);
    for (int i = 0; i < K; i++) {
      iteration.setPosterior(i, prob[i] / totalProbability);
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
     * Had no idea what this was until now because 
     * Bhiksha said to ignore his slides that day :-)
     */
    double mahalanobisDistance = 0;
    for (int i = 0; i < D; i++) {
      double diff = vec.getCoefficient(i) - pdf.getMean(i);
      mahalanobisDistance += diff * diff * pdf.getInverseVariance(i);
    }
    double u = (pdf.getEta() + pdf.getDimension()) / (pdf.getEta() + mahalanobisDistance);
    iteration.setUVec(index, u);
    logProb -= (pdf.getEta() + D) * 0.5 * Math.log(1 + mahalanobisDistance * pdf.getInverseEta());
    return logProb;
  }

}