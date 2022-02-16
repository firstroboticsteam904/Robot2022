// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/** Add your docs here. */
public class Shooter extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private final WPI_TalonSRX Shootmotor = new WPI_TalonSRX(5);
  public void SpeedSelectUp(){
    ItemTracker = (ItemTracker + 1) % ShootSpeedTable.length;
  
    SmartDashboard.putNumber("Speed Select", ShootSpeedTable[ItemTracker]);
   }
   public void SpeedSelectDown(){
     ItemTracker = (ItemTracker + ShootSpeedTable.length - 1) % ShootSpeedTable.length;
     SmartDashboard.putNumber("Speed Select", ShootSpeedTable[ItemTracker]);
   }
  
   
   public void ShootMotorSpeed(final double speed) {
    Shootmotor.set(speed);
   }
  
  double [] ShootSpeedTable = {0.50, 0.55, 0.60, 0.65, 0.70, 0.75, 0.80, 0.85, 0.90, 0.95, 1.0};
  int ItemTracker = 0;
  public double disiredRPM = 9.5 * ShootSpeedTable[ItemTracker];
  public void ShootMotorSelect(){
    final double speed = ShootSpeedTable[ItemTracker];}
  
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
