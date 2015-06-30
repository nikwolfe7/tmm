package mlsp.cs.cmu.edu.tmm;

public class GaussianDistribution extends Distribution {

  public GaussianDistribution(int dim) {
    super(dim);
    // TODO Auto-generated constructor stub
  }

  public GaussianDistribution(int dim, double[] means, double[] variances) {
    super(dim, means, variances, Double.MAX_VALUE);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void calculateLogScalingConstant() {
    logScalingConstant = 0;
    for (int i = 0; i < getDimension(); i++) {
      logScalingConstant -= 0.5 * Math.log(getVariance(i));
    }
  }

}
