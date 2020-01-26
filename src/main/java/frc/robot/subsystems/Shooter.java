package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.TalonFXConfig;

public class Shooter extends Subsystem {

    private WPI_TalonFX shooterMotor; 
    private double speed = 1;

    public Shooter() {
        shooterMotor = TalonFXConfig.generateDefaultTalon(RobotMap.SHOOTER_MOTOR);
        shooterMotor.configFactoryDefault();
        shooterMotor.setInverted(false);
    }
    
    public void shootOut() {
        shooterMotor.set(speed);
    }

    public void stop() {
        shooterMotor.set(0);
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
    }


}