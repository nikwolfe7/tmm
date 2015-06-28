package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public interface TMMTrainingFactory {
  
  /**
   * Top level training apparatus. Given a series of feature files in CSV format,
   * and a class label, this will will return at TMixtureModel trained from the
   * parameters set in TMMTrainingConfig.java 
   * 
   * @param classLabel
   * @param csvMFCCFiles
   * @return
   */
  public TMixtureModel getTMixtureModelFromData(String classLabel, String... csvFiles); 

}
