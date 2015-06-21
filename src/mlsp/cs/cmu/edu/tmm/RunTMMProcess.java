package mlsp.cs.cmu.edu.tmm;

public abstract class RunTMMProcess extends Thread {

  @Override
  public final void run() {
    TMMAbstractFactory factory = getTMMAbstractFactory();
    MFCCBlockStream mfccStream = getMFCCBlockStream();
    TMixtureModel[] mixtureModels = factory.getAllAvailableModels();
    while (!Thread.currentThread().isInterrupted()) {
      for (MFCCVector[] featureVector : mfccStream) {
        double[] alpha = TMMAlphaPosterior.computeAlpha(featureVector, mixtureModels);
        registerPosteriors(alpha);
      }
    }
  }

  protected abstract TMMAbstractFactory getTMMAbstractFactory();

  protected abstract MFCCBlockStream getMFCCBlockStream();

  /**
   * Define this behavior somehow... what to do with posteriors when we're done
   * 
   * @param posteriors
   */
  protected abstract void registerPosteriors(double[] posteriors);

} 
   
