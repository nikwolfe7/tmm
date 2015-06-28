package mlsp.cs.cmu.edu.tmm.training;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class MFCCWekaKMeansClusterer implements WekaKMeansClusteringWrapper {

  private int numClusters = TMMTrainingConfig.NUM_T_DISTRIBUTIONS.getIntValue();
  private int numIterations = TMMTrainingConfig.NUM_ITERATIONS.getIntValue();
  private WekaKMeansTrainingDatasetFactory datasetFactory;
  private SimpleKMeans clusterer;
  private Instances dataset;
  private double[][] kMeans;
  
  
  public MFCCWekaKMeansClusterer(String... data) {
    try {
      System.out.println("Loading dataset...");
      datasetFactory = new MFCCWekaKMeansTrainingDatasetFactory();
      this.dataset = datasetFactory.getDataset(data);
      clusterer = new SimpleKMeans();
      clusterer.setSeed(10);
      clusterer.setNumClusters(numClusters);
      clusterer.setMaxIterations(numIterations);
      clusterer.setPreserveInstancesOrder(true);
      System.out.println("Dataset loaded. Running k-Means with " + numClusters + " clusters for "
              + numIterations + " iterations...");
      double start = System.currentTimeMillis();
      clusterer.buildClusterer(dataset);
      double end = System.currentTimeMillis();
      double executionTime = Math.abs(start - end);
      System.out.println("Done with clustering! Clustering completed in: " + executionTime + "ms");
      calculateMeans(clusterer);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void calculateMeans(SimpleKMeans clusters) {
    Instances centroids = clusters.getClusterCentroids();
    
  }

  @Override
  public int[] getVectorAssignments() {
    int[] assignments = null;
    try {
      assignments = clusterer.getAssignments();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return assignments;
  }

  @Override
  public double[][] getKMeans() {
    
    
    
    
    return null;
  }

  public static void main(String[] args) {
    String file1 = "./features/expanded_mfccs_26_dim.csv";
    String[] data = new String[10];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    WekaKMeansClusteringWrapper wekaClusterer = new MFCCWekaKMeansClusterer(data);
    System.out.println("kmeans: " + wekaClusterer.getKMeans());
  }

}
