package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TMMConstants;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public interface TMMTrainingFactory {
  
  /**
   * Top level training apparatus. Given a series of feature files in CSV format,
   * and a class label, this will will return at TMixtureModel trained from the
   * parameters set in TMMTrainingConfig.java 
   * 
   * @param enumVal
   * @param csvFiles
   * @return
   */
  public TMixtureModel getInitializedModel(TMMConstants enumVal, String... csvFiles); 
  
  /**
   * Retrieves the wrapper used in the initialization to get other
   * components created in {@link #getInitializedModel(TMMConstants, String...)}
   * 
   * @return
   */
  public KMeansClusteringWrapper getKMeansClusteringWrapper();
  
}
