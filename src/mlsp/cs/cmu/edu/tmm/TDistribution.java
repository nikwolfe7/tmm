package mlsp.cs.cmu.edu.tmm;

import jebl.math.GammaFunction;

public class TDistribution {

  private final int dimension;

  private double[] means;

  private double[] variances;

  private double[] inverseVariances;

  private double eta;

  private double etaInverse;

  private double logScalingConstant;

  public void printDistribution() {
    System.out.println("Dimension: " + dimension);
    //System.out.println("Means: " + ",".join(delimiter, elements));
  }

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
    logScalingConstant -= GammaFunction.lnGamma(eta * 0.5);
    logScalingConstant += GammaFunction.lnGamma((eta + dimension) * 0.5);
  }

  public double getLogConstant() {
    return logScalingConstant;
  }

  public int getDimension() {
    return dimension;
  }

}
