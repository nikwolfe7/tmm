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
    TMixtureModel m1 = getModel(TMMConstants.CLASS_GO);
    if (outputOn)
      m1.printMixtureDistributions();

    System.out.println("\n-----------------------------------------------------------------------\n");
    TMixtureModel m2 = getModel(TMMConstants.CLASS_JUMP);
    if (outputOn)
      m2.printMixtureDistributions();

    // System.out.println("\n-----------------------------------------------------------------------\n");
    // model = getModel(TMMConstants.CLASS_BOTH);
    // model.printMixtureDistributions();
    // mixture[i++] = model;

    System.out.println("\n-----------------------------------------------------------------------\n");
    TMixtureModel m3 = getModel(TMMConstants.CLASS_BACKGROUND);
    if (outputOn)
      m3.printMixtureDistributions();

    TMixtureModel[] mixture = new TMixtureModel[] { m1, m2, m3 };

    System.out.println("\n-----------------------------------------------------------------------\n");
    return mixture;
  }

  public static void main(String[] args) {
    TMMAbstractFactory factory = new MATLABFileTMMAbstractFactory();
    factory.getAllAvailableModels();
  }

}
