package mlsp.cs.cmu.edu.tmm.training;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class MFCCWekaKMeansClusterer implements WekaKMeansClusteringWrapper{

  private int numClusters = TMMTrainingConfig.NUM_T_DISTRIBUTIONS.getIntValue();
  private int numIterations = TMMTrainingConfig.NUM_ITERATIONS.getIntValue();
  private int numInstances;
  private SimpleKMeans clusterer;
  private WekaKMeansTrainingDatasetFactory datasetFactory;
  
  public MFCCWekaKMeansClusterer(String... data) {
    try {
      System.out.println("Loading dataset...");
      datasetFactory = new MFCCWekaKMeansTrainingDatasetFactory();
      Instances dataset = datasetFactory.getDataset(data);
      System.out.println("Dataset loaded. Running k-Means with " + numClusters + " clusters for "
              + numIterations + " iterations...");
      clusterer = new SimpleKMeans();
      clusterer.setSeed(10);
      clusterer.setNumClusters(numClusters);
      clusterer.setMaxIterations(numIterations);
      clusterer.setPreserveInstancesOrder(true);
      double start = System.currentTimeMillis();
      clusterer.buildClusterer(dataset);
      double end = System.currentTimeMillis();
      double executionTime = Math.abs(start - end);
      System.out.println("Done with clustering! Clustering completed in: " + executionTime + "ms");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int[] getVectorAssignments() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double[][] getKMeans() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double[][] getKVariances() {
    // TODO Auto-generated method stub
    return null;
  }
  
  public static void main(String[] args) {
    String file1 = "./features/expanded_mfccs_26_dim.csv";
    String[] data = new String[200];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    WekaKMeansClusteringWrapper clusterer = new MFCCWekaKMeansClusterer(data);
    System.out.println("kmeans:" + clusterer.getKMeans());
  }

}
