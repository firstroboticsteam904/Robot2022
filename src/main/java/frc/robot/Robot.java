// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


//calls all of the packages and everything you need to program

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Autos.Auto1;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.networktables.NetworkTable;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
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

   //creates all objects and doubles so that they can be called.

   //
  private Command autonomousCommand;
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();
  public static DriveTrain drivetrain;
  public static RackMotor rackmotor;
  public static wrongball ohno;
  public Auto1 Auto1;
// pidcontroller 
  PIDController VisionPIDController = new PIDController(0.045, 0.065, 0.0065);
  //driveteam joysticks
  private Joystick m_DriveControl;
  private Joystick m_OperateControl;
  //deadzone for joysticks
  double deadzone = .25;
  public static Shooter shooter;
  Compressor pcmCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
  public final DoubleSolenoid solenoidintake = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 7);
  public final DoubleSolenoid solenoidclimb = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 5);
  public static DoubleSolenoid solenoidmiddle = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 6);
  public static Timer ticktick = new Timer();
  public static Timer tocktock = new Timer();
  public static limelightdist distance;
  public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  public static NetworkTableEntry ty = table.getEntry("ty");
  

                        
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
    solenoidintake.set(kReverse);
    solenoidclimb.set(kForward);
    solenoidmiddle.set(kForward);
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
    final double targetOffsetAngle_Vertical = ty.getDouble(0.0);
    final double limelightmountangledegrees = 13.490;
    final double limelightheightinches = 41.75;
    final double goalHeightInches = 104;
    final double angletogoaldegrees = limelightmountangledegrees + targetOffsetAngle_Vertical;
    final double angletogoalradians = angletogoaldegrees * (3.14159 / 180.0);
    final double distanceFromLimelightToGoalInches = (goalHeightInches - limelightheightinches)/Math.tan(angletogoalradians);

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

    if (m_DriveControl.getRawButton(1)) {
      solenoidclimb.set(kReverse);
    }

    if (m_DriveControl.getRawButton(2)) {
      solenoidclimb.set(kForward);
    }

//left bumper
    if (m_OperateControl.getRawButton(5)){
      ticktick.start();
      solenoidintake.set(DoubleSolenoid.Value.kForward);
      //for(int i = 0; i < 100000000; i++);
      if(ticktick.get() > 0.5){
        rackmotor.RackIntake(.85);
      }
    } else {
      ticktick.stop();
      ticktick.reset();
      rackmotor.RackIntake(0);
      //for(int i = 0; i < 1000000; i++);
      solenoidintake.set(DoubleSolenoid.Value.kReverse);
    }
//right trigger
    if (m_OperateControl.getRawButton(8)){
      solenoidmiddle.set(DoubleSolenoid.Value.kReverse);

    }  else {
      solenoidmiddle.set(DoubleSolenoid.Value.kForward);
    }

    if(m_OperateControl.getRawButton(1)){

        Robot.ohno.start();
      
    }
    if(m_DriveControl.getRawButton(6)){
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
    
      double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(2);
      double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
      SmartDashboard.putNumber("LimelightTX", tx);
      SmartDashboard.putNumber("LimelightTY", ty);
      double LimeCont = VisionPIDController.calculate(0, tx);
      drivetrain.arcadeDrive(LimeCont, throttledeadzone);
    }else{
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        drivetrain.arcadeDrive(throttledeadzone, turndeadzone);
        VisionPIDController.reset();
    }


    if(m_DriveControl.getRawButton(5)){

      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
    
      double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(2);
      double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
      SmartDashboard.putNumber("LimelightTX", tx);
      SmartDashboard.putNumber("LimelightTY", ty);
      double LimeCont = VisionPIDController.calculate(0, tx);
      drivetrain.arcadeDrive(LimeCont, throttledeadzone);
      SmartDashboard.putNumber("LimeCont", LimeCont);
      SmartDashboard.putNumber("Distance", distanceFromLimelightToGoalInches);

    if(distanceFromLimelightToGoalInches <=1 && m_OperateControl.getRawButton(7)){

      Shooter.shootspeed(0.35);
      } else if(distanceFromLimelightToGoalInches <= 110 &&  m_OperateControl.getRawButton(7)){
        Shooter.shootspeed(0.65);
      } else if(distanceFromLimelightToGoalInches >= 146 && distanceFromLimelightToGoalInches <=170 && m_OperateControl.getRawButton(7)){
        Shooter.shootspeed(0.70);
      } else if(distanceFromLimelightToGoalInches >= 171 && distanceFromLimelightToGoalInches <= 190 && m_OperateControl.getRawButton(7)){
        Shooter.shootspeed(0.75);
      } else if(distanceFromLimelightToGoalInches >= 191 && distanceFromLimelightToGoalInches <= 200 && m_OperateControl.getRawButton(7)){
        Shooter.shootspeed(0.90);
      } else if(distanceFromLimelightToGoalInches > 201 && m_OperateControl.getRawButton(7)){
        Shooter.shootspeed(1.0);
      }  else {                                       
        Shooter.shootspeed(0);
      }
    }
      
      else{
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        drivetrain.arcadeDrive(throttledeadzone, turndeadzone);
        VisionPIDController.reset();
        if(m_OperateControl.getRawButton(7)){
        shooter.ShootMotorSelect();
        SmartDashboard.putNumber("RPM", shooter.getRpm());
        } else{
          shooter.shootspeed(0);
        }
  
        if(m_OperateControl.getRawButtonPressed(2)){
          shooter.SpeedSelectUp();
        }
        if(m_OperateControl.getRawButtonPressed(3)){
          shooter.SpeedSelectDown();}
        
  }

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
