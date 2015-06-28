package mlsp.cs.cmu.edu.tmm.training;

import java.util.List;

import org.apache.commons.math3.util.Pair;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TDistribution;

public class MFCCTDistributionInitializationFactory implements TDistributionInitializationFactory {

  private KMeansClusteringWrapper kMeansWrapper = null;
  private List<Pair<MFCCVector,Integer>> vectorAssignments = null;
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
    this.variances = new double[means.length][means[0].length];
    this.etas = new double[means.length];
    double[] counts = kMeansWrapper.getVectorCountsPerCluster();
    this.vectorAssignments = kMeansWrapper.getVectorAssignments();
    /* Initialize the variances */
    for(Pair<MFCCVector,Integer> vectorAssignment : vectorAssignments) {
      for(int i = 0; i < variances.length; i++) {
        
      }
    }
    
    
    return null;
  }
  
  public static void main(String[] args) {
    String file1 = "./features/expanded_mfccs_26_dim.csv";
    String[] data = new String[1];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    KMeansClusteringWrapper kMeans =  new MFCCWekaKMeansClusterer();
    TDistributionInitializationFactory factory = new MFCCTDistributionInitializationFactory(kMeans);
    factory.getInitializedTDistributions(data);
  }

}
