package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TMMConstants;

public class Driver {
  
  public static void main(String[] args) {
    String[] data = new String[] { TMMConstants.TEST_MFCC_FILE.getStringVal() };
    TMMTrainingFactory initializationFactory = new MFCCTMMTrainingFactory();
    TMMTrainingModule trainingModule = new TMMTrainingModule(initializationFactory);
    trainingModule.train(TMMConstants.CLASS_GO, data);
    trainingModule.train(TMMConstants.CLASS_JUMP, data);
    trainingModule.train(TMMConstants.CLASS_BACKGROUND, data);
  }

}
