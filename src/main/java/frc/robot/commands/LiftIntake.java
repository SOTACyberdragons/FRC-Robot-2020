package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class LiftIntake extends commands {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private DoubleSolenoid piston;
    double speed;

    public class LiftIntake (String direction) {
        piston = new DoubleSolenoid(RobotMap.INTAKE_PISTON_0, RobotMap.INTAKE_PISTON_1);
        if (direction == "up"){
            piston.in();
        } else if (direction == "down") {
            piston.out();
        }
        speed = this.speed;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {  
    
    } 

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.piston.stopMoving();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    } 
    
    public void out() {
        piston.set(DoubleSolenoid.Value.kForward);
    }

    public void in() {
        piston.set(DoubleSolenoid.Value.kReverse);
    }

    public void getPiston() {
        piston.get();
    }
 
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}