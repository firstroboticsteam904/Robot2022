// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.Joystick;

/** Add your docs here. */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
    private WPI_TalonSRX m_left0 = new WPI_TalonSRX(1);
    private WPI_TalonSRX m_left1 = new WPI_TalonSRX(2);
    private SpeedControllerGroup m_left = new SpeedControllerGroup(m_left0, m_left1);
    private WPI_TalonSRX m_right0 = new WPI_TalonSRX(9);
    private WPI_TalonSRX m_right1 = new WPI_TalonSRX(8);
    private SpeedControllerGroup m_right = new SpeedControllerGroup(m_right0, m_right1);
    private DifferentialDrive m_myDrivetrain = new DifferentialDrive(m_left, m_right);
  //  private int offset;

    public void arcadeDrive(double throttle, double turnrate){
      m_myDrivetrain.arcadeDrive(throttle, turnrate, false);
      SmartDashboard.putNumber("throttle", throttle);
      SmartDashboard.putNumber("turnrate", turnrate);
    }

  /*  public void arcardeDrive(double throttle, double turnrate){
      m_myDrivetrain.arcadeDrive(-throttle, turnrate);
      SmartDashboard.putNumber("throttle", throttle);
      SmartDashboard.putNumber("turnrate", turnrate);
    } */

    public void resetdistancetraveled(){
     // offset = /*insertmotorwithencoder*/.getSensorCollection().getQuadraturePosition();
    }

   /* public double getdistancetraveled(){
      int realencoderticks = /insertmotorwithencoder/.getSensorCollection().getQuadraturePosition();
      fakeencoderticks = realencoderticks - offset
      double inches = fakeencoderticks * 0.004601
     SmartDashboard.putNumber("Distance Traveled", inches);
   
     return inches;
    
    } */

    public void DistanceDistance(){
      
    }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
