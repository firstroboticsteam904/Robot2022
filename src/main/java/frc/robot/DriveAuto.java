// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAuto extends Command {
  double desireddistance;
  public DriveAuto(double distance) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    desireddistance = distance;
    //requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    Robot.drivetrain.resetdistancetraveled();
    SmartDashboard.putString("Current Command", "DriveFarAuto");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double robovroom = Robot.drivetrain.getdistancetraveled();
   if(robovroom > desireddistance){
    Robot.drivetrain.arcadeDrive( 0.05, 0.25);
    SmartDashboard.getNumber("Distance Traveled", robovroom);
   } else{
     Robot.drivetrain.arcadeDrive(0, 0);
   }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.drivetrain.getdistancetraveled() < desireddistance;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
