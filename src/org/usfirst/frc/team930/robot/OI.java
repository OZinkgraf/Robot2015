package org.usfirst.frc.team930.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public static OI getInstance() {
		return Holder.instance;
	}

	private OI() {

	}

	public static class Holder {
		public static final OI instance = new OI();
	}

	final int JOYPORT = 0;
	final double DEADBAND = .09;

	// // CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	Joystick Xbox = new Joystick(JOYPORT);

	// // TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());

	public double getStrafe() {
		/*
		 * if(Math.pow(Xbox.getRawAxis(0), 2) + Math.pow(Xbox.getRawAxis(1), 2)
		 * <= Math.pow(DEADBAND, 2)) { return 0; }
		 */
		if (Math.abs(Xbox.getRawAxis(4)) < DEADBAND)
			return 0;
		return -1 * Xbox.getRawAxis(4);
	}

	public double getForward() {
		/*
		 * if (Math.pow(Xbox.getRawAxis(0), 2) + Math.pow(Xbox.getRawAxis(1), 2)
		 * < Math .pow(DEADBAND, 2)) { return 0; }
		 */
		if (Math.abs(Xbox.getRawAxis(1)) < DEADBAND)
			return 0;
		return Xbox.getRawAxis(1);
	}

	public double getRotX() {
		/*
		 * if (Math.pow(Xbox.getRawAxis(4), 2) + Math.pow(Xbox.getRawAxis(5), 2)
		 * < Math .pow(DEADBAND, 2)) { return 0; } return Xbox.getRawAxis(4);
		 */
		double val = Xbox.getRawAxis(3);
		if (Math.abs(val) < DEADBAND)
			return 0;
		else
			return val;
	}

	public double getRotY() {
		if (Math.pow(Xbox.getRawAxis(4), 2) + Math.pow(Xbox.getRawAxis(5), 2) < Math
				.pow(DEADBAND, 2)) {
			return 0;
		}
		return Xbox.getRawAxis(5);
	}
}
