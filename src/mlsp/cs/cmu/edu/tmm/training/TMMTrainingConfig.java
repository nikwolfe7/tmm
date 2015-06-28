package mlsp.cs.cmu.edu.tmm.training;

public enum TMMTrainingConfig {
  
  NUM_T_DISTRIBUTIONS(30,30),
  NUM_ITERATIONS(10,10),
  CONVERGENCE_CRITERIA(0,1e-10);
  
  private final int intValue;
  private final double dblValue;
  
  private TMMTrainingConfig(int v1, double v2) {
   this.intValue = v1;
   this.dblValue = v2;
  }
  
  public int getIntValue() {
    return intValue;
  }

  public double getDblValue() {
    return dblValue;
  }

}
