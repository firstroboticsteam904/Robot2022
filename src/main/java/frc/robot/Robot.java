// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Autos.Auto1;
import edu.wpi.first.wpilibj.command.Command;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.command.Scheduler;

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
  private Command autonomousCommand;
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();
 // private Command autonomousCommand;
  public static DriveTrain drivetrain;
  public static RackMotor rackmotor;
  public Auto1 Auto1;
  PIDController VisionPIDController = new PIDController(0, 0, 0);
  public static PigeonIMU pigeon;
  private Joystick m_DriveControl;
 // private Joystick XboxCont;
  private Joystick m_OperateControl;
  double deadzone = .30;
  public static Shooter shooter;
  private WPI_TalonSRX shootermotor = new WPI_TalonSRX(5);
 // Compressor pcmCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
 //private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

  @Override
  public void robotInit() {
    drivetrain = new DriveTrain();
    rackmotor = new RackMotor();
    Auto1 = new Auto1();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    m_DriveControl = new Joystick(0);
    m_DriveControl.setYChannel(2);
    m_DriveControl.setXChannel(1);
    shooter = new Shooter();
   // XboxCont.setYChannel(1);  //???? Look @ me
   // XboxCont.setXChannel(4);
   // pcmCompressor.enableDigital();
    //solenoid.set(kForward);
    SmartDashboard.putData("Autos", m_chooser);
    m_chooser.setDefaultOption("Auto", Auto1);
    drivetrain.resetdistancetraveled();


    //m_chooser.setDefaultOption(name, object);
  }

  

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    super.autonomousInit();
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }

    autonomousCommand = m_chooser.getSelected();
    autonomousCommand.start();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    super.autonomousInit();
  if (autonomousCommand != null) {
    autonomousCommand.cancel();
  }
  }

  @Override
  public void teleopPeriodic() {


    drivetrain.arcadeDrive(m_DriveControl.getY(), m_DriveControl.getX());
    double throttledeadzone; /*forward/backward deadzone*/
    double turndeadzone; /*rotation deadzone*/
 //   double Ydeadzonedrive;
  //  double Xdeadzonedrive;


    if(Math.abs(m_DriveControl.getY())>deadzone) {
      throttledeadzone = Math.pow(m_DriveControl.getY(), 3);
    } else{
      throttledeadzone = 0;
    }
    
    if(Math.abs(m_DriveControl.getX())>deadzone) {
      turndeadzone = Math.pow(m_DriveControl.getX(), 3);
    } else{
      turndeadzone = 0;
    }

    if (m_DriveControl.getRawButton(6)){
      Shooter.shootspeed(1);
    } else {
      Shooter.shootspeed(0);
    }
    /*if (m_OperateControl.getRawButton(6)) {
      shootermotor.set(1);
    } else {
      shootermotor.set(0);
    }*/

    //Adding Data to smart dashboard
    /*double [] ypr_deg = new double[3];
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
      drivetrain.arcadeDrive(throttledeadzone, turndeadzone);
      VisionPIDController.reset();
        }

  /*  if (m_OperateControl.getRawButtonPressed(5)){
      rackmotor.RackIntakeFR(1);
      rackmotor.RackIntakeBK(1);
      solenoid.toggle();
    } else if (m_OperateControl.getRawButtonPressed(7)){
      rackmotor.RackIntakeFR(0);
      rackmotor.RackIntakeBK(0);
      solenoid.toggle();
    }*/


  }




  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
  NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
  NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);}
  
  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
