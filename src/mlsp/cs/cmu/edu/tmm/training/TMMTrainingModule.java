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

    int numIteration = 0;
    System.out.println("START EM TRAINING...");
    while(numIteration++ <= TMMTrainingConfig.EM_MAX_ITERATIONS.getIntValue()) {
      System.out.println("Iteration " + numIteration + "...");
      double[][] meanNew = new double[K][D];
      double[][] varNew = new double[K][D];
      double[] mixtureWeightsNew = new double[K];
      double[] etaConstant = new double[K];
      double[] sumWeights = new double[K];
      
      
      
      
      
      
      
      
      
    }
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
    String[] data = new String[200];
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
