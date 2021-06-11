package frc.robot.commands;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
public class DriveStraight20inches extends Command {
     public DriveStraight20inches() {
             requires(Robot.drivetrain);
    }
            
    protected void initialize() {
      //  Robot.drivetrain.zeroEncoders();
    }
    
    protected void execute() {
        double throttle = 1;
        Robot.drivetrain.setDistance(20);
    }
    
    protected boolean isFinished() { 
         return false;
    }
         
         
    protected void end() {

    } 
    protected void interrupted() {

    }
}