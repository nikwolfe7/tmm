package mlsp.cs.cmu.edu.tmm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Assumes format made by Bhiksha's conversion script for .mat files
 * 
 * @author nwolfe
 *
 */
public class MATLABFileTMMFactory implements TMMFactory {
  
  public static String tmmExt = ".tmm";
  public static String dimensionMarker = "# Dimension of data";
  public static String mixtureComponentsMarker = "# Number of student-t components in mixture";
  public static String mixtureWeightsMarker = "# mixture weights for all student-t component densities";
  public static String etaValuesMarker = "# eta values for all student-t component densities";
  public static String meansMarker = "# means for all student-t component densities";
  public static String variancesMarker = "# variances for all student-t component densities";
  public static String logScalingConstantsMarker = "# Log scaling constants for all student-t component densities";

  public MATLABFileTMMFactory() {}

  @Override
  public TMixtureModel generateTMM(String fileName, String modelName) {
    System.out.println("Creating model for " + modelName + " from model file: " + fileName + "...");
    TMixtureModel tmm = null;
    if (fileName.endsWith(tmmExt)) {
      File file = new File(fileName);
      int dimension = 0;
      int numComponents = 0;
      double[] etaValues = null;
      double[] mixtureWeights = null;
      double[][] means = null;
      double[][] variances = null;
      try {
        Scanner scn = new Scanner(file);
        while (scn.hasNextLine()) {
          String line = scn.nextLine();
          if (line.contains(dimensionMarker)) {
            dimension = Integer.parseInt(scn.nextLine());
            System.out.println(dimensionMarker + ": " + dimension);
          } else if (line.contains(mixtureComponentsMarker)) {
            numComponents = Integer.parseInt(scn.nextLine());
            System.out.println(mixtureComponentsMarker + ": " + numComponents);
          } else if (line.contains(mixtureWeightsMarker)) {
            String[] arr = scn.nextLine().split("\\s");
            System.out.println(mixtureWeightsMarker + "...");
            mixtureWeights = new double[numComponents];
            for (int i = 0; i < numComponents; i++) {
              mixtureWeights[i] = Double.parseDouble(arr[i]);
            }
          } else if (line.contains(etaValuesMarker)) {
            String[] arr = scn.nextLine().split("\\s");
            System.out.println(etaValuesMarker + "...");
            etaValues = new double[numComponents];
            for (int i = 0; i < numComponents; i++) {
              etaValues[i] = Double.parseDouble(arr[i]);
            }
          } else if (line.contains(meansMarker)) {
            System.out.println(meansMarker + "...");
            means = new double[numComponents][dimension];
            int i = -1;
            while (++i < numComponents) {
              String[] arr = scn.nextLine().split("\\s");
              for (int j = 0; j < dimension; j++) {
                means[i][j] = Double.parseDouble(arr[j]);
              }
            }
          } else if (line.contains(variancesMarker)) {
            System.out.println(variancesMarker + "...");
            variances = new double[numComponents][dimension];
            int i = -1;
            while (++i < numComponents) {
              String[] arr = scn.nextLine().split("\\s");
              for (int j = 0; j < dimension; j++) {
                variances[i][j] = Double.parseDouble(arr[j]);
              }
            }
          }
        }
        scn.close();
        TDistribution[] tDists = new TDistribution[numComponents];
        for(int i = 0; i < tDists.length; i++) {
          tDists[i] = new TDistribution(dimension, means[i], variances[i], etaValues[i]);
        }
        tmm = new TMixtureModel(modelName, tDists, mixtureWeights);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("We need a TMM file type (.tmm)...");
    }
    return tmm;
  }
  
  public static void main(String[] args) {
    TMMFactory factory = new MATLABFileTMMFactory();
    System.out.println("-----------------------------------------------------------------------");
    TMixtureModel model = factory.generateTMM(TMMConstants.CLASS_GO.getFileName(), TMMConstants.CLASS_GO.getStringVal());
    model.printMixtureDistributions();
    System.out.println("\n-----------------------------------------------------------------------\n");
    model = factory.generateTMM(TMMConstants.CLASS_JUMP.getFileName(), TMMConstants.CLASS_JUMP.getStringVal());
    model.printMixtureDistributions();
//    System.out.println("\n-----------------------------------------------------------------------\n");
//    model = factory.generateTMM("Go-Jump.mfc.tmm", TMMConstants.CLASS_BOTH.getStringVal());
//    model.printMixtureDistributions();
    System.out.println("\n-----------------------------------------------------------------------\n");
    model = factory.generateTMM(TMMConstants.CLASS_BACKGROUND.getFileName(), TMMConstants.CLASS_BACKGROUND.getStringVal());
    model.printMixtureDistributions();
  }


}
