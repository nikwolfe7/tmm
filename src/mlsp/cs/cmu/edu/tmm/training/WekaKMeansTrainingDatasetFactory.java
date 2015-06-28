package mlsp.cs.cmu.edu.tmm.training;

import weka.core.Instances;

public interface WekaKMeansTrainingDatasetFactory {
  
  public Instances getDataset(String... csvFilenames); 

}
