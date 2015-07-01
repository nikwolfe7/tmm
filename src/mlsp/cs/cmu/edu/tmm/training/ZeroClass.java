package mlsp.cs.cmu.edu.tmm.training;

import mlsp.cs.cmu.edu.tmm.TDistribution;

import org.apache.commons.math3.special.Gamma;

import optimization.Fzero_methods;

public class ZeroClass implements Fzero_methods {

  private int dim;
  private double eta;
  private double etaConstant;

  public ZeroClass(double etaConstant, TDistribution tDistribution) {
    this.eta = tDistribution.getEta();
    this.dim = tDistribution.getDimension();
    double etaDimDivTwo = (eta + dim) / 2.0;
    this.etaConstant = 1 + etaConstant + Gamma.digamma(etaDimDivTwo) - Math.log(etaDimDivTwo);
  }

  @Override
  public double f_to_zero(double x) {
    return Math.log(x / 2.0) - Gamma.digamma(x / 2.0) + etaConstant;
  }

}
