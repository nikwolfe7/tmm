package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TDistribution;
import mlsp.cs.cmu.edu.tmm.TMMConstants;

public class MFCCTDistributionInitializationFactory implements TDistributionInitializationFactory {

  private KMeansClusteringWrapper kMeansWrapper = null;
  private double[][] means = null;
  private double[][] variances = null;
  private double[] etas = null;
  
  public MFCCTDistributionInitializationFactory(KMeansClusteringWrapper kMeansWrapper) {
    this.kMeansWrapper = kMeansWrapper;
  }

  @Override
  public TDistribution[] getInitializedTDistributions(String... csvFilenames) {
    /* Run K-Means */
    kMeansWrapper.initialize(csvFilenames);
    /* Initialize components */
    this.means = kMeansWrapper.getKMeans();
    this.variances = kMeansWrapper.getKVariances();
    this.etas = new double[means.length];
    for(int i = 0; i < etas.length; i++) {
      etas[i] = TMMTrainingConfig.ETA_INITIALIZE.getDblValue();
    }
    TDistribution[] distributions = new TDistribution[means.length];
    for(int i = 0; i < distributions.length; i++) {
      distributions[i] = new TDistribution(means[i].length, means[i], variances[i], etas[i]);
    }
    return distributions;
  }
  
  public static void main(String[] args) {
    String file1 = TMMConstants.TEST_MFCC_FILE.getStringVal();
    String[] data = new String[1];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    KMeansClusteringWrapper kMeans =  new MFCCWekaKMeansClusterer();
    TDistributionInitializationFactory factory = new MFCCTDistributionInitializationFactory(kMeans);
    factory.getInitializedTDistributions(data);
  }

}
