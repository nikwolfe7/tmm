package mlsp.cs.cmu.edu.tmm;

public class MATLABFileTMMAbstractFactory implements TMMAbstractFactory {

  private TMMFactory factory;

  private boolean outputOn;

  public MATLABFileTMMAbstractFactory() {
    this.factory = new MATLABFileTMMFactory();
    this.outputOn = false;
  }

  @Override
  public TMixtureModel getModel(TMMConstants enumVal) {
    return factory.generateTMM(enumVal.getFileName(), enumVal.getStringVal());
  }

  @Override
  public TMixtureModel[] getAllAvailableModels() {
    System.out.println("-----------------------------------------------------------------------\n");
    TMixtureModel go = getModel(TMMConstants.CLASS_GO);
    if (outputOn)
      go.printMixtureDistributions();

    System.out.println("\n-----------------------------------------------------------------------\n");
    TMixtureModel jump = getModel(TMMConstants.CLASS_JUMP);
    if (outputOn)
      jump.printMixtureDistributions();

    // System.out.println("\n-----------------------------------------------------------------------\n");
    // model = getModel(TMMConstants.CLASS_BOTH);
    // model.printMixtureDistributions();
    // mixture[i++] = model;

    System.out.println("\n-----------------------------------------------------------------------\n");
    TMixtureModel bg = getModel(TMMConstants.CLASS_BACKGROUND);
    if (outputOn)
      bg.printMixtureDistributions();

    TMixtureModel[] mixture = new TMixtureModel[] { go, jump, bg };

    System.out.println("\n-----------------------------------------------------------------------\n");
    return mixture;
  }

  public static void main(String[] args) {
    TMMAbstractFactory factory = new MATLABFileTMMAbstractFactory();
    factory.getAllAvailableModels();
  }

}
