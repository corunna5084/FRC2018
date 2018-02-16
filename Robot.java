/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.analog.adis16448.*;
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
	final Joystick Joy = new Joystick(0);
	
	int BtnIntake = 5;
	int BtnShootOut = 15;
	
	Victor LeftShooter = new Victor(8);
	Victor RightShooter = new Victor(9);
	WPI_TalonSRX Talon1 = new WPI_TalonSRX(1);
	WPI_TalonSRX Talon2 = new WPI_TalonSRX(3);
	WPI_TalonSRX Talon3 = new WPI_TalonSRX(4);
	WPI_TalonSRX Talon4 = new WPI_TalonSRX(5);
	
	ADIS16448_IMU Gyro = new ADIS16448_IMU();
	
	double YAxis;
	double XAxis;
	double ZRotation;
	
	MecanumDrive Mech = new MecanumDrive(Talon1, Talon2, Talon3, Talon4);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		//The Talon Motor Controllers
		
		
		
		
		
		//The lift
		Victor Lift = new Victor(0);
		
		
		
		
		
		
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

	/**
	 * This function is called periodically during autonomous.
	 */
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

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Mech.driveCartesian(Joy.getY(), Joy.getX(), Joy.getRawAxis(5), Gyro.getAngleZ());
		
		
		
		if(Joy.getZ() < -0.1 || Joy.getZ() > 0.1) {
			ZRotation = Math.pow(Joy.getZ(), 3);
		} else {
			ZRotation = 0;
		}
//////////////////////////////////////////////////////////////////		
		if(Joy.getX() < -0.1 || Joy.getX() > 0.1) {
			XAxis = Math.pow(Joy.getX(), 3);
		} else {
			XAxis = 0;
		}
////////////////////////////////////////////////////////////////////		
		if(Joy.getY() < -0.1 || Joy.getY() > 0.1) {
			YAxis = Math.pow(Joy.getY(), 3);
		} else {
			YAxis = 0;
		}
/////////////////////////////////////////////////////////////////////		
		if (Joy.getRawButton(BtnIntake) == true) {  
			LeftShooter.set(0.5);
			RightShooter.set(0.5);
		} else if(Joy.getRawButton(BtnShootOut) == true) {
			LeftShooter.set(-0.5);
			RightShooter.set(-0.5);
		} else {
			LeftShooter.set(0);
			RightShooter.set(0);
		}
		
		
		
	/**
	 * This function is called periodically during test mode.
	 */
	}@Override
	public void testPeriodic() {
	}
}
