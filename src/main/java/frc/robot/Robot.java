// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Command;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.PigeonIMU;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();
  private Command autonomousCommand;
  public static DriveTrain drivetrain;
  public static RackMotor rackmotor;
  PIDController VisionPIDController = new PIDController(0, 0, 0);
  public static PigeonIMU pigeon;
  private Joystick m_DriveControl;
  private Joystick m_OperateControl;
  double deadzone = .25;

  Compressor pcmCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
 private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

  @Override
  public void robotInit() {
    drivetrain = new DriveTrain();
    rackmotor = new RackMotor();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    m_DriveControl = new Joystick(0);
    m_DriveControl.setYChannel(1);
    m_DriveControl.setZChannel(2);
    pcmCompressor.enableDigital();
    solenoid.set(kForward);
    SmartDashboard.putData("Autos", m_chooser);


    //m_chooser.setDefaultOption(name, object);
  }

  

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    double throttledeadzone; /*forward/backward deadzone*/
    double pivitdeadzone; /*rotation deadzone*/


    if(Math.abs(m_DriveControl.getY())>deadzone) {
      throttledeadzone = Math.pow(m_DriveControl.getY(), 3);
    } else{
      throttledeadzone = 0;
    }
    
    if(Math.abs(m_DriveControl.getZ())>deadzone) {
      pivitdeadzone = Math.pow(m_DriveControl.getZ(), 3);
    } else{
      pivitdeadzone = 0;
    }

    double [] ypr_deg = new double[3];
    ErrorCode pigeonResult = pigeon.getYawPitchRoll(ypr_deg);
    PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
    ErrorCode generalStatusResult = pigeon.getGeneralStatus(genStatus);
    SmartDashboard.putNumber("Yaw:", ypr_deg[0]);
    SmartDashboard.putNumber("Pitch:", ypr_deg[1]);
    SmartDashboard.putNumber("Roll:", ypr_deg[2]);
    SmartDashboard.putString("Pigeon Error Code", pigeonResult.toString());
    SmartDashboard.putString("Pigeon General Status Error Code", generalStatusResult.toString());
    SmartDashboard.putString("Pigeon General Status", genStatus.toString());

    if(m_DriveControl.getRawButton(1)){
      double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(2);
      double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
      SmartDashboard.putNumber("LimelightTX", tx);
      SmartDashboard.putNumber("LimelightTY", ty);
      double ControlX = VisionPIDController.calculate(-tx, 0);
      drivetrain.arcadeDrive(throttledeadzone, ControlX);
      SmartDashboard.putNumber("ControlX", ControlX);
    }  else{
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
      drivetrain.arcadeDrive(throttledeadzone, pivitdeadzone);
      VisionPIDController.reset();
        }

    if (m_OperateControl.getRawButtonPressed(5)){
      rackmotor.RackIntakeFR(1);
      rackmotor.RackIntakeBK(1);
      solenoid.toggle();
    } else if (m_OperateControl.getRawButtonPressed(7)){
      rackmotor.RackIntakeFR(0);
      rackmotor.RackIntakeBK(0);
      solenoid.toggle();
    }
  }




  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
