package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TDistribution;

public interface TDistributionInitializationFactory {
  
  public TDistribution[] getInitializedTDistributions(String... csvFiles);

}
