package mlsp.cs.cmu.edu.tmm;

public enum TMMConstants {
  
  CLASS_GO(1,"GO","Go.mfc.tmm"),
  CLASS_JUMP(2,"JUMP","Jump.mfc.tmm"),
  CLASS_BOTH(3,"BOTH","Go-Jump.mfc.tmm"),
  CLASS_BACKGROUND(4,"BACKGROUND","Others.mfc.tmm");
  
  private final int value;
  private final String stringVal;
  private final String fileName;
  
  TMMConstants(int val, String string, String fileName) {
    this.value = val;
    this.stringVal = string;
    this.fileName = fileName;
  }

  public int getValue() {
    return value;
  }

  public String getStringVal() {
    return stringVal;
  }

  public String getFileName() {
    return fileName;
  }
  
}
