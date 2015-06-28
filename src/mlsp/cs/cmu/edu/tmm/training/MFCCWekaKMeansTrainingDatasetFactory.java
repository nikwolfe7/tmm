package mlsp.cs.cmu.edu.tmm.training;

import java.io.File;
import java.io.IOException;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class MFCCWekaKMeansTrainingDatasetFactory implements WekaKMeansTrainingDatasetFactory {

  @Override
  public Instances getDataset(String... filenames) {
    Instances combinedInstances = null;
    boolean first = true;
    int index = 0;
    for (String file : filenames) {
      System.out.println("Reading file: " + (++index) + ": " + file + "...");
      CSVLoader loader = new CSVLoader();
      try {
        loader.setSource(new File(file));
        if (first) {
          first = !first;
          combinedInstances = loader.getDataSet();
        } else {
          Instances instances = loader.getDataSet();
          for (int i = 0; i < instances.numInstances(); i++) {
            combinedInstances.add(instances.instance(i));
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return combinedInstances;
  }

  public static void main(String[] args) {
    String file1 = "./features/expanded_mfccs_26_dim.mfc";
    String file2 = "./features/expanded_mfccs_26_dim.mfc";
    WekaKMeansTrainingDatasetFactory factory = new MFCCWekaKMeansTrainingDatasetFactory();
    Instances set = factory.getDataset(new String[] { file1, file2 });
    System.out.println(set);
  }

}
