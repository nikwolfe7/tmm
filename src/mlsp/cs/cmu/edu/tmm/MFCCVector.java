package mlsp.cs.cmu.edu.tmm;

import java.util.Iterator;

public class MFCCVector implements Iterator<Double> {

  private int dimension;
  private double[] vector;
  private int iteration;
  
  public MFCCVector(int dim) {
    this.dimension = dim;
    this.vector = new double[dimension];
    this.iteration = 0;
  }
  
  public MFCCVector(double[] vec) {
    this.dimension = vec.length;
    this.vector = vec;
    this.iteration = 0;
  }
  
  public void setVector(double[] vec) {
    this.dimension = vec.length;
    this.vector = vec;
  }
  
  public void setCoefficient(int index, double value) {
    if(index < dimension)
      vector[index] = value;
  }
  
  public double getCoefficient(int index) {
    if(index < dimension)
      return vector[index];
    else
      return Double.MIN_VALUE;
  }
  
  public double[] getVector() {
    return vector;
  }
  
  public int getDimensionality() {
    return dimension;
  }

  public void resetIteration() {
    iteration = 0;
  }
  
  @Override
  public boolean hasNext() {
    if(iteration < dimension)
      return true;
    else
      return false;
  }

  @Override
  public Double next() {
    return vector[iteration++];
  }
  
  public void printVector() {
    while(hasNext())
      System.out.print("\t"+next());
    System.out.println();
    resetIteration();
  }
  
  /*
   * TODO: Delta coeffs... and stuff
   */
}
