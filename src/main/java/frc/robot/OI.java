package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.commands.SpinToColor;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

 

    // Setting squaredInput to true decreases the sensitivity for tankdrive at lower
    // speeds
    private boolean squaredInput = true;

    public Command moveToAngle20;

    public Joystick leftDriveStick = new Joystick(0); //main driver
    public Joystick rightDriveStick = new Joystick(1); //main driver
    public Joystick leftAuxStick = new Joystick(2); //co-driver
    public Joystick rightAuxStick = new Joystick(3); //co-driver

    public OI() {

        /*
         * Set buttons
         */

         JoystickButton spinToColor = new JoystickButton(rightDriveStick, ButtonMap.SPIN_TO_COLOR); 
        
       
        /*
         * Set commands
         */
        spinToColor.whenPressed(new SpinToColor(Robot.getGameData()));


       
	}
	
	public Joystick getLeftStick() {
		return leftDriveStick;
	}

	public Joystick getRightStick() {
		return rightDriveStick;
	}

    public Joystick getLeftAuxStick() {
		return leftAuxStick;
    }
    
    public Joystick getRightAuxStick() {
        return rightAuxStick;
    }


    public boolean getSquaredInput() {
        return squaredInput;
    }
}