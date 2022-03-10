// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

public class wrongball extends Command {
  public wrongball() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.shooter);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.tocktock.start();

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.shooter.shootspeed(.40);
      if(Robot.tocktock.get() > 3){
        Robot.solenoidmiddle.set(DoubleSolenoid.Value.kReverse);
      }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.tocktock.get() > 5;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.tocktock.stop();
    Robot.tocktock.reset();
    Robot.solenoidmiddle.set(DoubleSolenoid.Value.kForward);
    Robot.shooter.shootspeed(0.0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    
  }
}
