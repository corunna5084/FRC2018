/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {

	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(1);
	Joystick joy = new Joystick(0);
	StringBuilder sb = new StringBuilder();
 
	int loops = 0;
	double P = 0.68;//talon4 = 2.224 
	double I = 0.0065;//talon4 = 0.03 
	double D = 6.8;//talon4 = 22.24
	double F = 0.3365;// talon4 = 0.3545
	int V = 2280;//talon4  = 2165
	int TimeOut = 10;
	double lDist;
	int lPos;

	
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();

		
		rearLeft.configNominalOutputForward(0, TimeOut);
		rearLeft.configNominalOutputReverse(0, TimeOut);
		rearLeft.configPeakOutputForward(1, TimeOut);
		rearLeft.configPeakOutputReverse(-1, TimeOut);

		rearLeft.configAllowableClosedloopError(0, 0, TimeOut);
		
		
		
//		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);		
//		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		frontLeft.setInverted(true);
		
		rearLeft.setSensorPhase(true);
		rearLeft.setInverted(false);
		rearLeft.selectProfileSlot(0, 0);
		rearLeft.config_kP(0, P, TimeOut);
		rearLeft.config_kI(0, I, TimeOut);
		rearLeft.config_kD(0, D, TimeOut);
		rearLeft.config_kF(0, F, TimeOut);
		
		rearLeft.configMotionCruiseVelocity(V, TimeOut);
		rearLeft.configMotionAcceleration(V, TimeOut);
		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);

	}
	
	@Override
	public void autonomousInit() {
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);

	}

	@Override
	public void autonomousPeriodic() {
		lPos = rearLeft.getSelectedSensorPosition(0);
		lDist = ((lPos)*6*Math.PI)/4096;
		SmartDashboard.putNumber("lPosition", lPos);
		SmartDashboard.putNumber("lDist", lDist);
//		rearLeft.set(ControlMode.PercentOutput, 0.1);
		rearLeft.set(ControlMode.MotionMagic, 4096);


		Timer.delay(0.01);
	}
	
	@Override
	public void teleopPeriodic() {
		
		double Z = joy.getRawAxis(6);
		double target = Z*4096;
		
		if(joy.getRawButton(8)) {
			rearLeft.set(ControlMode.MotionMagic, target);
//			double percent = rearLeft.getMotorOutputPercent();
//			frontLeft.set(ControlMode.PercentOutput, percent);

		}
		
//		else if(joy.getRawButton(24)) {
//			rearLeft.set(ControlMode.PercentOutput, joy.getRawAxis(4));
//		}
//		else if (joy.getRawButton(25)) {
//			rearLeft.set(ControlMode.PercentOutput, joy.getRawAxis(4));
//			frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
//
//		}
		SmartDashboard.putNumber("Current Position", rearLeft.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Target Position", target);
		SmartDashboard.putNumber("Error", rearLeft.getClosedLoopError(0));
		SmartDashboard.putNumber("Encoder Velocity", rearLeft.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Output Percent", rearLeft.getMotorOutputPercent());
		
		
		Timer.delay(0.01);
	}

	@Override
	public void testPeriodic() {
		
	}
}
