package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TMMConstants;

public class Driver {
  
  public static void main(String[] args) {
    
    // temp
    args = new String[] { TMMConstants.TEST_MFCC_FILE.getStringVal() };
    
    TMMTrainingFactory initializationFactory = new MFCCTMMTrainingFactory();
    TMMTrainingModule trainingModule = new TMMTrainingModule(initializationFactory);
    /* pick your poison */
    trainingModule.train(TMMConstants.CLASS_GO, args);
    trainingModule.train(TMMConstants.CLASS_JUMP, args);
    trainingModule.train(TMMConstants.CLASS_BOTH, args);
    trainingModule.train(TMMConstants.CLASS_BACKGROUND, args);
  }

}
