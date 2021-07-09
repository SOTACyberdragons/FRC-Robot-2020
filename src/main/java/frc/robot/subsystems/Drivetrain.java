
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.fasterxml.jackson.databind.deser.impl.FailingDeserializer;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.commands.DifferentialDriveWithJoysticks;
import frc.robot.oi.limelightvision.limelight.LimeLight;
import frc.robot.utils.TalonFXConfig;


/**
 * Class for drivetrain of the robot.
 */
public class Drivetrain extends Subsystem {


	public final static double WHEELBASE_WIDTH = 24.25;
	public final static double WHEEL_DIAMETER = 6;
	public final static double PULSE_PER_REVOLUTION = 2048; // from http://www.ctr-electronics.com/talon-fx.html#product_tabs_tech_specs
	public final static double REDUCTION_TO_ENCODER_FAST = PULSE_PER_REVOLUTION * 7.95; //11:42 24:50
	public final static double REDUCTION_TO_ENCODER_SLOW = (2048 * 42*60)/(11*14); //11:42 14:60
	public final static double DISTANCE_PER_PULSE = (Math.PI * WHEEL_DIAMETER) / REDUCTION_TO_ENCODER_FAST;
	public final static double DISTANCE_PER_PULSE_METERS = Units.inchesToMeters(DISTANCE_PER_PULSE);
	public final static double MEETERS_PER_SECOND = 7.95 * 2 * Math.PI * Units.inchesToMeters(3) / 60;
	public final static double MAX_SPEED = 110.0;
	public static final double MAX_ACCEL = 1.0 / 0.0254; //0.2g in in/s^2
	public static final double MAX_JERK = 20 / 0.0254; // 30 / 0.0254; //from example code in Pathfinder
	public final double encoderMaxSpeed = 33000;
	public final double distanceBetweenWheels = 24;

	public final boolean gyroReversed = false;

	public WPI_TalonFX leftFollower, leftMain, rightFollower, rightMain;
	private Faults faults = new Faults();

	private final LimeLight limelight = new LimeLight();

	public final DifferentialDrive drive;
	private final PigeonIMU gyro = new PigeonIMU(0);
	private Preferences prefs;
	
	DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(distanceBetweenWheels));
	DifferentialDriveOdometry odometry; 
		

	public Drivetrain() {
		//zeroEncoders();

		leftMain = TalonFXConfig.generateDefaultTalon(RobotMap.LEFT_MASTER);
		leftFollower = TalonFXConfig.generateDefaultTalon(RobotMap.LEFT_SLAVE);

		rightMain = TalonFXConfig.generateDefaultTalon(RobotMap.RIGHT_MASTER);
		rightFollower = TalonFXConfig.generateDefaultTalon(RobotMap.RIGHT_SLAVE);

		leftMain.configFactoryDefault();
		rightMain.configFactoryDefault();
		leftFollower.configFactoryDefault();
		rightFollower.configFactoryDefault();

		leftFollower.follow(leftMain);
		rightFollower.follow(rightMain);

		leftMain.setInverted(false);
		leftFollower.setInverted(false);
		rightMain.setInverted(false);
		rightFollower.setInverted(false);

		rightMain.setSensorPhase(false);
		leftMain.setSensorPhase(false);



		rightMain.setNeutralMode(NeutralMode.Coast);
		leftMain.setNeutralMode(NeutralMode.Coast);
		rightFollower.setNeutralMode(NeutralMode.Coast);
		leftFollower.setNeutralMode(NeutralMode.Coast);

		odometry = new DifferentialDriveOdometry(getHeading());
		drive = new DifferentialDrive(leftMain, rightMain);

		drive.setRightSideInverted(false);
	}
	/** 
	* Initializes TalonSRX motors.
	*
	* @param talon of type WPI_TalonSRX, motor to initialize
	* @return none
	*/
	public void initDriveTalonSRX(final WPI_TalonSRX talon) {
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Constants.PID_LOOP_IDX,
				Constants.TIMEOUT_MS);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);

		/* set the peak and nominal outputs */
		talon.configNominalOutputForward(0, Constants.TIMEOUT_MS);
		talon.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
		talon.configPeakOutputForward(1, Constants.TIMEOUT_MS);
		talon.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

		/* set closed loop gains in slot 0 - see documentation */
		// distance
		talon.selectProfileSlot(Constants.SLOT_IDX, Constants.PID_LOOP_IDX);
		talon.config_kF(0, Constants.TALON_MAX_OUTPUT / encoderMaxSpeed, Constants.TIMEOUT_MS);
		talon.config_kP(0, 0.45, Constants.TIMEOUT_MS);
		talon.config_kI(0, 0, Constants.TIMEOUT_MS);
		talon.config_kD(0, 0.0, Constants.TIMEOUT_MS);

		// turning
		talon.config_kF(1, 0, Constants.TIMEOUT_MS);
		talon.config_kP(1, 0.1, Constants.TIMEOUT_MS);
		talon.config_kI(1, 0, Constants.TIMEOUT_MS);
		talon.config_kD(1, 0, Constants.TIMEOUT_MS);

		/* set acceleration and cruise velocity - see documentation */
		talon.configMotionCruiseVelocity(25000, Constants.TIMEOUT_MS);
		talon.configMotionAcceleration(20000, Constants.TIMEOUT_MS);
	}
	/** 
	 * Returns sensor out of phase fault code.
	 * @return Sensor Out of Phase
	 * 
	*/
	
	public boolean leftEncoderOutOfPhase() {
		return faults.SensorOutOfPhase;
	}
	/**
	 * Method to stop the robot.
	 * @param none
	 * @return none
	 */
	public void stop() {
		drive.arcadeDrive(0, 0);
	}

	// public void zeroEncoders() {
	// 	leftMain.setSelectedSensorPosition(0, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
	// 	rightMain.setSelectedSensorPosition(0, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
	// }
	/**
	 * Gets the position of the left motor in raw position units.
	 * 
	 * @return left motor sensor position
	 */
	public double getLeftRawEncoderTicks() {
		return leftMain.getSelectedSensorPosition(0);
	}

	/**
	 * Gets the positon of the right motor in raw position units.
	 * @return right motor sensor position
	 */
	public double getRightRawEncoderTicks() {
		return rightMain.getSelectedSensorPosition(0);
	}

	/**
	 * Gets the position of the left motor in inches.
	 * @return Left motor in raw position units * DISTANCE_PER_PULSE
	 */
	public double getLeftEncoderInches() {
		return getLeftRawEncoderTicks() * DISTANCE_PER_PULSE;
	}

	/**
	 * Gets the position of the right motor in inches.
	 * @return Right motor in raw position units * DISTANCE_PER_PULSE
	 */
	public double getRightEncoderInches() {
		return getRightRawEncoderTicks() * DISTANCE_PER_PULSE;
	}

	/**
	 * Gets the distance of the left motor.
	 * @return Left motor in raw position units * the DISTANCE_PER_PULSE_METERS
	 */
	public double getLeftDistance() {
		return getLeftRawEncoderTicks() * DISTANCE_PER_PULSE_METERS;
	}

	/**
	 * Gets the distance of the right motor.
	 * @return Right motor in raw position units * the DISTANCE_PER_PULSE_METERS
	 */
	public double getRightDistance() {
		return getRightRawEncoderTicks() * DISTANCE_PER_PULSE_METERS;
	}

	/** 
	 * Gets the average distance of the left and right motors.
	 * @return The average distance of the left and right motor.  ((getRightDistance + getLeftDistance)/2)
	*/
	public double getAverageDistance() {
		return (getRightDistance() + getLeftDistance()) / 2;
	}

	/**
	 * Gets the speeds of the right and left wheels.
	 * @return The right and left Active Trajectory Velocity * DISTANCE_PER_PULSE_METERS * 10
	 */
	public DifferentialDriveWheelSpeeds getWheelSpeeds() {
		return new DifferentialDriveWheelSpeeds(
			leftMain.getActiveTrajectoryVelocity() * DISTANCE_PER_PULSE_METERS * 10,
			rightMain.getActiveTrajectoryVelocity() * DISTANCE_PER_PULSE_METERS * 10
		);
	}

	// /**
	//  * Resets the odometry to the specified pose.
	//  *
	//  * @param pose The pose to which to set the odometry.
	//  */
	// public void resetOdometry(Pose2d pose) {
	// 	zeroEncoders();
	// 	odometry.resetPosition(pose, getHeading());
	// }

	// /**
	//  * Controls the left and right sides of the drive directly with voltages.
	//  *
	//  * @param leftVolts  the commanded left output
	//  * @param rightVolts the commanded right output
	//  */
	// public void tankDriveVolts(double leftVolts, double rightVolts) {
	// 	leftMain.setVoltage(leftVolts);
	// 	rightMain.setVoltage(-rightVolts);
	// 	drive.feed();
	// }

	/**
	 * Drives the robot using arcade controls.
	 *
	 * @param fwd the commanded forward movement
	 * @param rot the commanded rotation
	 */
	public void arcadeDrive(double fwd, double rot) {
		drive.arcadeDrive(fwd, rot);
	}
	/**
	 * Returns the currently-estimated pose of the robot.
	 *
	 * @return The pose.
	 */
	public Pose2d getPose() {
		return odometry.getPoseMeters();
	}

		
	/**
	 * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
	 *
	 * @param maxOutput the maximum output to which the drive will be constrained
	 */
	public void setMaxOutput(double maxOutput) {
		drive.setMaxOutput(maxOutput);
	}

	/**
	 * Zeroes the heading of the robot.
	 */
	public void zeroHeading() {
		gyro.setFusedHeading(0);
	}

	/**
	 * Returns the turn rate of the robot.
	 *
	 * @return The turn rate of the robot, in degrees per second
	 */
	public double getTurnRate() {
		double [] xyz_dps = new double [3];
		gyro.getRawGyro(xyz_dps);
		double currentAngularRate = xyz_dps[2];
		return currentAngularRate;
	}
	/**
	 * Returns the angle of the robot
	 * 
	 * @return the angle of the robot in degrees
	 */
	public double getAngle() {
		final PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		final double[] xyz_dps = new double[3];
		gyro.getRawGyro(xyz_dps);
		final double currentAngle = gyro.getFusedHeading(fusionStatus);
		return currentAngle;
	}
	
	/**
	 * Finds the degree in which the robot is turning.
	 * @return A rotation in a 2d coordinate frame with sine and cosine. (radians)
	 */
	public Rotation2d getHeading() {
		final PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		double angle = gyro.getFusedHeading(fusionStatus);
		double newAngle = Math.IEEEremainder(angle, 360) * (gyroReversed ? -1.0 : 1.0);
		return Rotation2d.fromDegrees(newAngle);
	}

	/**
	 * Resets the sensor position.
	 */
	public void resetSensors() {
		leftMain.setSelectedSensorPosition(0);
		rightMain.setSelectedSensorPosition(0);
		gyro.setFusedHeading(0);
	}

	/**
	 * drive method for differential drive platform.
	 * 
	 * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
	 * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     	 *                  positive.
	 */
	public void drive(final double xSpeed, final double zRotation) {
		drive.arcadeDrive(xSpeed, zRotation, true);
	}

	/**
	* This is a public accessor method for the drivetrain instance of the *limelight.
	*
	*@returns the limelight object.
	*/
	public LimeLight getLimeLight() {
		return limelight;
	}

	/**
	* This method moves the drivetrain a given distance in inches. 
	* It converts the distance in inches to encoder ticks, then adds that to
	* the current robot position, then uses talon.set(...) to set the distance. https://www.ctr-electronics.com/downloads/api/java/html/classcom_1_1ctre_1_1phoenix_1_1motorcontrol_1_1can_1_1_w_p_i___talon_f_x.html#a33ebd356cf623a9f5ef0634d7284105d
	* @param distanceIn to move straight in inches.
	*/
	public void setDistance(final double distanceIn) {
		final double distanceTicks = distanceIn / DISTANCE_PER_PULSE;
		final double totalDistance = (getLeftRawEncoderTicks() + getRightRawEncoderTicks()) / 2 + distanceTicks;
		final double angle = getAngle();
		rightMain.set(ControlMode.MotionMagic, totalDistance, DemandType.AuxPID, angle);
	}

	/**
	 *  Gets the distance of the robot
	 * @return average distance of left and right motors * DISTANCE_PER_PULSE
	 */
	public double getDistance() {
		return ((getLeftRawEncoderTicks() + getRightRawEncoderTicks()) / 2)*DISTANCE_PER_PULSE;
	}

	/**
	 * Gets the distance of the right motor from the left motor in inches.
	 * @return left motor position * DISTANCE_PER_PULSE
	 */
	public double getRightDistanceInches() {
		return getLeftRawEncoderTicks() *DISTANCE_PER_PULSE;
	}

	/**
	 * Gets the distance of the left motor from the right motor in inches.
	 * @return right motor position * DISTANCE_PER_PULSE
	 */
	public double getLeftDistanceInches() {
		return getRightRawEncoderTicks() * DISTANCE_PER_PULSE;
 	}

	/**
	 * This method rotates the drivetrain.
	 * Averages the distance of the left and right motors.
	 * Creates the total angle ot the robot.
	 * @param angle to rotate in degrees
	 */
	public void setAngle(final double angle) {
		final double distance = (getLeftRawEncoderTicks() + getRightRawEncoderTicks()) / 2;
		final double totalAngle = angle + getAngle();
		// rightMain.set(ControlMode.MotionMagic, distance, DemandType.AuxPID,
		// totalAngle);
		// leftMain.set(ControlMode.MotionMagic, distance, DemandType.AuxPID,
		// -totalAngle);
		// leftMain.set(ControlMode.PercentOutput, distance,
		// DemandType.ArbitraryFeedForward, totalAngle);
		rightMain.set(ControlMode.PercentOutput, distance, DemandType.ArbitraryFeedForward, -totalAngle);
	}

	// inches per second
	/**
	 * This methods sets the speed of the robot.
	 * 
	 * @param leftSpeed Speed of left motor
	 * @param rightSpeed Speed of right motor
	 */
	public void setVelocity(final double leftSpeed, final double rightSpeed) {
		double left, right;
		if (leftSpeed > MAX_SPEED) {
			left = MAX_SPEED;
		} else {
			left = leftSpeed;
		}
		if (rightSpeed > MAX_SPEED) {
			right = MAX_JERK;
		} else {
			right = rightSpeed;
		}
		final double leftInPerSecToTicksPer100ms = left / DISTANCE_PER_PULSE / 10;
		leftMain.set(ControlMode.Velocity, leftInPerSecToTicksPer100ms);
		final double rightInPerSecToTicksPer100ms = right / DISTANCE_PER_PULSE / 10;
		leftMain.set(ControlMode.Velocity, rightInPerSecToTicksPer100ms);

	}

	@Override
	public void periodic() {
	  // Update the odometry in the periodic block
	  odometry.update(getHeading(), getLeftDistance(), getRightDistance());
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DifferentialDriveWithJoysticks());
	}
}


