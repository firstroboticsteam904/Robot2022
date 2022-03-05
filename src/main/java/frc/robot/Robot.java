// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Autos.Auto1;
import frc.robot.RackMotor;
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
import edu.wpi.first.wpilibj.Timer;

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
  double deadzone = .38;
  public static Shooter shooter;
  Compressor pcmCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
  public final DoubleSolenoid solenoidright = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 7);
  public final DoubleSolenoid solenoidleft = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 5);
  public final DoubleSolenoid solenoidmiddle = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 6);
 public static Timer ticktick = new Timer();
                        
  @Override
  public void robotInit() {
    drivetrain = new DriveTrain();
    rackmotor = new RackMotor();
    Auto1 = new Auto1();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    m_DriveControl = new Joystick(0);
    m_DriveControl.setYChannel(4);
    m_DriveControl.setXChannel(1);
    m_OperateControl = new Joystick(1);
    shooter = new Shooter();
    pcmCompressor.enableDigital();
    solenoidright.set(kReverse);
    solenoidleft.set(kReverse);
    solenoidmiddle.set(kReverse);
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


    if (m_OperateControl.getRawButton(12)){
      ticktick.start();
      solenoidright.set(DoubleSolenoid.Value.kForward);
      solenoidleft.set(DoubleSolenoid.Value.kForward);
      //for(int i = 0; i < 100000000; i++);
      if(ticktick.get() > 2){
        rackmotor.RackIntake(1);
      }
    } else {
      ticktick.stop();
      ticktick.reset();
      rackmotor.RackIntake(0);
      //for(int i = 0; i < 1000000; i++);
      solenoidright.set(DoubleSolenoid.Value.kReverse);
      solenoidleft.set(DoubleSolenoid.Value.kReverse);
    }

    if (m_OperateControl.getRawButton(11)){
      solenoidmiddle.set(DoubleSolenoid.Value.kForward);

    }  else {
      solenoidmiddle.set(DoubleSolenoid.Value.kReverse);
    }


      if(m_OperateControl.getRawButton(7)){
      shooter.ShootMotorSelect();
      } else{
        shooter.shootspeed(0);
      }

      if(m_OperateControl.getRawButtonPressed(2)){
        shooter.SpeedSelectUp();
      }
      if(m_OperateControl.getRawButtonPressed(3)){
        shooter.SpeedSelectDown();
      }
   
    /*if(m_DriveControl.getRawButton(1)){
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
