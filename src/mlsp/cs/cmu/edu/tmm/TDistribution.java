package mlsp.cs.cmu.edu.tmm;

import java.text.DecimalFormat;
import java.text.Format;

import cern.jet.stat.Gamma;
import jebl.math.GammaFunction;

public class TDistribution {

  private final int dimension;

  private double[] means;

  private double[] variances;

  private double[] inverseVariances;

  private double eta;

  private double etaInverse;

  private double logScalingConstant;

  /**
   * Bare bones constructor.
   * 
   * @param dim
   */
  public TDistribution(int dim) {
    this.dimension = dim;
    this.means = new double[dim];
    this.variances = new double[dim];
    this.inverseVariances = new double[dim];
  }

  /**
   * Much better constructor, if you can afford it.
   * 
   * @param dim
   * @param means
   * @param variances
   * @param eta
   */
  public TDistribution(int dim, double[] means, double[] variances, double eta) {
    this.dimension = dim;
    this.means = new double[dim];
    this.variances = new double[dim];
    this.inverseVariances = new double[dim];
    for (int i = 0; i < dim; i++) {
      setMean(i, means[i]);
      setVariance(i, variances[i]);
    }
    setEta(eta);
    calculateLogConstant();
  }

  public void setMean(int index, double mean) {
    if (index < means.length)
      means[index] = mean;
  }

  public double getMean(int index) {
    if (index < means.length)
      return means[index];
    else
      return -1;
  }

  public void setVariance(int index, double variance) {
    if (index < variances.length) {
      variances[index] = variance;
      inverseVariances[index] = 1 / variance;
    }
  }

  public double getVariance(int index) {
    if (index < variances.length)
      return variances[index];
    else
      return -1;
  }

  public double getInverseVariance(int index) {
    return inverseVariances[index];
  }

  public void setEta(double eta) {
    this.eta = eta;
    this.etaInverse = 1 / eta;
  }

  public double getEta() {
    return eta;
  }

  public double getInverseEta() {
    return etaInverse;
  }

  public void calculateLogConstant() {
    logScalingConstant = 0;
    for (int i = 0; i < dimension; i++) {
      logScalingConstant -= 0.5 * Math.log(variances[i]);
    }
    logScalingConstant -= (0.5 * dimension) * Math.log(Math.PI * eta);
    logScalingConstant -= logGamma(eta * 0.5);
    logScalingConstant += logGamma((eta + dimension) * 0.5);
  }
  
  private double logGamma(double arg) {
    return Gamma.logGamma(arg); // colt
//    return GammaFunction.lnGamma(arg); // jebl
  }
  
  public double getLogConstant() {
    return logScalingConstant;
  }

  public int getDimension() {
    return dimension;
  }
  
  public void printDistribution() {
    Format format = new DecimalFormat("##.###");
    System.out.println("Dimension: " + dimension);
    System.out.println("Eta: " + eta + " Inverse: " + etaInverse);
    System.out.println(logScalingConstant);
    System.out.println("log Scaling Constant: " + logScalingConstant);
    System.out.print("Means: ");
    for (Double d : means)
      System.out.print("\t" + format.format(d));
    System.out.print("\nVars:  ");
    for (Double d : variances)
      System.out.print("\t" + format.format(d));
  }

}
