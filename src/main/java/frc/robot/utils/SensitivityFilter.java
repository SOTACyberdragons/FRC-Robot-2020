  package frc.robot.utils;

/**
 * DESCRIPTION: <br>
 * Keeps input at 0 up to the sensitivity threshold then behaves linearly
 * 
 */

public class SensitivityFilter {
	
	private double threshold;
	
	public SensitivityFilter(double threshold) {
		
		this.threshold = threshold;
		
	}

	
	public double output(double input) {
		
		double sign = Math.signum(input);
		double magn = Math.abs(input);
		
		double result = sign * (magn < threshold ? 0 : (magn - threshold) / (1 - threshold));	
		return result;
	}
}