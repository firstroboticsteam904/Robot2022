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
    private WPI_TalonSRX m_left2 = new WPI_TalonSRX(15);
    private MotorControllerGroup m_left = new MotorControllerGroup(m_left0, m_left1, m_left2);
    private WPI_TalonSRX m_right0 = new WPI_TalonSRX(9);
    private WPI_TalonSRX m_right1 = new WPI_TalonSRX(8);
    private WPI_TalonSRX m_right2 = new WPI_TalonSRX(16);
    private MotorControllerGroup m_right = new MotorControllerGroup(m_right0, m_right1, m_right2);
    private DifferentialDrive m_myDrivetrain = new DifferentialDrive(m_left, m_right);
   private int offset;

    public void arcadeDrive(double throttle, double turnrate){
      m_myDrivetrain.arcadeDrive(throttle, -turnrate, false);
      SmartDashboard.putNumber("throttle", throttle);
      SmartDashboard.putNumber("turnrate", turnrate);
    }


    public void resetdistancetraveled(){
      m_right0.setSelectedSensorPosition(0);
      int offset = m_right0.getSensorCollection().getQuadraturePosition();
      SmartDashboard.putNumber("encoder", offset);
    }

    public double getdistancetraveled(){
      int realencoderticks = m_right0.getSensorCollection().getQuadraturePosition();
      int fakeencoderticks = realencoderticks - offset;
      //double inches = fakeencoderticks * 0.004601;
      double inches = fakeencoderticks * 0.00308267;
     SmartDashboard.putNumber("Distance Traveled", inches);
   
     return inches;
    
    } 

    public void DistanceDistance(){
      
    }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
