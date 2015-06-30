package mlsp.cs.cmu.edu.tmm;

import org.apache.commons.math3.special.Gamma;

public class TDistribution extends Distribution {

  /**
   * Bare bones constructor.
   * 
   * @param dim
   */
  public TDistribution(int dim) {
    super(dim);
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
    super(dim, means, variances, eta);
  }

  public void calculateLogScalingConstant() {
    logScalingConstant = 0;
    for (int i = 0; i < getDimension(); i++) {
      logScalingConstant -= 0.5 * Math.log(getVariance(i));
    }
    logScalingConstant -= (0.5 * getDimension()) * Math.log(Math.PI * getEta());
    logScalingConstant -= logGamma(getEta() * 0.5);
    logScalingConstant += logGamma((getEta() + getDimension()) * 0.5);
  }

  private double logGamma(double arg) {
    return Gamma.logGamma(arg);
  }

}
