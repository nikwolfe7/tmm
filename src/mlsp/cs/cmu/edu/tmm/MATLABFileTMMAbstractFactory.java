package mlsp.cs.cmu.edu.tmm;

public class MATLABFileTMMAbstractFactory implements TMMAbstractFactory {

  private TMMFactory factory;
  
  public MATLABFileTMMAbstractFactory() {
    this.factory = new MATLABFileTMMFactory();
  }

  @Override
  public TMixtureModel getModel(TMMConstants enumVal) {
    return factory.generateTMM(enumVal.getFileName(), enumVal.getStringVal());
  }

  @Override
  public TMixtureModel[] getAllAvailableModels() {
    TMixtureModel[] mixture = new TMixtureModel[4];
    int i = 0;
    System.out.println("-----------------------------------------------------------------------");
    TMixtureModel model = getModel(TMMConstants.CLASS_GO);
    model.printMixtureDistributions();
    mixture[i++] = model;
    
    System.out.println("\n-----------------------------------------------------------------------\n");
    model = getModel(TMMConstants.CLASS_JUMP);
    model.printMixtureDistributions();
    mixture[i++] = model;
    
//    System.out.println("\n-----------------------------------------------------------------------\n");
//    model = getModel(TMMConstants.CLASS_BOTH);
//    model.printMixtureDistributions();
//    mixture[i++] = model;
    
    System.out.println("\n-----------------------------------------------------------------------\n");
    model = getModel(TMMConstants.CLASS_BACKGROUND);
    model.printMixtureDistributions();
    mixture[i++] = model;
    return mixture;
  }
  
  public static void main(String[] args) {
    TMMAbstractFactory factory = new MATLABFileTMMAbstractFactory();
    factory.getAllAvailableModels();
  }

}
