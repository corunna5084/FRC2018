/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import com.analog.adis16448.ADIS16448_IMU;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String Forward = "Forward Auto";
	private static final String rSwitch = "Right Switch Auto";
	private static final String Test = "Testing Auto";
	private static final String Switch = "Main Switch Auto";
	private static final String SwtchScle = "Switch to Scale Auto";
	private static final String Left = "Scale Left";
	private static final String Right = "Scale Right";
//	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
//	ADIS16448_IMU Gyro = new ADIS16448_IMU();
	MecanumDrive mech;
	Compressor Comp = new Compressor(0);
	
	AnalogInput LimitT = new AnalogInput(1);
	AnalogInput LimitB = new AnalogInput(0);
	AnalogInput BoxDetect = new AnalogInput(2);
	AnalogInput Hall = new AnalogInput(3);
	Timer time = new Timer();

	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);	
	Victor Lift = new Victor(1);
	Victor Grab1 = new Victor(9);
	Victor Grab2 = new Victor(8);	
	Solenoid Claw = new Solenoid(0);
	
	Joystick Log;
	int A = 1;
	int X2 = 3;
	int Y2 = 4;
	int B = 2;
	int LB = 5;
	int RB = 6;
	int Start = 8;	
	int Back = 7;

//	Joystick Log;
//	int E = 8;
//	int i = 30;
//	int Trigger = 1;
//	int C = 5;
//	int D = 7;
//	int B = 4;
//	int Fire = 2;	
//	int ModeG = 24;
//	int ModeO = 25;
//	int ModeR = 26;
	
	//Overrides control
	int lift = 0;
	int grab = 0;
	
	//Misc. values
	int TimeOut = 10;
	int rampUp = 0;
	double Percent = 0.5;
	int flag;
	String FMS;
	int Total = 0;
	int g = 0;
	int h = 0;
	int c = 0;
	double Top;
	double Bottom;
	double Interesting;
	boolean check = false;

	//Competition 
	double lP = 0.5077; //(102.3/25791)*128 
	double lI = 0.0;
	double lD = 5.077; //lP*10
	double lF = 0.167 * Percent;
	int lV = 4604;
	
	double rP = 0.51086; //(102.3/25632)*128
	double rI = 0.0;
	double rD = 5.1086; //rP*10
	double rF = 0.168 * Percent;
	int rV = 4561;

	//Practice
//	double lP = 0.00473*128; //2.827;
//	double lI = 0.00;//0.008;
//	double lD = lP*10;//28.27;
//	double lF = 0.372 * Percent;//0.372
//	int lV = 2064;
//	
//	double rP = 0.00452*128;//3.020;
//	double rI = 0.00;//0.0125;
//	double rD = rP*10;//30.20;
//	double rF = 0.385 * Percent;//0.385
//	int rV = 1995;	
	

	
	/*
	 * Practice robot PIDF control numbers
	 * Left(PIDF V):0.68, 0.0065, 6.8, 0.3365,		2280
	 * Right(PIDF V); 2.224, 0.03, 22.24, 0.3545	2165
	 */

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
		mech = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
		Log = new Joystick(0);
//		Log = new Joystick(0);		
		Comp.setClosedLoopControl(true);
		time.start();

		m_chooser.addDefault(Forward, Forward);
		m_chooser.addObject(Test, Test);
		m_chooser.addObject(rSwitch, rSwitch);
		m_chooser.addObject(Switch, Switch);
		m_chooser.addObject(SwtchScle, SwtchScle);
		m_chooser.addObject(Left, Left);
		m_chooser.addObject(Right, Right);
		SmartDashboard.putData("Auton choices", m_chooser);
		
		rearLeft.setSafetyEnabled(false); 
		frontLeft.setSafetyEnabled(false);
		rearRight.setSafetyEnabled(false);
		frontRight.setSafetyEnabled(false);
		Grab1.setSafetyEnabled(false);
		Grab2.setSafetyEnabled(false);
		Lift.setSafetyEnabled(false);
		mech.setSafetyEnabled(false);
		
		//Setup for the Rear Left Encoder/Motor
		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		rearLeft.configNominalOutputForward(0, TimeOut);
		rearLeft.configNominalOutputReverse(0, TimeOut);
		rearLeft.configPeakOutputForward(1, TimeOut);
		rearLeft.configPeakOutputReverse(-1, TimeOut);
		rearLeft.configAllowableClosedloopError(0, 0, TimeOut);
		rearLeft.setSensorPhase(true);
		rearLeft.setInverted(false);
		rearLeft.selectProfileSlot(0, 0);
		rearLeft.config_kP(0, lP, TimeOut);
		rearLeft.config_kI(0, lI, TimeOut);
		rearLeft.config_kD(0, lD, TimeOut);
		rearLeft.config_kF(0, lF, TimeOut);
		rearLeft.configMotionCruiseVelocity(lV, TimeOut);
		rearLeft.configMotionAcceleration(lV, TimeOut);
		rearLeft.configClosedloopRamp(rampUp, TimeOut);
		
		//Setup for the Rear Right Encoder/Motor
		rearRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);		
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
		rearRight.configNominalOutputForward(0, TimeOut);
		rearRight.configNominalOutputReverse(0, TimeOut);
		rearRight.configPeakOutputForward(1, TimeOut);
		rearRight.configPeakOutputReverse(-1, TimeOut);
		rearRight.configAllowableClosedloopError(0, 0, TimeOut);
		rearRight.setSensorPhase(true);
		rearRight.setInverted(false);
		rearRight.selectProfileSlot(0, 0);
		rearRight.config_kP(0, rP, TimeOut);
		rearRight.config_kI(0, rI, TimeOut);
		rearRight.config_kD(0, rD, TimeOut);
		rearRight.config_kF(0, rF, TimeOut);
		rearRight.configMotionCruiseVelocity(rV, TimeOut);
		rearRight.configMotionAcceleration(rV, TimeOut);
		rearRight.configClosedloopRamp(rampUp, TimeOut);
		frontLeft.setInverted(false);
		frontRight.setInverted(false);
				
	}

	
	
	public void debug() {
		//Misc
		SmartDashboard.putNumber("TIME					:", time.get());
		SmartDashboard.putNumber("Lift Revolutions	 	:", Total);
		SmartDashboard.putNumber("flag				 	:", flag);
		SmartDashboard.putNumber("Box Detection Voltage	:", BoxDetect.getAverageVoltage());
		//Magnetic Encoders 
	    SmartDashboard.putNumber("Left Mag Position	 	:", rearLeft.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Left Front Percent 	:", frontLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Left Rear Percent	 	:", rearLeft.getMotorOutputPercent());	
		SmartDashboard.putNumber("Left Error			:", rearLeft.getClosedLoopError(0));
	    SmartDashboard.putNumber("Right Mag Position 	:", rearRight.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Right Front Percent	:", frontRight.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Rear Percent 	:", rearRight.getMotorOutputPercent());
		SmartDashboard.putNumber("Right Error			:", rearRight.getClosedLoopError(0));
	    //Pneumatics
	    SmartDashboard.putBoolean("Comperssor State:", Comp.getClosedLoopControl());
	    SmartDashboard.putBoolean("Claw State Open:", Claw.get());
	    

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
	public void teleopInit() {
		time.reset();
		
	}
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Top = LimitT.getVoltage();
		Bottom = LimitB.getAverageVoltage();
		debug(); 
		
		if(Hall.getAverageVoltage() < 0.1) {
			if(Lift.getSpeed() > 0 && h == 0) {
				Total = Total + 1;
				h = 1;
			}else if(Lift.getSpeed() <= 0 && h == 0) {
				Total = Total - 1;
				h = 1;
			}else {
				Total = Total + 0;
			}
		}else {
			h = 0;
		}
		
		double Y;
		double X;
		double rZ;
		
		X = Log.getRawAxis(0);
		Y = Log.getRawAxis(1)*-1;
		rZ = Log.getRawAxis(4);
		
		
//		if(Log.getRawAxis(1) < -0.1 || Log.getRawAxis(1) > 0.1) {
//			X = Math.pow(Log.getX(), 3);
//		}else {
//			X = 0;
//		}
//		if(Log.getRawAxis(0) < -0.1 || Log.getRawAxis(0) > 0.1) {
//			Y = -1*Math.pow(Log.getY(), 3);
//		}else {
//			Y = 0;
//		}
//		if(Log.getRawAxis(4) < -0.1 || Log.getRawAxis(4) > 0.1) {
//			rZ = .5*Math.pow(Log.getRawAxis(5), 5);
//			
//		}else {
//			rZ = 0;
//		}

		mech.driveCartesian(X, Y, rZ, 0.0); 

//		double Speed = 1.0;
		
		//Saitek Log Joystick Controls:
		if(Log.getRawButton(RB) && Top < 4.5) {
			Lift.set(1);
			lift = 1;
		}else if(Log.getRawButton(LB) && Bottom < 4.5) {
			Lift.set(-1);
			lift = -1;
		}else { 
			Lift.set(0);
			lift = 0;
		}				
		if(Log.getRawButton(B)) {
			Grab1.set(-0.45);
			Grab2.set(0.45);
			grab = 1;
		}else if(Log.getRawButton(X2)) {
			Grab1.set(0.8);  //Values need to be reversed for Comp. Robot
			Grab2.set(-0.8);
			grab = 1;
		}else if(Log.getRawButton(Y2)) {
			Grab1.set(-1);  //Values need to be reversed for Comp. Robot
			Grab2.set(1);
			grab = 1;
		}else if(rZ > 0.3 || rZ < -0.3) {
			Grab1.set(0.45);
			Grab2.set(-0.45);
		}else if(Y < -0.1) {
			Grab1.set(0.2);
			Grab2.set(-0.2);
		}else {
			Grab1.set(0);
			Grab2.set(0);
			grab = 0;
		}
		
		//For Logictech Controller		
//		if(Log.getRawButton(B2) && lift == 0 && Top < 4.5) {
//			Lift.set(Speed);
//		}else if(Log.getRawButton(A2) && lift == 0 && Bottom < 4.5) {
//			Lift.set(-1*Speed);
//		}
//		if(Log.getRawButton(RB) && grab == 0) {
//			Grab1.set(-0.45);//Values need to be reversed for Comp. Robot
//			Grab2.set(0.45);
//		}else if(Log.getRawButton(LB) && grab == 0) {
//			Grab1.set(1);
//			Grab2.set(-1);
//		}else if(Log.getRawButton(Y2) && grab == 0) {
//			Grab1.set(-1);  //Values need to be reversed for Comp. Robot
//			Grab2.set(1);	
//		}	
//		if(Log.getRawButton(Back) && c == 0) {
//			c = 1;
//		}else if(!Log.getRawButton(Back) && c == 1) {
//			c = 2;
//		}else if (Log.getRawButton(Back) && c == 2) {
//			c = 3;
//		}else if(!Log.getRawButton(Back) && c == 3) {
//			c = 0;
//		}		
//		if(c == 1 || c == 2) {
//			Comp.stop();
//		}else { 
//			Comp.start();
//		}
		
		// Claw is used by both Controllers:
		if(Log.getRawButton(A) && g == 0) {
			g = 1;
		}else if(!Log.getRawButton(A) && g == 1) {
			g = 2;
		}else if (Log.getRawButton(A) && g == 2) {
			g = 3;
		}else if(!Log.getRawButton(A) && g == 3) {
			g = 0;
		}	
		if(g == 1 || g == 2) {
			Claw.set(true);
		}else { 
			Claw.set(false);
		}	
	    Timer.delay(0.01);
	}

	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
