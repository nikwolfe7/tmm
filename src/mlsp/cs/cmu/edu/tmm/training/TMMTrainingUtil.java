package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TDistribution;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public class TMMTrainingUtil {
  
    /**
     * log mixture probability for an individual student-t mixture model
     * with respect to a single MFCC vector 
     * 
     * @param vec
     * @param tMixtureModel
     * @return
     */
  public static double logMixtureProbability(MFCCVector vec, TMixtureModel tMixtureModel) {
    /* mixture components */
    int K = tMixtureModel.getNumComponents();
    /* keep track of the max */
    double maxLogP = Double.NEGATIVE_INFINITY;
    /* log probabilities */
    double logProb = 0;
    double[] logP = new double[K];
    /* cacluate log probs for vector */
    /* u -- who knows what it is??? */
    double[] u  = new double[K];
    for (int i = 0; i < K; i++) {
      TDistribution pdf = tMixtureModel.getTDistribution(i);
      double tmmLogPrior = tMixtureModel.getLogMixtureWeight(i);
      /* logP[i], u[i] = logProbability_and_u (vec, pdf) 
       * returns U and logprobability
       * U[i] comes from here, gets returned
       * */
      logP[i] = logProbability(vec, pdf);
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
    double[] posterior = new double[K];
    for (int i = 0; i < K; i++) {
      posterior[i] = prob[i] / totalProbability;
    }
    /* posteriro gets returned */
    logProb = maxLogP + Math.log(totalProbability);
    
    /* log prob is also returned */
    return logProb;
  }

    /**
     * log probability for a single MFCC vector with respect to a single student-t 
     * distribution 
     * 
     * @param vec
     * @param pdf
     * @return
     */
    private static double logProbability(MFCCVector vec, TDistribution pdf) {
      /* vector dimension */
      int D = vec.getDimensionality();
      /* return probability */
      double logProb = pdf.getLogScalingConstant();
      /* Had no idea what this was until now because Bhiksha 
       * said to ignore his slides that day :-) 
       * */
      double mahalanobisDistance = 0;
      for(int i = 0; i < D; i++) {
        double diff = vec.getCoefficient(i) - pdf.getMean(i);
        mahalanobisDistance += diff * diff * pdf.getInverseVariance(i);
      }

      //double u = (pdf.eta + pdf.dim) / (pdf.eta + mahalanobisDistance);
      
      logProb -= (pdf.getEta() + D) * 0.5 * Math.log(1 + mahalanobisDistance * pdf.getInverseEta());
      return logProb;
    }


}
