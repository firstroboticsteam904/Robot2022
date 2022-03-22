// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/** Add your docs here. */
public class Shooter extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private final static WPI_TalonSRX shootmotor = new WPI_TalonSRX(5);
  private final static WPI_TalonSRX shootmotor2 = new WPI_TalonSRX(6);
  private final static MotorControllerGroup shootmotors = new MotorControllerGroup(shootmotor, shootmotor2);

 public static void shootspeed(final double speed){
 shootmotors.set(speed);
 }

//double [] ShootSpeedTable = {0.70, 0.75, 0.80, 0.85, 0.90, 0.95, 1.0, 0.40, 0.45, 0.50, 0.55, 0.60, 0.65};
double [] ShootSpeedTable = {0.70, 0.75, 0.90, 1.0, 0.35, 0.65};
int ItemTracker = 0;


 public void SpeedSelectUp(){
   ItemTracker = (ItemTracker + 1) % ShootSpeedTable.length;
 
  SmartDashboard.putNumber("Speed Select", ShootSpeedTable[ItemTracker]);
  }

  public void SpeedSelectDown(){
    ItemTracker = (ItemTracker + ShootSpeedTable.length - 1) % ShootSpeedTable.length;
    SmartDashboard.putNumber("Speed Select", ShootSpeedTable[ItemTracker]);
  }

  public void ShootMotorSelect(){
    final double speed = ShootSpeedTable[ItemTracker];

    shootmotors.set(speed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
