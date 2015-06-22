package mlsp.cs.cmu.edu.tmm;

public class TMMDriver extends RunTMMProcess {
  
  private int delayInMilliseconds;
  private int blockSize;
  private double numPredictions;
  
  /* testing */
  private double numGos;
  private double numJumps;
  private double numBgs; 

  public TMMDriver() {
    this.blockSize = TMMConstants.MFCC_BLOCK_SIZE.getValue();
    this.delayInMilliseconds = 0;
    this.numPredictions = 0;
    this.numGos = 0;
    this.numJumps = 0;
    this.numBgs = 0;
  }

  @Override
  protected TMMAbstractFactory getTMMAbstractFactory() {
    return new MATLABFileTMMAbstractFactory();
  }

  @Override
  protected MFCCBlockStream getMFCCBlockStream() {
    MFCCBlockStream stream = new MFCCBlockStream("./features/expanded_mfccs_38_dim.mfc",blockSize);
    stream.setPulseTime(delayInMilliseconds);
    return stream;
  }

  @Override
  protected void registerPosteriors(double[] posteriors) {
    numPredictions++;
    int maxIndex = 0;
    double maxVal = Double.MIN_VALUE;
    for (int i = 0; i < posteriors.length; i++) {
      if (maxVal < posteriors[i]) {
        maxVal = posteriors[i];
        maxIndex = i;
      }
    }
    if (maxIndex == 0) {
      numGos++;
      System.out.println(TMMConstants.CLASS_GO.getStringVal());
    } else if (maxIndex == 1) {
      numJumps++;
      System.out.println(TMMConstants.CLASS_JUMP.getStringVal());
    } else if (maxIndex == 2) {
      numBgs++;
      System.out.println(TMMConstants.CLASS_BACKGROUND.getStringVal());
    }
  }
  
  public double getNumPredictions() {
    return numPredictions;
  }

  @Override
  protected double[] getUniformPriors(int numPriors) {
    double[] arr = new double[numPriors];
    for(int i = 0; i < arr.length; i++)
      arr[i] = 1.0 / arr.length;
    return arr;
  }
  
  public static void main(String[] args) throws InterruptedException {
    TMMDriver driver = new TMMDriver();
    double start = System.currentTimeMillis();
    driver.start();
    driver.join();
    double end = System.currentTimeMillis();
    double executionTime = Math.abs(end - start);
    System.out.println("Execution time: " + executionTime + "ms");
    System.out.println("Processing time per " + driver.blockSize + " MFCC block: "
            + (executionTime / driver.numPredictions) + "ms");

    /* in this case we know there are no go's jumps in the file */
    double miss = driver.numGos + driver.numJumps;
    System.out.println("Num Go: " + driver.numGos + "\nNum Jump: " + driver.numJumps + "\nNum BG: "
            + driver.numBgs + "\nNum Predictions: " + driver.numPredictions);
    System.out.println("Accuracy: " + driver.numBgs / driver.numPredictions);
    System.out.println("Error rate: " + miss / driver.numPredictions);

  }
  
}
