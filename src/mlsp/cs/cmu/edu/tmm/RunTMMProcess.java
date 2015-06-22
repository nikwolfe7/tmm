package mlsp.cs.cmu.edu.tmm;

public abstract class RunTMMProcess extends Thread {

  @Override
  public final void run() {
    
    TMMAbstractFactory factory = getTMMAbstractFactory();
    MFCCBlockStream mfccStream = getMFCCBlockStream();
    TMixtureModel[] mixtureModels = factory.getAllAvailableModels();
    double[] priors = getAlphaPriors(mixtureModels.length);
    while (!Thread.currentThread().isInterrupted()) {
      for (MFCCVector[] featureVector : mfccStream) {
        double[] alpha = TMMAlphaPosterior.computeAlphaToConvergence(featureVector, mixtureModels, priors);
        registerPosteriors(alpha);
      }
      Thread.currentThread().interrupt();
    }
  }

  protected abstract double[] getAlphaPriors(int length);

  protected abstract TMMAbstractFactory getTMMAbstractFactory();

  protected abstract MFCCBlockStream getMFCCBlockStream();

  /**
   * Define this behavior somehow... what to do with posteriors when we're done
   * 
   * @param posteriors
   */
  protected abstract void registerPosteriors(double[] posteriors);

} 
   
