package frc.robot.commands;

import java.io.File;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.path.Waypoints;
import frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class FollowPath extends Command {

	private static double kP;
	private static double kI;
	private static double kD;
	private static double kF = 1 / Drivetrain.MAX_SPEED;
	private static double kA = 0;
	private static double angleKP;
	private static double angleKD;

	private Trajectory trajectory;
	private TankModifier modifier;
	private DistanceFollower left;
	private DistanceFollower right;
	private Drivetrain drive;
	private Preferences prefs;
	private Timer timer;
	private double angleError;
	private double lastAngleError;

	public FollowPath(Waypoints waypoints, double maxSpeed) {
		super();
		requires(Robot.drivetrain);

		System.out.println("Initializing Follow Path");

		timer = new Timer();
		prefs = Robot.prefs;
		drive = Robot.drivetrain;

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, 
				Trajectory.Config.SAMPLES_FAST, 
				Constants.CYCLE_SEC, 
				maxSpeed, 
				Drivetrain.MAX_ACCEL, 
				Drivetrain.MAX_JERK);

		trajectory = Pathfinder.generate(waypoints.points(), config);
		modifier = new TankModifier(trajectory).modify(Drivetrain.WHEELBASE_WIDTH);

		left = new DistanceFollower(modifier.getLeftTrajectory());
		right = new DistanceFollower(modifier.getRightTrajectory());

		//		File trajectoryCsv = new File(Constants.PATHFINDER_DIR + waypoints.getClass().getSimpleName() + ".csv");
		//		Pathfinder.writeToCSV(trajectoryCsv, trajectory);

	}

	protected void initialize() {
		System.out.println("In Follow Path init");
		drive.resetSensors();
		timer.reset();
		timer.start();
		kP = prefs.getDouble("Pathfinder.kP", 0.45);
		prefs.putDouble("Pathfinder.kP", kP);
		kD = prefs.getDouble("Pathfinder.kD", 0.01);
		prefs.putDouble("Pathfinder.kD", kD);
		angleKP = prefs.getDouble("Pathfinder.angleKP", 0.05);
		prefs.putDouble("Pathfinder.angleKP", angleKP);
		angleKD = prefs.getDouble("Pathfinder.angleKD", 0.0);
		prefs.putDouble("Pathfinder.angleKD", angleKD);

		// The first argument is the proportional gain. Usually this will be quite high
		// The second argument is the integral gain. This is unused for motion profiling
		// The third argument is the derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
		// The fourth argument is the velocity ratio. This is 1 over the maximum velocity you provided in the 
		// trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
		// The fifth argument is your acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
		
		left.configurePIDVA(kP, kI, kD, kF, kA);
		right.configurePIDVA(kP, kI, kD, kF, kA);

		drive.resetSensors();
		left.reset();
		right.reset();
	}

	protected void execute() {
		System.out.println("In Follow Path Execute");

		double gyroHeading = - drive.getHeading();    // gyro is clockwise, pathfinder counter-clockwise
		double desiredHeading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees
		SmartDashboard.putNumber("Pathfinder.desiredHeading", desiredHeading);

		angleError = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
		double angleErrorChange = lastAngleError - angleError;
		lastAngleError = angleError;
		SmartDashboard.putNumber("Pathfinder.angleError", angleError);
		SmartDashboard.putNumber("Pathfinder.angleErrorChange", angleErrorChange);

		double leftMotorOutput = left.calculate(drive.getLeftEncoder().getDistance()) - 
				(angleKP * angleError - angleKD * angleErrorChange);
		double rightMotorOutput = right.calculate(drive.getRightEncoder().getDistance()) + 
				(angleKP * angleError - angleKD * angleErrorChange);
		
		SmartDashboard.putNumber("Pathfinder.leftMotorOutput", leftMotorOutput);
		SmartDashboard.putNumber("Pathfinder.rightMotorOutput", rightMotorOutput);

		//		System.out.println("Pathfinder at " + timer.get() + ", output: " + leftMotorOutput);
		drive.drive(leftMotorOutput, rightMotorOutput);
	}

	protected boolean isFinished() {
		return left.isFinished() && right.isFinished();
	}

	protected void end() {
		System.out.println("Ended Follow Path");
		timer.stop();
	}

	protected void interrupted() {
		end();
	}
}