package mlsp.cs.cmu.edu.tmm.training;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import mlsp.cs.cmu.edu.tmm.MFCCVector;
import mlsp.cs.cmu.edu.tmm.TMMConstants;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

public class MFCCWekaKMeansClusterer implements KMeansClusteringWrapper {

  private int numClusters = TrainingConfig.NUM_T_DISTRIBUTIONS.getIntValue();
  private int numIterations = TrainingConfig.NUM_ITERATIONS.getIntValue();
  private WekaKMeansTrainingDatasetFactory datasetFactory = null;
  private SimpleKMeans clusterer = null;
  private Instances dataset = null;
  private double[][] kMeans = null;
  private double[][] kVariances = null;
  private double[] numVectorsPerCluster = null;
  private List<Pair<MFCCVector, Integer>> vectorAssignments = null;
  private double minVariance = TrainingConfig.REALLY_SMALL_NUMBER.getDblValue();
  
  @Override
  public void initialize(String... csvFilenames) {
    try {
      /* loading the data */
      print("Loading dataset...");
      datasetFactory = new MFCCWekaKMeansTrainingDatasetFactory();
      this.dataset = datasetFactory.getDataset(csvFilenames);
      
      /* setting the K-means paramesters */
      clusterer = new SimpleKMeans();
      clusterer.setSeed(10);
      clusterer.setNumClusters(numClusters);
      clusterer.setMaxIterations(numIterations);
      clusterer.setPreserveInstancesOrder(true);
      clusterer.setDisplayStdDevs(true);
      print("Dataset loaded. Running k-Means with " + numClusters + " clusters for "
              + numIterations + " iterations...");
      
      /* running the clustering */
      double start = System.currentTimeMillis();
      clusterer.buildClusterer(dataset);
      double end = System.currentTimeMillis();
      double executionTime = Math.abs(start - end);
      print("Done with clustering! Clustering completed in: " + executionTime + "ms");
      
      /* fixing up outputs */
      this.vectorAssignments = buildVectorAssignments();
      this.numVectorsPerCluster = buildVectorsPerClusterList();
      this.kMeans = buildCentroidMatrix();
      this.kVariances = buildVarianceMatrix();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public double[] getVectorCountsPerCluster() {
    return numVectorsPerCluster;
  }
  
  @Override
  public List<Pair<MFCCVector, Integer>> getVectorAssignments() {
   return vectorAssignments;
  }
  
  @Override
  public double[][] getKMeans() {
    return kMeans;
  }
  
  @Override
  public double[][] getKVariances() {
    return kVariances;
  }
  
  private double[][] buildVarianceMatrix() {
    Instances stdDevs = clusterer.getClusterStandardDevs();
    int K = clusterer.getNumClusters();
    int D = stdDevs.get(0).toDoubleArray().length;
    double[][] matrix = new double[K][D];
    int i = 0;
    for(Instance instance : stdDevs) {
      double[] row = instance.toDoubleArray();
      for(int j = 0; j < row.length; j++) {
        /* Std Dev is sqrt of the variance */
        matrix[i][j] = Math.max(Math.pow(row[j],2), minVariance);
      }
      i++;
    }
    return matrix;
  }

  private double[] buildVectorsPerClusterList() {
    return clusterer.getClusterSizes();
  }

  private double[][] buildCentroidMatrix() {
    int K = clusterer.getNumClusters();
    int D = vectorAssignments.get(0).getFirst().getDimensionality();
    double[][] matrix = new double[K][D];
    int i = 0; 
    for(Instance instance : clusterer.getClusterCentroids()) {
      double[] row = instance.toDoubleArray();
      for(int j = 0; j < row.length; j++) {
        matrix[i][j] = row[j];
      }
      i++;
    }
    return matrix;
  }

  private List<Pair<MFCCVector, Integer>> buildVectorAssignments() {
    List<Pair<MFCCVector, Integer>> map = new ArrayList<Pair<MFCCVector,Integer>>();
    try {
      int[] assignments = clusterer.getAssignments();
      for(int i = 0; i < assignments.length; i++) {
        Instance instance = dataset.get(i);
        MFCCVector vector = new MFCCVector(instance.toDoubleArray());
        map.add(new Pair<MFCCVector, Integer>(vector, assignments[i]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  private void print(Object s) {
    System.out.println(s.toString());
  }

  public static void main(String[] args) {
    String file1 = TMMConstants.TEST_MFCC_FILE.getStringVal();
    String[] data = new String[1];
    for(int i = 0; i < data.length; i++) {
      data[i] = file1;
    }
    KMeansClusteringWrapper wekaClusterer = new MFCCWekaKMeansClusterer();
    wekaClusterer.initialize(data);
    System.out.println("kmeans: " + wekaClusterer.getKMeans());
    System.out.println("numberspercluster: " + wekaClusterer.getVectorCountsPerCluster());
    System.out.println("assignments: " + wekaClusterer.getVectorAssignments());
  }

  

  

 

  

}
