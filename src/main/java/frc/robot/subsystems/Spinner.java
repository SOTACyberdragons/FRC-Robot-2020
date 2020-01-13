package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Spinner extends Subsystem {

    private WPI_TalonSRX spinnerMotor;
    public Spinner() {
        spinnerMotor = new WPI_TalonSRX(RobotMap.SPINNER_MOTOR);
        
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }
}