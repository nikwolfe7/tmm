package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TDistribution;
import mlsp.cs.cmu.edu.tmm.TMMConstants;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public class MFCCTMMTrainingFactory implements TMMTrainingFactory {
  
  private TDistributionInitializationFactory factory = null;
  private KMeansClusteringWrapper kMeans = null;
  
  @Override
  public TMixtureModel getInitializedModel(TMMConstants enumVal, String... csvFiles) {
    if(kMeans == null)
      this.kMeans =  new MFCCWekaKMeansClusterer();
    this.factory = new MFCCTDistributionInitializationFactory(kMeans);
    TDistribution[] tDists = factory.getInitializedTDistributions(csvFiles);
    double[] mixtureWeights = getUniformMixtureWeights(tDists.length);
    TMixtureModel TMM = new TMixtureModel(enumVal.getStringVal(), tDists, mixtureWeights);
    return TMM;
  }
  
  @Override
  public KMeansClusteringWrapper getKMeansClusteringWrapper() {
    return kMeans;
  }
  
  private double[] getUniformMixtureWeights(int size) {
    double[] uniform = new double[size];
    for(int i = 0; i < uniform.length; i++) {
      uniform[i] = 1.0/uniform.length;
    }
    return uniform;
  }
  
  public static void main(String[] args) {
    String file1 = TMMConstants.TEST_MFCC_FILE.getStringVal();
    String[] data = new String[10];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    TMMTrainingFactory factory = new MFCCTMMTrainingFactory();
    TMixtureModel goModel = factory.getInitializedModel(TMMConstants.CLASS_GO, data);
    goModel.printMixtureDistributions();
  }

  

  


}
