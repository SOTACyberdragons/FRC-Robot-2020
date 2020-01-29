package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Hopper extends Subsystem{
    private WPI_TalonSRX motor;
    private double speed = 1;
    
    public Hopper(){
        motor = new WPI_TalonSRX(RobotMap.HOPPER_MOTOR);
    }

    public void moveBall(){
        motor.set(speed);
    }

    public void stop(){
        motor.set(0);
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
    }

}
