package mlsp.cs.cmu.edu.tmm.training;

public enum TrainingConfig {
  
  GAUSSIAN(0,0),
  STUDENT_T(1,1),
  DISTRIBUTION(STUDENT_T.getIntValue(),STUDENT_T.getDblValue()),
  NUM_T_DISTRIBUTIONS(64,64),
  NUM_ITERATIONS(10,10),
  ETA_INITIALIZE(500,500),
  EM_MAX_ITERATIONS(250,250),
  REALLY_SMALL_NUMBER(0,1.0e-8),
  CONVERGENCE_CRITERIA(0,1e-8);

  
  private final int intValue;
  private final double dblValue;
  
  private TrainingConfig(int v1, double v2) {
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
