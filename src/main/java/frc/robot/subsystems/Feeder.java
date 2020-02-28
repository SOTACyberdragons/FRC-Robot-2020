package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Feeder extends Subsystem {

    private WPI_TalonSRX feederMotor;
    private int feederSpeed = -1;

    public Feeder() {
        feederMotor = new WPI_TalonSRX(RobotMap.FEEDER_MOTOR);

    }

   public void feedIn() {
       feederMotor.set(feederSpeed);

   }

   public void feedOut() {
       feederMotor.set(-feederSpeed);
   }

 
    public void stopFeeding(){
        feederMotor.set(0);
    }



    
    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }
}