package frc.robot.utils;

/**
 * DESCRIPTION: <br>
 * Boosts the input to motors to the provided boost level when just below or above zero
 * then behaves linearly
 * 
 * 
 */

public class BoostFilter {
	
	private double boost;
	
	/**
	 * @param boost to what magnitude the output should jump when just above or below zero
	 */
	public BoostFilter(double boost) {
		
		this.boost = boost;
	}

	public double output(double input) {
		
		double sign = Math.signum(input);
		double magn = Math.abs(input);
		
		double result = sign * (magn * (1 - boost) + boost);
		return result;
	}

}