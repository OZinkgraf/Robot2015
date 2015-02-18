package org.usfirst.frc.team930.robot.commands;

import org.usfirst.frc.team930.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class OpenRightClaw extends Command {
	
    public OpenRightClaw() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.rightClaw);	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        SmartDashboard.putString("Open right status", "Im opening the right claw ");
        SmartDashboard.putBoolean("Right Claw Open", Robot.rightClaw.limitSwitchOpen.get());
      	Robot.rightClaw.openClaw();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.rightClaw.limitSwitchOpen.get();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.rightClaw.stopClaw(); 
    	SmartDashboard.putString("Open right status","Leaving open right claw");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
