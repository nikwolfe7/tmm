package mlsp.cs.cmu.edu.tmm;

public enum TMMConstants {
  
  VECTOR_DIMENSIONS(38,"38"),
  NUM_T_DISTRIBUTIONS(16,"16"),
  CLASS_GO(1,"GO"),
  CLASS_JUMP(2,"JUMP"),
  CLASS_BOTH(3,"BOTH"),
  CLASS_BACKGROUND(4,"BACKGROUND");
  
  private final int value;
  private final String stringVal;
  
  TMMConstants(int val, String string) {
    this.value = val;
    this.stringVal = string;
  }

  public int getValue() {
    return value;
  }

  public String getStringVal() {
    return stringVal;
  }

}
