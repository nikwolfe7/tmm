package mlsp.cs.cmu.edu.tmm;

public class TMMAlphaPosterior {
  
  /**
   * Class label posteriors for the mfcc vector block
   * 
   * @param featureVector
   * @param mixtureModels
   * @return
   */
  public static double[] computeAlpha(MFCCVector[] featureVector, TMixtureModel[] mixtureModels) {
    /* return alpha and prior vectors */
    /* loop limit / array size */
    int N = mixtureModels.length;
    double[] alpha = new double[N];
    double[] priors = new double[N];
    /* uniform priors */
    for (int i = 0; i < N; i++)
      priors[i] = 1 / priors.length;
    /* iterate through feature vectors in the block */
    for (MFCCVector vec : featureVector) {
      /* calculate the posteriors */
      double[] posterior = computePosterior(vec, mixtureModels, priors);
      /* alpha = alpha + posterior */
      for (int i = 0; i < N; i++)
        alpha[i] += posterior[i];
    }
    return alpha;
  }

  /**
   * Single posterior calculation for a collection of student-t mixture 
   * models with respect to a single MFCC vector 
   * 
   * @param vec
   * @param mixtureModels
   * @param priors
   * @return
   */
  private static double[] computePosterior(MFCCVector vec, TMixtureModel[] mixtureModels,
          double[] priors) {
    /* loop limit / array size */
    int N = mixtureModels.length;
    /* keep track of the max */
    double maxLogP = Double.NEGATIVE_INFINITY;
    /* return posteriors */
    double[] posteriors = new double[N];
    /* log probabilities */
    double[] logP = new double[N];
    /* iterate through the models and get log probs */
    for (int i = 0; i < N; i++) {
      /* log probability for each TMM */
      logP[i] = Math.log(priors[i]) + logMixtureProbability(vec, mixtureModels[i]);
      /* keep track of the max */
      maxLogP = Math.max(maxLogP, logP[i]);
    }
    /* total probability */
    double totalProbability = 0;
    /* mixture probabilities */
    double[] prob = new double[N];
    for(int i = 0; i < N; i++) {
      prob[i] = Math.exp(logP[i] - maxLogP);
      totalProbability += prob[i];
    }
    /* update posteriors */
    for(int i = 0; i < N; i++) {
      posteriors[i] = prob[i] / totalProbability;
    }
    /* finished! return posteriors */
    return posteriors;
  }

  /**
   * log mixture probability for an individual student-t mixture model
   * with respect to a single MFCC vector 
   * 
   * @param vec
   * @param tMixtureModel
   * @return
   */
  private static double logMixtureProbability(MFCCVector vec, TMixtureModel tMixtureModel) {
   /* mixture components */
   int K = vec.getDimensionality();
   /* keep track of the max */
   double maxLogP = Double.NEGATIVE_INFINITY;
   /* log probabilities */
   double logProb = 0;
   double[] logP = new double[K];
   /* cacluate log probs for vector */
   for(int i = 0; i < K; i++) {
     TDistribution pdf = tMixtureModel.getTDistribution(i);
     double tmmLogPrior = tMixtureModel.getLogMixtureWeight(i);
     logP[i] = logProbability(vec, pdf) + tmmLogPrior;
     /* keep track of the max */
     maxLogP = Math.max(maxLogP, logP[i]);
   }
   /* calc total probability */
   double totalProbability = 0;
   for(int i = 0; i < K; i++) {
     totalProbability += Math.exp(logP[i] - maxLogP);
   }
   logProb = maxLogP + Math.log(totalProbability);
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
    logProb -= (pdf.getEta() + D) * 0.5 * Math.log(1 + mahalanobisDistance * pdf.getInverseEta());
    return logProb;
  }

}
