/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
		
	Victor Lift = new Victor(0);
	Victor Grabl = new Victor(9);
	Victor Grabr = new Victor(8);
	
	int First = 7175;
	int Second = 11305;
	int Third = 15219;
	String FMS;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		rearRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);


	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}
	public void FirstMovement() {
		rearLeft.set(ControlMode.Position, First);
		rearRight.set(ControlMode.Position, First);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
	}
	
	public void SecondMovement() {
		FMS = DriverStation.getInstance().getGameSpecificMessage();
		
		if(FMS.charAt(0) == 'L') {
			rearLeft.set(ControlMode.Position, Second);
			rearRight.set(ControlMode.Position, -1 * Second);
			frontLeft.set(ControlMode.PercentOutput, -1* rearLeft.getMotorOutputPercent());
			frontRight.set(ControlMode.PercentOutput, -1 * rearRight.getMotorOutputPercent());
		}else{
			rearLeft.set(ControlMode.Position, -1* Second);
			rearRight.set(ControlMode.Position, Second);
			frontLeft.set(ControlMode.PercentOutput, -1* rearLeft.getMotorOutputPercent());
			frontRight.set(ControlMode.PercentOutput, -1 * rearRight.getMotorOutputPercent());
		}
	}
	
	public void ThirdMovement() {
		rearLeft.set(ControlMode.Position, Third);
		rearRight.set(ControlMode.Position, Third);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
	}
	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				FirstMovement();
				Timer.delay(1);
				SecondMovement();
				Timer.delay(1);
				Lift.set(.5);
				Timer.delay(3);
				ThirdMovement();
				Timer.delay(1);
				Grabl.set(0.3); Grabr.set(0.3);
				Timer.delay(3);				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
