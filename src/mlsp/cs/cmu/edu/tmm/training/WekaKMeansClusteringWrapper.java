package mlsp.cs.cmu.edu.tmm.training;

import java.util.List;

import org.apache.commons.math3.util.Pair;

import mlsp.cs.cmu.edu.tmm.MFCCVector;

public interface WekaKMeansClusteringWrapper {
  
  public List<Pair<MFCCVector,Integer>> getVectorAssignments();
  
  public double[] getVectorNumbersPerCluster();
  
  public double[][] getKMeans();
  
}
