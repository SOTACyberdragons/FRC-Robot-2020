package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.TalonFXConfig;

public class Shooter extends Subsystem {

    private WPI_TalonSRX leftMotor, rightMotor; 
    private double speed = 1;

    public Shooter() {
        leftMotor = new WPI_TalonSRX(RobotMap.LEFT_SHOOTER_MOTOR);
        leftMotor.configFactoryDefault();
        leftMotor.setInverted(false);
        rightMotor = new WPI_TalonSRX(RobotMap.RIGHT_SHOOTER_MOTOR);
        rightMotor.configFactoryDefault();
        rightMotor.setInverted(false);
        leftMotor.follow(rightMotor);
    }
    
    public void shootOut() {
        rightMotor.set(speed);
    }

    public void stop() {
        rightMotor.set(0);
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
    }


}