package mlsp.cs.cmu.edu.tmm;

public class TMMDriver extends RunTMMProcess {
  
  private int delayInMilliseconds = 0;
  private int blockSize = 5;

  public TMMDriver() {
    /**
     * Need some behavior here...
     */
  }

  @Override
  protected TMMAbstractFactory getTMMAbstractFactory() {
    return new MATLABFileTMMAbstractFactory();
  }

  @Override
  protected MFCCBlockStream getMFCCBlockStream() {
    MFCCBlockStream stream = new MFCCBlockStream("./features/My.mfc", blockSize);
    stream.setPulseTime(delayInMilliseconds);
    return stream;
  }

  @Override
  protected void registerPosteriors(double[] posteriors) {
    for(Double d : posteriors)
      System.out.print(d+"\t");
    System.out.println();
  }
  
  @Override
  protected double[] getAlphaPriors(int numPriors) {
    double[] arr = new double[numPriors];
    for(int i = 0; i < arr.length; i++)
      arr[i] = 1 / arr.length;
    return arr;
  }
  
  public static void main(String[] args) {
    TMMDriver driver = new TMMDriver();
    driver.start();
  }
  
}
