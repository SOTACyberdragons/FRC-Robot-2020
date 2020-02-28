package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Hopper extends Subsystem {

    private WPI_TalonSRX hopperMotor;
    private int hopperSpeed = -1;

    public Hopper() {

       
       
        hopperMotor = new WPI_TalonSRX(RobotMap.HOPPER_MOTOR);

    }

   public void setHopperSpeed() {
        hopperMotor.set(hopperSpeed);

   }

 
    public void stopHopping(){
        hopperMotor.set(0);
    }



    
    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }
}