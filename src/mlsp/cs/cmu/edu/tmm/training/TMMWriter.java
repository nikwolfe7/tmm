package mlsp.cs.cmu.edu.tmm.training;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mlsp.cs.cmu.edu.tmm.MATLABFileTMMFactory;
import mlsp.cs.cmu.edu.tmm.TDistribution;
import mlsp.cs.cmu.edu.tmm.TMMConstants;
import mlsp.cs.cmu.edu.tmm.TMixtureModel;

public class TMMWriter {
  
  private TMixtureModel model;
  private TMMConstants enumVal;

  public TMMWriter(TMixtureModel model, TMMConstants enumVal) {
    this.model = model;
    this.enumVal = enumVal;
  }
  
  public void writeModelToFile() {
    File file = new File(enumVal.getFileName());
    try {
      FileWriter writer = new FileWriter(file);
      
      /* data dimensionality */
      hash(writer);
      writer.write(MATLABFileTMMFactory.dimensionMarker + "\n");
      writer.write(model.getTDistribution(0).getDimension() + "\n");
      
      
      /* Student-t components */
      hash(writer);
      writer.write(MATLABFileTMMFactory.mixtureComponentsMarker + "\n");
      writer.write(model.getNumComponents() + "\n");
      
      /* Eta values */
      hash(writer);
      writer.write(MATLABFileTMMFactory.etaValuesMarker + "\n");
      StringBuilder str = new StringBuilder();
      for(TDistribution tDist : model) 
        str.append(tDist.getEta() + " ");
      writer.write(str.toString() + "\n");
      
      /* Log Scaling constants */
      hash(writer);
      writer.write(MATLABFileTMMFactory.logScalingConstantsMarker + "\n");
      str = new StringBuilder();
      for(TDistribution tDist : model)
        str.append(tDist.getLogScalingConstant() + " ");
      writer.write(str.toString() + "\n");
      
      /* Mixture weights */
      hash(writer);
      writer.write(MATLABFileTMMFactory.mixtureWeightsMarker + "\n");
      str = new StringBuilder();
      for(int i = 0; i < model.getNumComponents(); i++)
        str.append(model.getMixtureWeight(i) + " ");
      writer.write(str.toString() + "\n");
      
      /* Means */
      hash(writer);
      writer.write(MATLABFileTMMFactory.meansMarker + "\n");
      for(TDistribution tDist : model) {
        str = new StringBuilder();
        for(int i = 0; i < tDist.getDimension(); i++)
          str.append(tDist.getMean(i) + " ");
        writer.write(str.toString() + "\n");
      }
      
      /* Variances */
      hash(writer);
      writer.write(MATLABFileTMMFactory.variancesMarker + "\n");
      for(TDistribution tDist : model) {
        str = new StringBuilder();
        for(int i = 0; i < tDist.getDimension(); i++)
          str.append(tDist.getVariance(i) + " ");
        writer.write(str.toString() + "\n");
      }
      
      /* Done! */
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void hash(FileWriter writer) throws IOException {
    writer.write("#\n");
  }

}
