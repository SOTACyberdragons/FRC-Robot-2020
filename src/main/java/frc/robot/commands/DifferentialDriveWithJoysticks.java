package frc.robot.commands;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
public class DifferentialDriveWithJoysticks extends Command {
     public DifferentialDriveWithJoysticks() {
             requires(Robot.drivetrain);
    }
            
    protected void initialize() {
        System.out.println("driving!!!!");

    }
    
    protected void execute() {
        double throttle = 1;
        Robot.drivetrain.drive(-Robot.oi.getLeftStick().getY()*throttle, Robot.oi.getRightStick().getX());
    }
    
    protected boolean isFinished() { 
         return false;
    }
         
         
    protected void end() {

    } 
    protected void interrupted() {

    }
}