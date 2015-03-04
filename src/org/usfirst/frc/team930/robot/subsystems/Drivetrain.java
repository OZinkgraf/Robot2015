//Subsystem: Drivetrain

package org.usfirst.frc.team930.robot.subsystems;

import org.usfirst.frc.team930.robot.RobotMap;
import org.usfirst.frc.team930.robot.subsystems.SwerveDrive;
import org.usfirst.frc.team930.robot.subsystems.SwerveDrive.Outputs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	public static final double DEFAULT_TAL_P_AUTO = .1;
	public static final double DEFAULT_TAL_I_AUTO = .00001;

	public boolean isSpeedMode;
	
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
	
	public void resetEncoder(){
		frDrive.setPosition(0);
		flDrive.setPosition(0);
		brDrive.setPosition(0);
		blDrive.setPosition(0);
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
		
		isSpeedMode = true;

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
		double dir = 0;
		angle += 360 * (int) ((oldAngle - angle) / 360);

		if (oldAngle > angle) {
			dir = 1;
		} else {
			dir = -1;
		}
		while (Math.abs(oldAngle - angle) > 90) {
			angle += dir * 180;
			speed *= -1;

		}
		jag.set(angle * DEG_TO_GEAR_TO_REV);
		talon.set(SPEED_TO_CODES * SLOWDOWN * speed);

	}
	
	public boolean isSpeedMode() {
		return isSpeedMode;
	}
	
	public void manualDrive(double d){
		frDrive.set(d);
		flDrive.set(d);
		blDrive.set(d);
		brDrive.set(d);
		SmartDashboard.putNumber("Encoder value front right", frDrive.getEncPosition());
	}
	
	public double getWheelPosition(){
		return frDrive.getPosition()/4.0;
	}
	
	public void changeTalonToPosition(){
		frDrive.changeControlMode(ControlMode.Position);
		frDrive.setPID(DEFAULT_TAL_P_AUTO, DEFAULT_TAL_I_AUTO, 0);
		flDrive.changeControlMode(ControlMode.Position);
		flDrive.setPID(DEFAULT_TAL_P_AUTO, DEFAULT_TAL_I_AUTO, 0);
		blDrive.changeControlMode(ControlMode.Position);
		blDrive.setPID(DEFAULT_TAL_P_AUTO, DEFAULT_TAL_I_AUTO, 0);
		brDrive.changeControlMode(ControlMode.Position);
		brDrive.setPID(DEFAULT_TAL_P_AUTO, DEFAULT_TAL_I_AUTO, 0);
		
		isSpeedMode = false;
	}

	public void changeTalonToSpeed(){
		frDrive.changeControlMode(ControlMode.Speed);
		frDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		flDrive.changeControlMode(ControlMode.Speed);
		flDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		blDrive.changeControlMode(ControlMode.Speed);
		blDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		brDrive.changeControlMode(ControlMode.Speed);
		brDrive.setPID(DEFAULT_TAL_P, DEFAULT_TAL_I, 0);
		
		isSpeedMode = true;
	}
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand();
	}
}