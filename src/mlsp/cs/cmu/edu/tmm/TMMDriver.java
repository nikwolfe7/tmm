package mlsp.cs.cmu.edu.tmm;

public class TMMDriver extends RunTMMProcess {
  
  private int delayInMilliseconds = 50;
  private int blockSize = 10;

  public static void main(String[] args) {
    TMMDriver driver = new TMMDriver();
    driver.start();
  }
  
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
  
  
  
}
