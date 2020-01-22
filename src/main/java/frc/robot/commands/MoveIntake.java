package frc.robot.commands;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class MoveIntake extends Command {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private String direction;

    public MoveIntake(String direction) {
        requires(Robot.intake);
        direction = this.direction;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {  
        if (direction == "up"){
            Robot.intake.moveIntake("up");
        } else if (direction == "down") {
           Robot.intake.moveIntake("down");
        }
    } 

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       
    }
    
    @Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

    // Called once after isFinished returns true
    protected void end() {
        Robot.intake.stopMoving();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    } 
   
}
