package mlsp.cs.cmu.edu.tmm;

public class TMMDriver extends RunTMMProcess {

  public static void main(String[] args) {
    TMMDriver driver = new TMMDriver();
    driver.start();
  }

  @Override
  protected TMMAbstractFactory getTMMAbstractFactory() {
    return new MATLABFileTMMAbstractFactory();
  }

  @Override
  protected MFCCBlockStream getMFCCBlockStream() {
    return new MFCCBlockStream("./features/My.mfc");
  }

  @Override
  protected void registerPosteriors(double[] posteriors) {
    for(Double d : posteriors)
      System.out.print(d+"\t");
    System.out.println();
  }
  
  
  
}
