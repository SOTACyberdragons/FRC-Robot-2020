package frc.robot.commands;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;


/**
 *
 */
public class FeedBall extends Command {

    String direction = "";

    public FeedBall(String direction) {
        requires(Robot.feeder);
        this.direction = direction;
    }
    public FeedBall() {
        requires(Robot.feeder);
    }


    // Called just before this Command runs the first time
    protected void initialize() {  

    } 

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(direction == "in") { 
        Robot.feeder.feedIn();	
        } else if(direction == "out") {
            Robot.feeder.feedOut();
        } else{
            Robot.feeder.feedIn();
        }
        System.out.println("Feeding!!!!");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.feeder.stopFeeding();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
    
}