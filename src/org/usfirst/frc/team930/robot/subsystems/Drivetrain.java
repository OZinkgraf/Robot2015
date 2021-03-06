//Subsystem: Drivetrain

package org.usfirst.frc.team930.robot.subsystems;

import org.usfirst.frc.team930.robot.RobotMap;
//import org.usfirst.frc.team930.robot.commands.Drive;
import org.usfirst.frc.team930.robot.subsystems.SwerveDrive;
import org.usfirst.frc.team930.robot.subsystems.SwerveDrive.Outputs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {
	// By Team 930

	public static final int CODES_PER_REV = 414;
	public static final double DEG_TO_GEAR_TO_REV = 1 / 360.0;
	public static final double SLOWDOWN = .5;
	public static final int SPEED_TO_CODES = 8000;

	public static final double DEFAULT_JAG_P = -5900;
	public static final double DEFAULT_JAG_I = -80;
	public static final double DEFAULT_TAL_P = .05;
	public static final double DEFAULT_TAL_I = .0007;

	public SwerveDrive swerve;

	private CANTalon frDrive;
	private CANTalon flDrive;
	private CANTalon blDrive;
	private CANTalon brDrive;

	private CANJaguar frRot;
	private CANJaguar flRot;
	private CANJaguar blRot;
	private CANJaguar brRot;

	private Gyro gyro;

	public Drivetrain(double length, double width) {
		this.swerve = new SwerveDrive(length, width);
		this.setMotors();
	}

	public Drivetrain(double length, double width, boolean fieldcent) {
		this.swerve = new SwerveDrive(length, width, fieldcent);
		this.setMotors();
	}

	public void setMotors() {

		frDrive = new CANTalon(RobotMap.FRONT_RIGHT);
		flDrive = new CANTalon(RobotMap.FRONT_LEFT);
		blDrive = new CANTalon(RobotMap.BACK_LEFT);
		brDrive = new CANTalon(RobotMap.BACK_RIGHT);

		frRot = new CANJaguar(RobotMap.FRONT_RIGHT);
		flRot = new CANJaguar(RobotMap.FRONT_LEFT);
		blRot = new CANJaguar(RobotMap.BACK_LEFT);
		brRot = new CANJaguar(RobotMap.BACK_RIGHT);

		gyro = new Gyro(RobotMap.GYRO);

		frRot.setPositionMode(CANJaguar.kQuadEncoder, CODES_PER_REV,
				DEFAULT_JAG_P, DEFAULT_JAG_I, 0);
		flRot.setPositionMode(CANJaguar.kQuadEncoder, CODES_PER_REV,
				DEFAULT_JAG_P, DEFAULT_JAG_I, 0);
		blRot.setPositionMode(CANJaguar.kQuadEncoder, CODES_PER_REV,
				DEFAULT_JAG_P, DEFAULT_JAG_I, 0);
		brRot.setPositionMode(CANJaguar.kQuadEncoder, CODES_PER_REV,
				DEFAULT_JAG_P, DEFAULT_JAG_I, 0);

		frDrive.changeControlMode(ControlMode.Speed);
		frDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		flDrive.changeControlMode(ControlMode.Speed);
		flDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		blDrive.changeControlMode(ControlMode.Speed);
		blDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		brDrive.changeControlMode(ControlMode.Speed);
		brDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);

		frRot.enableControl();
		flRot.enableControl();
		blRot.enableControl();
		brRot.enableControl();

		gyro.initGyro();
	}

	public void drive(double forward, double strafe, double rot) {

		swerve.updateSwerve(forward, strafe, rot);

		this.quickAngle(swerve.output(Outputs.frontRightAngle),
				swerve.output(Outputs.frontRightSpeed), frRot, frDrive);
		this.quickAngle(swerve.output(Outputs.frontLeftAngle),
				swerve.output(Outputs.frontLeftSpeed), flRot, flDrive);
		this.quickAngle(swerve.output(Outputs.backLeftAngle),
				swerve.output(Outputs.backLeftSpeed), blRot, blDrive);
		this.quickAngle(swerve.output(Outputs.backRightAngle),
				swerve.output(Outputs.backRightSpeed), brRot, brDrive);
	}

	public void quickAngle(double angle, double speed, CANJaguar jag,
			CANTalon talon) {
		double oldAngle = jag.getPosition() * 360;
		angle += 360 * (int) ((oldAngle - angle) / 360);

		int dir = oldAngle > angle ? 1 : -1;
		// if (oldAngle > angle) {
		// dir = 1;
		// } else {
		// dir = -1;
		// }
		while (Math.abs(oldAngle - angle) > 90) {
			angle += dir * 180;
			speed *= -1;

		}
		jag.set(angle * DEG_TO_GEAR_TO_REV);
		talon.set(SPEED_TO_CODES * SLOWDOWN * speed);

	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new Drive());
	}
}
