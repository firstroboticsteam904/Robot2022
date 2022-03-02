// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/** Add your docs here. */
public class RackMotor extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.


  private WPI_TalonSRX rackintake = new WPI_TalonSRX(5);


  public void RackIntake(double speed){
    rackintake.set(speed);
  }

  
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
