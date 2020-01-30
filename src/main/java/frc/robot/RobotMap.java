/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//PWM
	
	//public static final int CLIMBER_MOTOR = 0;


	//CAN

	//drivetrain
	public static final int RIGHT_MASTER = 1;
	public static final int RIGHT_SLAVE = 0;
	public static final int LEFT_MASTER = 2; 
	public static final int LEFT_SLAVE = 3; 

	//spinner
	public static final int SPINNER_MOTOR = 11;

	//shooter
	public static final int LEFT_SHOOTER_MOTOR = 4 ;
	public static final int RIGHT_SHOOTER_MOTOR = 5;

	//robotintake
	public static final int INTAKE_MOTOR = 5;

	//PCM
	public static final int DOUBLE_SOLENOID_ZERO = 0;
	public static final int DOUBLE_SOLENOID_ONE = 1;




	
}