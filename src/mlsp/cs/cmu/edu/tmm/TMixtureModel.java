package mlsp.cs.cmu.edu.tmm;

import java.util.Iterator;

public class TMixtureModel implements Iterator<TDistribution>, Iterable<TDistribution> {
  
  private final int numComponents;
  private TDistribution[] tDistributions;
  private double[] mixtureWeights;
  private double[] logMixtureWeights;
  private String classLabel;
  private int iteration;
  
  public TMixtureModel(String classLabel, int components) {
    this.numComponents = components;
    this.tDistributions = new TDistribution[components];
    this.mixtureWeights = new double[components];
    this.logMixtureWeights = new double[components];
    this.classLabel = classLabel;
    this.iteration = 0;
  }
  
  public TMixtureModel(String classLabel, TDistribution[] tDists, double[] mixtureWeights) {
    this.numComponents = tDists.length;
    this.tDistributions = tDists;
    this.classLabel = classLabel;
    this.mixtureWeights = new double[numComponents];
    this.logMixtureWeights = new double[numComponents];
    this.iteration = 0;
    for(int i = 0; i < numComponents; i++) {
    	setMixtureWeights(i, mixtureWeights[i]);
    }
  }
  
  public int getNumComponents() {
    return numComponents;
  }
  
  public String getMixtureClassLabel() {
    return classLabel;
  }
  
  public TDistribution getTDistribution(int index) {
    if(index < tDistributions.length)
      return tDistributions[index];
    else 
      return null;
  }
  
  public double getMixtureWeight(int component) {
    if(component < numComponents)
      return mixtureWeights[component];
    else 
      return -1;
  }
  
  public void setMixtureWeights(int index, double newWeight) {
    if(index < mixtureWeights.length) {
      mixtureWeights[index] = newWeight;
      if(newWeight > 0)
        logMixtureWeights[index] = Math.log(newWeight);
      else 
        logMixtureWeights[index] = Double.NEGATIVE_INFINITY;
    }
  }
  
  public double getLogMixtureWeight(int component) {
    if(component < numComponents)
      return logMixtureWeights[component];
    else 
      return -1;
  }
  
  public void resetIteration() {
    iteration = 0;
  }
  
  @Override
  public boolean hasNext() {
    if(iteration >= numComponents) {
      resetIteration();
      return false;
    } else {
      return true;
    }
  }

  @Override
  public TDistribution next() {
    return tDistributions[iteration++];
  }
  
  @Override
  public Iterator<TDistribution> iterator() {
    return this;
  }
  
  public void printMixtureDistributions() {
    int i = 0;
    for (TDistribution tDist : tDistributions) {
      System.out.println("\nDist " + i + " with mixture weight: " + mixtureWeights[i]
              + " / log mixture weight: " + logMixtureWeights[i]);
      tDist.printDistribution();
      i++;
      System.out.println();
    }
  }

  
  
}
