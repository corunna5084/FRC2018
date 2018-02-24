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
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.analog.adis16448.ADIS16448_IMU;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends IterativeRobot {
	
	ADIS16448_IMU Gyro = new ADIS16448_IMU();
	private static final int kJoystickChannel = 0;

	private MecanumDrive m_robotDrive;
	private Joystick X52;
	
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
		
	Victor Lift = new Victor(0);
	
	double X;
	double Y;
	double Z;
	double rX;  //Third axis
	double rY;	//Fourth axis
	double rZ;	//Fifth axis
	
	int A = 3;
	int B = 4;
	int C = 5;
	int D = 7;
	int E = 8;
	int i = 30;
	int ModeG = 24;
	int ModeO = 25;
	int ModeR = 26;
	int Fire = 2;
	int Trigger = 1;
	
	
//	boolean BtnA = X52.getRawButton(A);
//	boolean BtnB = X52.getRawButton(B);
//	boolean BtnC = X52.getRawButton(C);
//	boolean BtnD = X52.getRawButton(D);
//	boolean BtnE = X52.getRawButton(E);
//	boolean Btni = X52.getRawButton(i);
//	boolean BtnFire = X52.getRawButton(Fire);
//	boolean BtnTrigger = X52.getRawButton(Trigger);

	
	
	@Override
	public void robotInit() {
	

		// Invert the left side motors.
		// You may need to change or remove this to match your robot.
//		frontLeft.setInverted(true);
//		rearLeft.setInverted(true);

		m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

		X52 = new Joystick(kJoystickChannel);
		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

	}

	@Override
	public void teleopPeriodic() {
		// Use the joystick X axis for lateral movement, Y axis for forward
		// movement, and Z axis for rotation.
		
		if(X52.getX()<-0.1 || X52.getX() > 0.1) {
			X = Math.pow(X52.getX(), 3);
		}else {
			X = 0;
		}
		if(X52.getY()<-0.1 || X52.getY() > 0.1) {
			Y = -1*Math.pow(X52.getY(), 3);
		}else {
			Y = 0;
		}
		if(X52.getRawAxis(5)<-0.1 || X52.getRawAxis(5) > 0.1) {
			rZ = Math.pow(X52.getRawAxis(5), 3);
		}else {
			rZ = 0;
		}
		
		m_robotDrive.driveCartesian(X, Y, rZ, Gyro.getAngleZ());
		
		
		
		
		double Speed = (X52.getRawAxis(6)+1)/2;
		if(X52.getRawButton(E)) {
		Lift.set(Speed);
		}else if(X52.getRawButton(i)) {
			Lift.set(-1*Speed);
		}else {
			Lift.set(0);
		}
		
		int EncodeL = rearLeft.getSensorCollection().getQuadraturePosition();
		double x = Math.PI;
		double InchL = (EncodeL * 6 * x)/4096;
		
		SmartDashboard.putNumber("Left Encoder in Inches", InchL);
		SmartDashboard.putNumber("Lift Speed", Speed);
		SmartDashboard.putNumber("X axis", X);
		SmartDashboard.putNumber("Y axis", Y);
		SmartDashboard.putNumber("Z Rotation axis", rZ);
		SmartDashboard.putNumber("Gyro on Z plane", Gyro.getAngleZ());
		
	}
}
