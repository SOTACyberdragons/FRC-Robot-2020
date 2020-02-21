package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.TalonFXConfig;

public class Shooter extends Subsystem {

    private WPI_TalonFX leftMotor , rightMotor; 
    private double speed = 1;

    public Shooter() {
        rightMotor = TalonFXConfig.generateDefaultTalon(RobotMap.LEFT_SHOOTER_MOTOR);
        rightMotor.configFactoryDefault();
        rightMotor.setInverted(true);
        leftMotor = TalonFXConfig.generateDefaultTalon(RobotMap.RIGHT_SHOOTER_MOTOR);
        leftMotor.configFactoryDefault();
        leftMotor.setInverted(false);
        rightMotor.follow(leftMotor);

        leftMotor.configFactoryDefault();
        rightMotor.configFactoryDefault();

        rightMotor.setNeutralMode(NeutralMode.Coast);
        leftMotor.setNeutralMode(NeutralMode.Coast);
    }
    
    public void shootOut() {
        leftMotor.set(speed);
    }

    public void stop() {
        leftMotor.set(0);
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
    }


}