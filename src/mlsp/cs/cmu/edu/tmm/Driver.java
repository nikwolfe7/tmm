package mlsp.cs.cmu.edu.tmm;

public class Driver {

  public static void main(String[] args) {
   TMMAbstractFactory factory = new MATLABFileTMMAbstractFactory();
   TMixtureModel[] mixtureModels = factory.getAllAvailableModels();
  }
  
}
