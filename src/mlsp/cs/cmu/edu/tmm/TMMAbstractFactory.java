package mlsp.cs.cmu.edu.tmm;

public interface TMMAbstractFactory {
  
  public TMixtureModel getModel(TMMConstants enumVal);
  
  public TMixtureModel[] getAllAvailableModels();

}
