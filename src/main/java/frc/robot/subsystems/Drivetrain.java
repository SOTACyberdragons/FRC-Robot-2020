
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.Preferences;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.commands.DifferentialDriveWithJoysticks;
import oi.limelightvision.limelight.frc.LimeLight;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Drivetrain extends Subsystem {


	public final static double WHEELBASE_WIDTH = 24.25;
	public final static double WHEEL_DIAMETER = 6;
	public final static double PULSE_PER_REVOLUTION = 4096;
	public final static double REDUCTION_TO_ENCODER = 10.75;
	public final static double DISTANCE_PER_PULSE = Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION;
	public final static double MAX_SPEED = 110.0;
	public static final double MAX_ACCEL = 1.0 / 0.0254; //0.2g in in/s^2
	public static final double MAX_JERK = 20 / 0.0254; // 30 / 0.0254; //from example code in Pathfinder
	public final double encoderMaxSpeed = 33000;

	public WPI_TalonSRX leftSlave;
	public WPI_TalonSRX leftMaster;
	public WPI_TalonSRX rightSlave;
	public WPI_TalonSRX rightMaster;

	private LimeLight  limelight = new LimeLight();
	
	private DifferentialDrive drive;
	public DifferentialDrive drive1;
	private PigeonIMU gyro = new PigeonIMU(0);
	private Preferences prefs;


	public Drivetrain() {

		leftSlave = new WPI_TalonSRX(RobotMap.LEFT_MASTER);
		rightSlave = new WPI_TalonSRX(RobotMap.RIGHT_MASTER);
		leftMaster = new WPI_TalonSRX(RobotMap.LEFT_SLAVE);
		rightMaster = new WPI_TalonSRX(RobotMap.RIGHT_SLAVE);
		
		//WPI_TalonSRX leftMotor2 = leftMotor;
		drive = new DifferentialDrive(leftMaster, rightMaster);
		drive.setSafetyEnabled(true);
		drive.setExpiration(0.1);
		drive.setMaxOutput(1);
	

		initDriveTalon(leftMaster);
		leftMaster.setSensorPhase(true);
		leftMaster.setInverted(false); //'true' disabled this side -- be careful
		initDriveTalon(rightMaster);
		rightMaster.setSensorPhase(false);
		rightMaster.setInverted(false); //set to false
		
		leftSlave.follow(leftMaster);
		rightSlave.follow(rightMaster);
	}
	
	
	public void initDriveTalon(WPI_TalonSRX talon) {
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
		
	
	
		/* Set relevant frame periods to be at least as fast as periodic rate */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);
	
		/* set the peak and nominal outputs */
		talon.configNominalOutputForward(0, Constants.TIMEOUT_MS);
		talon.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
		talon.configPeakOutputForward(1, Constants.TIMEOUT_MS);
		talon.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);
	
		/* set closed loop gains in slot 0 - see documentation */
		//distance
		talon.selectProfileSlot(Constants.SLOT_IDX, Constants.PID_LOOP_IDX);
		talon.config_kF(0, Constants.TALON_MAX_OUTPUT/encoderMaxSpeed, Constants.TIMEOUT_MS);
		talon.config_kP(0, 0.45, Constants.TIMEOUT_MS);
		talon.config_kI(0, 0, Constants.TIMEOUT_MS);
		talon.config_kD(0, 0.0, Constants.TIMEOUT_MS); 

		//turning 
		talon.config_kF(1, 0, Constants.TIMEOUT_MS);
		talon.config_kP(1, 0.1, Constants.TIMEOUT_MS);
		talon.config_kI(1, 0, Constants.TIMEOUT_MS);
		talon.config_kD(1, 0, Constants.TIMEOUT_MS); 
		
		/* set acceleration and cruise velocity - see documentation */
		talon.configMotionCruiseVelocity(25000 , Constants.TIMEOUT_MS);
		talon.configMotionAcceleration(20000, Constants.TIMEOUT_MS);
	}
	 
	public void stop() {
		drive.arcadeDrive(0,0);
	}

	public void zeroEncoder() {
		leftMaster.setSelectedSensorPosition(0, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
		rightMaster.setSelectedSensorPosition(0, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
	}
	
	public double getLeftRawEncoderTicks() {
		return leftMaster.getSelectedSensorPosition(0);
	}

	public double getRightRawEncoderTicks() {
		return rightMaster.getSelectedSensorPosition(0);
	}

	public double getLeftEncoder() {
		return getLeftRawEncoderTicks() * DISTANCE_PER_PULSE;
	}

	public double getRightEncoder() {
		return getRightRawEncoderTicks() * DISTANCE_PER_PULSE;
	}

	public double getHeading() {
		PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		double [] xyz_dps = new double[3];
		gyro.getRawGyro(xyz_dps);
		double currentAngle = gyro.getFusedHeading(fusionStatus);
		return currentAngle;
	}

	public void resetSensors() {
		leftMaster.setSelectedSensorPosition(0);
		rightMaster.setSelectedSensorPosition(0);
		gyro.setFusedHeading(0);
	}
	public void drive(double xSpeed, double zRotation) {
		drive.arcadeDrive(xSpeed, zRotation, true);
	}

	public LimeLight getLimeLight() {
		return limelight;
	}
	
	public void setDistance(double distanceIn) {
		double distanceTicks = distanceIn / DISTANCE_PER_PULSE;
		double totalDistance = (getLeftRawEncoderTicks() + getRightRawEncoderTicks()) / 2 + distanceTicks;
		double angle = getHeading();
		rightMaster.set(ControlMode.MotionMagic, totalDistance, DemandType.AuxPID, angle);
	}

	public void setAngle(double angle) {
		double distance = (getLeftRawEncoderTicks() + getRightRawEncoderTicks()) / 2;
		double totalAngle = angle + getHeading();
		// rightMaster.set(ControlMode.MotionMagic, distance, DemandType.AuxPID, totalAngle);
		// leftMaster.set(ControlMode.MotionMagic, distance, DemandType.AuxPID, -totalAngle);
		//leftMaster.set(ControlMode.PercentOutput, distance, DemandType.ArbitraryFeedForward, totalAngle);
		rightMaster.set(ControlMode.PercentOutput, distance, DemandType.ArbitraryFeedForward, -totalAngle);
	}
	 //inches per second 
	public void setVelocity(double leftSpeed, double rightSpeed) {
		double left, right;
		if(leftSpeed > MAX_SPEED) {
			left = MAX_SPEED;
		} else {
			left = leftSpeed;
		}
		if(rightSpeed > MAX_SPEED) {
			right = MAX_JERK;
		} else {
			right = rightSpeed;
		}
		double leftInPerSecToTicksPer100ms = left / DISTANCE_PER_PULSE / 10;
		leftMaster.set(ControlMode.Velocity, leftInPerSecToTicksPer100ms);
		double rightInPerSecToTicksPer100ms = right / DISTANCE_PER_PULSE / 10;
		leftMaster.set(ControlMode.Velocity, rightInPerSecToTicksPer100ms);

	}

	public void initDefaultCommand() {
		setDefaultCommand(new DifferentialDriveWithJoysticks());
	}
}

