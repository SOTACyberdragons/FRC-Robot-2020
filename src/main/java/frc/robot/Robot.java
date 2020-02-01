/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.AutoChoice;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Spinner;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private static String gameData;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
 
  public static Preferences prefs;
  public static Drivetrain drivetrain;
  public static Spinner spinner;
  public static Intake intake;
  public static Shooter shooter; 
  public static Hopper hopper;
  public static OI oi;

  private AutoChoice autoChoice;
	private SendableChooser<AutoChoice> chooser;
	private Command autoCommand;

	/*Auto Commands*/
	//Baseline
	private Command doNotMove;
	

//	public static CsvLogger csvLogger;

	private void initCommands() {
		System.out.println("Initializing path commands...");
		
		//Baseline
	//	doNotMove = new AutoCrossBaselineCenter();
		System.out.println("Done initializing path commands.");
		
		SmartDashboard.putData("autoCrossBaselineCenter", doNotMove);
  }


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    drivetrain = new Drivetrain();
    spinner = new Spinner();
    intake = new Intake();
    shooter = new Shooter();
    hopper = new Hopper();
    oi = new OI();

    initCommands();

		//Autonomous Chooser
		chooser = new SendableChooser<AutoChoice>();
    chooser.addOption("Don't Move", AutoChoice.DO_NOT_MOVE);
    SmartDashboard.putData("Autonomous Chooser", chooser);


  }

  @Override
  public void robotPeriodic() {
  }


  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    Scheduler.getInstance().run();
		
		autoChoice = chooser.getSelected();
    SmartDashboard.putString("Selected Autonomous", autoChoice.toString());
    
    switch (autoChoice) {
			case DO_NOT_MOVE:
			//	autoCommand = new AutoDoNotMove();
				break;
			default:
				//will only work on sides
			//	autoCommand = autoCrossBaseline;
		}

		SmartDashboard.putString("Autonomous Command", autoCommand.getName());
		autoCommand.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
	
  }

  @Override
	public void teleopInit() {

		if (autoCommand != null) {
			autoCommand.cancel();
		}
		
		drivetrain.resetSensors();
	}


  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    Scheduler.getInstance().run();
  
		SmartDashboard.putNumber("Left drive ticks", drivetrain.getLeftRawEncoderTicks());
		SmartDashboard.putNumber("Right drive ticks", drivetrain.getRightRawEncoderTicks());
		SmartDashboard.putNumber("Right Drive", drivetrain.getRightEncoder());
		SmartDashboard.putNumber("Left Drive", drivetrain.getLeftEncoder());

		SmartDashboard.putNumber("Gyro", drivetrain.getHeading());

    gameData = DriverStation.getInstance().getGameSpecificMessage();
  }


  public static String getGameData() {
    String color = ""; 
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'B' :
          color = "Blue";
          break;
        case 'G' :
          color = "Green";
          break;
        case 'R' :
          color = "Red";
          break;
        case 'Y' :
          color = "Yellow";
          break;
        default :
          //This is corrupt data
          break;
      }
    } else {
      color = "Red";
    }
    return color;
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
