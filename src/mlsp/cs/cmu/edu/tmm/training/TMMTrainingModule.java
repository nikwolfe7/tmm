package mlsp.cs.cmu.edu.tmm.training;

import java.util.List;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TMMConstants;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

import org.apache.commons.math3.util.Pair;

public class TMMTrainingModule {
  
  private KMeansClusteringWrapper kMeansWrapper;
  private TMMTrainingFactory initializationFactory;
  private List<Pair<MFCCVector,Integer>> trainingData;
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
    printModelToFile();
  }
  
  private void printModelToFile() {
    TMMWriter tmmWriter = new TMMWriter(mixtureModel, enumVal);
    tmmWriter.writeModelToFile();
  }
  
  public static void main(String[] args) {
    String file1 = TMMConstants.TEST_MFCC_FILE.getStringVal();
    String[] data = new String[10];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    TMMTrainingFactory initializationFactory = new MFCCTMMTrainingFactory(); 
    TMMTrainingModule trainingModule = new TMMTrainingModule(initializationFactory);
    trainingModule.train(TMMConstants.CLASS_GO, data);
  }
  
  

}
