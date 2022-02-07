// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static final int A = 1;
  private static final int B = 2;
  private static final int X = 3;
  private static final int Y = 4;

  private static final double kSetPoint = 48;
  private static final double kP = 0.015; // next try 0.015
  private static final double kI = 0;
  private static final double kD = 0;

  private PIDController m_pidController;
  private CANSparkMax leftFront, leftRear, rightFront, rightRear;
  private RelativeEncoder leftEncoder, rightEncoder;
  private ShuffleboardTab tab;
  private NetworkTableEntry driveEnable;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    leftFront = new CANSparkMax(10, MotorType.kBrushless);
    leftRear = new CANSparkMax(11, MotorType.kBrushless);
    rightFront = new CANSparkMax(15, MotorType.kBrushless);
    rightRear = new CANSparkMax(16, MotorType.kBrushless);

    leftFront.setInverted(false);
    rightFront.setInverted(true);

    leftRear.follow(leftFront);
    rightRear.follow(rightFront);

    leftEncoder = leftFront.getEncoder();
    rightEncoder = rightFront.getEncoder();

    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    
    m_pidController = new PIDController(kP, kI, kD);
    m_pidController.setSetpoint(kSetPoint);
    leftEncoder.setPositionConversionFactor(1.125);
    rightEncoder.setPositionConversionFactor(1.125);

    Joystick controller = new Joystick(2);
    JoystickButton a = new JoystickButton(controller, A);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    double pidLOut = m_pidController.calculate(leftEncoder.getPosition());
    double pidROut = m_pidController.calculate(rightEncoder.getPosition());
    leftFront.set(pidLOut);
    rightFront.set(pidROut);

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {

    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
