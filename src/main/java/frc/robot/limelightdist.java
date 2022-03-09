// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;

/** Add your docs here. */
public class limelightdist extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  public static NetworkTableEntry ty = table.getEntry("ty");
  public static double targetOffsetAngle_Vertical = ty.getDouble(0.0);
  //how many degrees back is your limelight rotated from perfectly vertical
  public static double limelightmountangledegrees = 1.0;
  //distance from the center of the Limelight to the floor
  public static double limelightheightinches = 41.75;
  //distance from target to floor
  public static double goalHeightInches = 104.0;

  public static double angletogoaldegrees = limelightmountangledegrees + targetOffsetAngle_Vertical;
  public static double angletogoalradians = angletogoaldegrees * (3.14159 / 180.0);
  //calculates distance
  public static double distanceFromLimelightToGoalInches = (goalHeightInches - limelightheightinches)/Math.tan(angletogoalradians);


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
