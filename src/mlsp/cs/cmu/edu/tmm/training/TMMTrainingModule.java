package mlsp.cs.cmu.edu.tmm.training;

import java.util.List;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TMMConstants;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

import org.apache.commons.math3.util.Pair;

public class TMMTrainingModule {

  private KMeansClusteringWrapper kMeansWrapper;

  private TMMTrainingFactory initializationFactory;

  private List<Pair<MFCCVector, Integer>> trainingData;

  private TMixtureModel mixtureModel;

  private TMMConstants enumVal;

  public TMMTrainingModule(TMMTrainingFactory initializationFactory) {
    this.initializationFactory = initializationFactory;
  }

  public void train(TMMConstants enumVal, String... csvFiles) {
    this.enumVal = enumVal;
    this.mixtureModel = initializationFactory.getInitializedModel(enumVal, csvFiles);
    this.kMeansWrapper = initializationFactory.getKMeansClusteringWrapper();
    this.trainingData = kMeansWrapper.getVectorAssignments();
    int K = kMeansWrapper.getKMeans().length;
    int D = kMeansWrapper.getKMeans()[0].length; // crash!
    int T = trainingData.size();

    int numIteration = 0;
    System.out.println("START EM TRAINING...");
    while(numIteration <= TMMTrainingConfig.EM_MAX_ITERATIONS.getIntValue()) {
      System.out.print("Iteration " + numIteration + "... ");
      numIteration++;
      double[][] meanNew = new double[K][D];
      double[][] varNew = new double[K][D];
      double[] mixtureWeightsNew = new double[K];
      double[] etaConstants = new double[K];
      double[] sumWeights = new double[K];
      
      double totalLogProbability = Double.NEGATIVE_INFINITY;
      TrainingIteration iteration = new TrainingIteration();
      iteration.setTMM(mixtureModel);
      for(Pair<MFCCVector, Integer> vector : trainingData) {
        /* MFCC vector */
        MFCCVector xt = vector.getFirst();
        iteration.setMFCC(xt);
        
        /* Calcuate posteriors and sufficient statistics */
        TMMTrainingUtil.getTrainingIteration(iteration);
        double logProb = iteration.getLogProbability();
        double[] posterior = iteration.getPosterior();
        double[] uVec = iteration.getUVec();
        
        /* Update means and variances */
        totalLogProbability += logProb;
        double[] weight = new double[K];
        for(int n = 0; n < K; n++) {
          /* weights */
          weight[n] = posterior[n] * uVec[n];
          /* eta constants */
          etaConstants[n] = posterior[n] * (Math.log(uVec[n]) - uVec[n]);
          /* update new mixture weights */
          mixtureWeightsNew[n] += posterior[n];
          /* sum the weights */
          sumWeights[n] += weight[n];
          /* update the mean and variance */
          for(int t = 0; t < xt.getDimensionality(); t++) {
            meanNew[n][t] = weight[n] * xt.getCoefficient(t);
            /* THIS IS A HACK TO MAKE THINGS FASTER */
            double oldMean = mixtureModel.getTDistribution(n).getMean(t);
            varNew[n][t] += weight[n] * Math.pow((xt.getCoefficient(t) - oldMean),2);
          }
        }
        
        /* update weights, variances and eta values */
        for(int n = 0; n < K; n++) {
          for(int t = 0; t < xt.getDimensionality(); t++) {
            meanNew[n][t] = meanNew[n][t] / sumWeights[n];
            varNew[n][t] = varNew[n][t] / sumWeights[n];
          }
          /* update means and variances in the TDistribution */
          mixtureModel.getTDistribution(n).setMean(meanNew[n]);
          mixtureModel.getTDistribution(n).setVariance(varNew[n]);
          /* eta constants */
          etaConstants[n] /= mixtureWeightsNew[n];
          /* mixture weights */
          mixtureModel.setMixtureWeights(n, mixtureWeightsNew[n] / T);
          double newEta = TMMTrainingUtil.solveForEta(etaConstants[n], mixtureModel.getTDistribution(n));
          mixtureModel.getTDistribution(n).setEta(newEta);
        }
      }
      System.out.println("Total Log Probability: " + totalLogProbability);
    }
    System.out.println("Training finished!");
    /* Training complete! */
    printModelToFile();
  }
  
  
  
  private void printModelToFile() {
    TMMWriter tmmWriter = new TMMWriter(mixtureModel, enumVal);
    System.out.println("Now printing model for " + enumVal.getStringVal() + " to file: "
            + enumVal.getFileName() + "...");
    tmmWriter.writeModelToFile();
    System.out.println("Done!");
  }

  public static void main(String[] args) {
    String file1 = TMMConstants.TEST_MFCC_FILE.getStringVal();
    String[] data = new String[10];
    for (int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    TMMTrainingFactory initializationFactory = new MFCCTMMTrainingFactory();
    TMMTrainingModule trainingModule = new TMMTrainingModule(initializationFactory);
    trainingModule.train(TMMConstants.CLASS_GO, data);
    trainingModule.train(TMMConstants.CLASS_JUMP, data);
    trainingModule.train(TMMConstants.CLASS_BACKGROUND, data);
  }

}
