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
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.analog.adis16448.ADIS16448_IMU;

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
	private static final String Test = "Testing";
	private static final String Switch = "Main Switch Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	ADIS16448_IMU Gyro = new ADIS16448_IMU();

	MecanumDrive mech;
	
	Compressor Comp = new Compressor(0);
	
	AnalogInput LimitT = new AnalogInput(1);
	AnalogInput LimitB = new AnalogInput(0);
	
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
	
	Timer time = new Timer();
		
	Spark Lift = new Spark(1);
	Victor Grab1 = new Victor(9);
	Victor Grab2 = new Victor(8);
	
	Solenoid Claw = new Solenoid(0);
	AnalogInput Hall = new AnalogInput(3);
//	DigitalInput Hall = new DigitalInput(0);
	
	Joystick Log;
	int A2 = 1;
	int X2 = 3;
	int Y2 = 4;
	int B2 = 2;
	int LB = 5;
	int RB = 6;
	int Start = 8;	
	int Back = 7;

	Joystick X52;
	int E = 8;
	int i = 30;
	int Trigger = 1;
	int C = 5;
	int D = 7;
	int B = 4;
	int Fire = 2;
	
	int lift = 0;
	int grab = 0;
	int claw = 0;
	
	double FinnalPos;
	int TimeOut = 10;
	
	double Percent = 0.5;
	int loops = 0;
	double lP = 0.00473*128; //2.827;
	double lI = 0.00;//0.008;
	double lD = lP*10;//28.27;
	double lF = 0.372 * Percent;//0.372
	int lV = 2064;
	
	double rP = 0.00452*128;//3.020;
	double rI = 0.00;//0.0125;
	double rD = rP*10;//30.20;
	double rF = 0.385 * Percent;//0.385
	int rV = 1995;	
	
	int flag;
	int First = 7175;
	int Second = 11305*2; 
	int Third = 15219/2;
	String FMS;
	int Flag = 0;
	int Total = 0;
	int g = 0;
	int h = 0;
	int c = 0;
	double Top = LimitT.getVoltage();
	double Bottom = LimitB.getAverageVoltage();

	
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
		
		mech = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
		
		Log = new Joystick(1);
		X52 = new Joystick(0);
		
		Comp.setClosedLoopControl(true);
		
		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);
		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		
		rearRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);
		
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
//		Gyro.reset();
//		Gyro.calibrate();
		time.start();

		
		m_chooser.addDefault("Basic Auto", Forward);
		m_chooser.addObject("Test", Test);
		m_chooser.addObject("Right Switch Auto", rSwitch);
		m_chooser.addObject("Main Switch Auto", Switch);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		CameraServer.getInstance().startAutomaticCapture().setResolution(320, 240);
		rearLeft.setSafetyEnabled(false); 
		frontLeft.setSafetyEnabled(false);
		rearRight.setSafetyEnabled(false);
		frontRight.setSafetyEnabled(false);
		Grab1.setSafetyEnabled(false);
		Grab2.setSafetyEnabled(false);
		Lift.setSafetyEnabled(false);
		mech.setSafetyEnabled(false);
		
		rearLeft.configNominalOutputForward(0, TimeOut);
		rearLeft.configNominalOutputReverse(0, TimeOut);
		rearLeft.configPeakOutputForward(1, TimeOut);
		rearLeft.configPeakOutputReverse(-1, TimeOut);
		rearLeft.configAllowableClosedloopError(0, 0, TimeOut);
		
		rearRight.configNominalOutputForward(0, TimeOut);
		rearRight.configNominalOutputReverse(0, TimeOut);
		rearRight.configPeakOutputForward(1, TimeOut);
		rearRight.configPeakOutputReverse(-1, TimeOut);
		rearRight.configAllowableClosedloopError(0, 0, TimeOut);
		
//		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);		
//		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		frontLeft.setInverted(false);
		frontRight.setInverted(false);
		
//		rearLeft.setPID();
		
		rearLeft.setSensorPhase(true);
		rearLeft.setInverted(false);
		rearLeft.selectProfileSlot(0, 0);
		rearLeft.config_kP(0, lP, TimeOut);
		rearLeft.config_kI(0, lI, TimeOut);
		rearLeft.config_kD(0, lD, TimeOut);
		rearLeft.config_kF(0, lF, TimeOut);
		rearLeft.configMotionCruiseVelocity(lV, TimeOut);
		rearLeft.configMotionAcceleration(lV, TimeOut);
		
		rearRight.setSensorPhase(true);
		rearRight.setInverted(false);
		rearRight.selectProfileSlot(0, 0);
		rearRight.config_kP(0, rP, TimeOut);
		rearRight.config_kI(0, rI, TimeOut);
		rearRight.config_kD(0, rD, TimeOut);
		rearRight.config_kF(0, rF, TimeOut);
		rearRight.configMotionCruiseVelocity(rV, TimeOut);
		rearRight.configMotionAcceleration(rV, TimeOut);

	}

	
	
	public void debug() {
//		SmartDashboard.putNumber("X-Angle			 ", Gyro.getAngleX());
//	    SmartDashboard.putNumber("Y-Angle			 ", Gyro.getAngleY());
//	    SmartDashboard.putNumber("Z-Angle			 ", Gyro.getAngleZ());
//	    SmartDashboard.putNumber("X-Acceleration	 ", Gyro.getAccelX());
//	    SmartDashboard.putNumber("Y-Acceleration	 ", Gyro.getAccelY());
//	    SmartDashboard.putNumber("Z-Acceleration	 ", Gyro.getAccelZ());
//	    SmartDashboard.putNumber("X-Mag				 ", Gyro.getMagX());
//	    SmartDashboard.putNumber("Y-Mag				 ", Gyro.getMagY());
//	    SmartDashboard.putNumber("Z-Mag				 ", Gyro.getMagZ());
//	    SmartDashboard.putNumber("Yaw				 ", Gyro.getYaw());
//	    SmartDashboard.putNumber("Roll				 ", Gyro.getRoll());
//	    SmartDashboard.putNumber("Pitch				 ", Gyro.getPitch());
//	    SmartDashboard.putNumber("Pressure			 ", Gyro.getBarometricPressure());
//	    SmartDashboard.putNumber("Temperature		 ", Gyro.getTemperature());
//	    SmartDashboard.putNumber("X-Rate			 ", Gyro.getRateX());
//	    SmartDashboard.putNumber("Y-Rate			 ", Gyro.getRateY());
//	    SmartDashboard.putNumber("Z-Rate			 ", Gyro.getRateZ());
//	    
	    SmartDashboard.putNumber("Left Mag Position	 ", rearLeft.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Right Mag Position ", rearRight.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Left Front Percent ", frontLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Left Rear Percent	 ", rearLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Front Percent", frontRight.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Rear Percent ", rearRight.getMotorOutputPercent());
	    
	    SmartDashboard.putString("Light Position	 ", FMS);
		SmartDashboard.putNumber("flag				 ", flag);
		SmartDashboard.putNumber("HallEffect		 ", Total);



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
	public void AutoLine(){
		int Auto = 40960; // pulses for 140 inches
		
		
		rearLeft.set(ControlMode.MotionMagic, Auto);
		rearRight.set(ControlMode.MotionMagic, -1*Auto);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
		if((rearLeft.getSelectedSensorVelocity(0) == 0) && (time.get() > 1)) {
			flag = 42;
		}
		
	}
	
	public void FirstMovement() {
		rearLeft.set(ControlMode.MotionMagic, First);
		rearRight.set(ControlMode.MotionMagic, -1*First);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
		if((rearLeft.getSelectedSensorVelocity(0) == 0) && (time.get() > 1)) {
			flag = 1;
			time.reset();
			if(m_autoSelected != "Test") {
					rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
					rearRight.setSelectedSensorPosition(0, 0, TimeOut);
			}
		}
	}
	
	public void SecondMovement() {
	
	if(FMS.charAt(0) == 'L') {
		rearLeft.set(ControlMode.MotionMagic, Second);
		rearRight.set(ControlMode.MotionMagic, Second);//Right motor is Physically inverted so this is a Negative
		frontLeft.set(ControlMode.PercentOutput, -1 * rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, -1*rearRight.getMotorOutputPercent());
		}else{
		rearLeft.set(ControlMode.MotionMagic, -1* Second);
		rearRight.set(ControlMode.MotionMagic, -1* Second);
		frontLeft.set(ControlMode.PercentOutput,  -1*rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, -1*rearRight.getMotorOutputPercent());
	}
	
	if((rearLeft.getSelectedSensorVelocity(0) == 0) && (time.get() > 1)) {
		flag = 2;
		time.reset();
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
	}
}
	
	public void ThirdMovement() {
		rearLeft.set(ControlMode.MotionMagic, Third);
		rearRight.set(ControlMode.MotionMagic, -1*Third);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
		
		if((rearLeft.getSelectedSensorVelocity(0) == 0)&& (time.get() > 1)) {
			flag = 3;
			rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
			rearRight.setSelectedSensorPosition(0, 0, TimeOut);

		}
	}
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		
		FMS = DriverStation.getInstance().getGameSpecificMessage();
		
		System.out.println("Auto selected: " + m_autoSelected);
		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
		
		rearLeft.setIntegralAccumulator(0, 0, TimeOut);
		rearRight.setIntegralAccumulator(0, 0, TimeOut);
		
		flag= 0;
		Total = 0;

	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Left Error			:", rearLeft.getClosedLoopError(0));
		SmartDashboard.putNumber("Right Error			:", rearRight.getClosedLoopError(0));
  
		SmartDashboard.putNumber("TIME					:", time.get());
	    SmartDashboard.putString("Light Position	 	:", FMS);
		SmartDashboard.putNumber("flag				 	:", flag);
		SmartDashboard.putNumber("HallEffect		 	:", Total);
		
	    SmartDashboard.putNumber("Left Mag Position	 	:", rearLeft.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Right Mag Position 	:", rearRight.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Left Front Percent 	:", frontLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Left Rear Percent	 	:", rearLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Front Percent	:", frontRight.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Rear Percent 	:", rearRight.getMotorOutputPercent());
	    
	    SmartDashboard.putString("Light Position	 	:", FMS);
		SmartDashboard.putNumber("flag				 	:", flag);
		SmartDashboard.putNumber("Revolutions for Lift	:", Total);
		SmartDashboard.putNumber("LIFT					:", lift);
		SmartDashboard.putNumber("Hall Effect Sensor	:", Hall.getAverageVoltage());
		SmartDashboard.putNumber("Bottom Limit Switch", LimitB.getAverageVoltage());
		SmartDashboard.putNumber("X-Angle			 ", Gyro.getAngleX());
	    SmartDashboard.putNumber("Y-Angle			 ", Gyro.getAngleY());
	    SmartDashboard.putNumber("Z-Angle			 ", Gyro.getAngleZ());
	    SmartDashboard.putNumber("X-Acceleration	 ", Gyro.getAccelX());
	    SmartDashboard.putNumber("Y-Acceleration	 ", Gyro.getAccelY());
	    SmartDashboard.putNumber("Z-Acceleration	 ", Gyro.getAccelZ());

		
		if(Hall.getAverageVoltage() < 0.1) {
			if(Lift.getSpeed() > 0 && h == 0) {
				Total = Total + 1;
				h = 1;
			}else if(Lift.getSpeed() < 0 && h == 0) {
				Total = Total - 1;
				h = 1;
			}else {
				Total = Total + 0;
			}
		}else {
			h = 0;
		}


		
		switch (m_autoSelected) {
		
			case rSwitch:
				
				if(time.get() < 3) {// Move Robot forward for 3 Seconds
					
					mech.driveCartesian(0.0, 0.3, 0.0);
					
				}else {
					if(FMS.charAt(0)=='R') {
						if(Total < 10) {// Move Lift Up 10 revolutions

							Lift.set(0.4);					
						}else {
							
							Lift.set(0.0);
						}
						if(time.get() < 4 && time.get() > 3 ) {//Turn Counter Clockwise for 1 Second
							
							mech.driveCartesian(0, 0, -0.4);
							
						}else if(time.get() < 5.0 && time.get() > 4.0) { //Move Forward for 1 Second
							
							mech.driveCartesian(0.0, 0.3, 0.0); 
							
						}else if(time.get() > 5.5) { //Stop and drop Block
							
							mech.driveCartesian(0.0, 0.0, 0.0);
							Grab1.set(0.4);
							Grab2.set(0.4);
							
						}else {//DO NOTHIN'
								
							mech.driveCartesian(0.0, 0.0, 0.0);
								
						}
					}
				}
				
			break;
			case Forward:
			default:
				
				if(time.get() < 3) {  
				
				rearLeft.set(ControlMode.PercentOutput, 0.3);
				rearRight.set(ControlMode.PercentOutput, -0.3); 
				frontLeft.set(ControlMode.PercentOutput, 0.3);
				frontRight.set(ControlMode.PercentOutput, -0.3);
				}else {
				rearLeft.set(ControlMode.PercentOutput, 0.0);
				rearRight.set(ControlMode.PercentOutput, 0.0);
				frontLeft.set(ControlMode.PercentOutput, 0.0);
				frontRight.set(ControlMode.PercentOutput, 0.0);
				}
				flag = 42;

			break;
			
			case Test:
				if(flag == 0) {
					AutoLine();
				}else {
					if(Total < 4 && Top < 4.5) {
						Lift.set(1);
					}else {
						Lift.set(0.0);
					}
				}
//				if(time.get() < 3) {  	
//				mech.driveCartesian(0.0, 0.3, 0.0, 0.0);
//				}else {
//					if(Total < 10) {// Move Lift Up 10 revolutions
//						Lift.set(0.4);
//					}else {
//						Lift.set(0.0);	
//					}
//					if(FMS.charAt(0)=='R') {
//						if(time.get() < 4.5 && time.get() > 3 ) {
//						}else if(time.get() < 5.5 && time.get() > 4.5) {
//							mech.driveCartesian(0, 0, -0.4);
//						}else if(time.get() > 5.5 && time.get() < 6.5) { 
//							mech.driveCartesian(0.0, 0.3, 0.0); 
//						}else if(time.get() > 6.5) {
//							mech.driveCartesian(0.0, 0.0, 0.0);
//							Grab1.set(0.4);
//							Grab2.set(0.4);
//
//						}
//					}else {
//						rearLeft.set(ControlMode.PercentOutput, 0.0);
//						rearRight.set(ControlMode.PercentOutput, 0.0);
//						frontLeft.set(ControlMode.PercentOutput, 0.0);
//						frontRight.set(ControlMode.PercentOutput, 0.0);
//					}
//			}
				
			break;
			
			case Switch:
				Claw.set(false);

				System.out.println("Switch: flag:"+flag+" Revolutions : "+Total);
				if(flag == 0) {
					FirstMovement();
				}else if(flag == 1) {
					SecondMovement();
				}else if(flag == 2) {
					ThirdMovement();
				}else if((flag == 3) && (Total >= 5)) {
					mech.driveCartesian(0.0, 0.0, 0.0, 0.0);
					Grab1.set(-0.7);
					Grab2.set(0.7);
				}
				if(flag < 3) {
					Grab1.set(0.1);
					Grab2.set(-0.1);
				}
					if(Total < 7) {// Move Lift Up 4 revolutions
						Lift.set(1);
					}else {
						Lift.set(0.0);	
					}
				
				
			break;
		}
		Timer.delay(0.01);
	}
	@Override
	public void teleopInit() {
		time.reset();
		Gyro.reset();
		Gyro.calibrate();
	}
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		boolean Safety =		mech.isSafetyEnabled();
		Top = LimitT.getVoltage();
		Bottom = LimitB.getAverageVoltage();

		
		// Use the joystick X axis for lateral movement, Y axis for forward
		// movement, and Z axis for rotation.

//		debug();
		SmartDashboard.putBoolean("Robot Safety			:", Safety);
		SmartDashboard.putNumber("Left Error			:", rearLeft.getClosedLoopError(0));
		SmartDashboard.putNumber("Right Error			:", rearRight.getClosedLoopError(0));
  
		SmartDashboard.putNumber("TIME					:", time.get());
		SmartDashboard.putNumber("flag				 	:", flag);
		SmartDashboard.putNumber("HallEffect		 	:", Total);
		
	    SmartDashboard.putNumber("Left Mag Position	 	:", rearLeft.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Right Mag Position 	:", rearRight.getSelectedSensorPosition(0));
	    SmartDashboard.putNumber("Left Front Percent 	:", frontLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Left Rear Percent	 	:", rearLeft.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Front Percent	:", frontRight.getMotorOutputPercent());
	    SmartDashboard.putNumber("Right Rear Percent 	:", rearRight.getMotorOutputPercent());
	    
		SmartDashboard.putNumber("flag				 	:", flag);
		SmartDashboard.putNumber("Revolutions for Lift	:", Total);
		SmartDashboard.putNumber("LIFT					:", lift);
		SmartDashboard.putNumber("Hall Effect Sensor	:", Hall.getAverageVoltage());
		SmartDashboard.putNumber("Bottom Limit Switch", LimitB.getAverageVoltage());
		SmartDashboard.putNumber("X-Angle			 ", Gyro.getAngleX());
	    SmartDashboard.putNumber("Y-Angle			 ", Gyro.getAngleY());
	    SmartDashboard.putNumber("Z-Angle			 ", Gyro.getAngleZ());
	    SmartDashboard.putNumber("X-Acceleration	 ", Gyro.getAccelX());
	    SmartDashboard.putNumber("Y-Acceleration	 ", Gyro.getAccelY());
	    SmartDashboard.putNumber("Z-Acceleration	 ", Gyro.getAccelZ());
	    
	    double Cal = -0.58/2 *Math.pow(time.get(), 2);
	    
	    SmartDashboard.putNumber("New Gyro Angle", Cal);

		if(Hall.getAverageVoltage() < 0.1) {
			if(Lift.getSpeed() > 0 && h == 0) {
				Total = Total + 1;
				h = 1;
			}else if(Lift.getSpeed() < 0 && h == 0) {
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
		
		if(X52.getX() < -0.1 || X52.getX() > 0.1) {
			X = Math.pow(X52.getX(), 3);
		}else {
			X = 0;
		}
		if(X52.getY() < -0.1 || X52.getY() > 0.1) {
			Y = -1*Math.pow(X52.getY(), 3);
		}else {
			Y = 0;
		}
		if(X52.getRawAxis(5) < -0.1 || X52.getRawAxis(5) > 0.1) {
			rZ = .5*Math.pow(X52.getRawAxis(5), 5);
			
		}else {
			rZ = 0;
		}

		mech.driveCartesian(X, Y, rZ, 0.0); 
		
		
		double Speed = (X52.getRawAxis(6)+1)/2;
		
		if(X52.getRawButton(E) && Top < 4.5) {
			Lift.set(Speed);
			lift = 1;
		}else if(X52.getRawButton(i) && Bottom < 4.5) {
			Lift.set(-1*Speed);
			lift = 1;
		}else { 
			Lift.set(0);
			lift = 0;
		}
		
		
		
		if(X52.getRawButton(C)) {
			Grab1.set(-0.45);
			Grab2.set(0.45);
			grab = 1;
		}else if(X52.getRawButton(Trigger)) {
			Grab1.set(1);  //Values need to be reversed for Comp. Robot
			Grab2.set(-1);
			grab = 1;
		}else if(X52.getRawButton(Fire)) {
			Grab1.set(-1);  //Values need to be reversed for Comp. Robot
			Grab2.set(1);
			grab = 1;
		}else if(rZ > 0.3 || rZ < -0.3 || X52.getRawButton(D)) {
			Grab1.set(0.45);
			Grab2.set(-0.45);
		}else {
			Grab1.set(0);
			Grab2.set(0);
			grab = 0;
		}
		
		if((X52.getRawButton(B) || Log.getRawButton(X2))&& g == 0) {
			g = 1;
		}else if(!(X52.getRawButton(B) || Log.getRawButton(X2))&& g == 1) {
			g = 2;
		}else if ((X52.getRawButton(B) || Log.getRawButton(X2)) && g == 2) {
			g = 3;
		}else if(!(X52.getRawButton(B) || Log.getRawButton(X2)) && g == 3) {
			g = 0;
		}
		
		if(g == 1 || g == 2) {
			Claw.set(true);
			claw = 1;
		}else { 
			Claw.set(false);
			claw = 0;
		}
		

		
		//For Logictech Controller
		
		if(Log.getRawButton(B2) && lift == 0 && Top < 4.5) {
			Lift.set(Speed);
		}else if(Log.getRawButton(A2) && lift == 0 && Bottom < 4.5) {
			Lift.set(-1*Speed);
		}
		
		if(Log.getRawButton(RB) && grab == 0) {
			Grab1.set(-0.45);//Values need to be reversed for Comp. Robot
			Grab2.set(0.45);
		}else if(Log.getRawButton(LB) && grab == 0) {
			Grab1.set(1);
			Grab2.set(-1);
		}else if(Log.getRawButton(Y2) && grab == 0) {
			Grab1.set(-1);  //Values need to be reversed for Comp. Robot
			Grab2.set(1);
			
		}
		
//		if(Log.getRawButton(X2) && claw == 0) {
//			Claw.set(true);
//		}
		
		if(Log.getRawButton(Back) && c == 0) {
			c = 1;
		}else if(!Log.getRawButton(Back) && c == 1) {
			c = 2;
		}else if (Log.getRawButton(Back) && c == 2) {
			c = 3;
		}else if(!Log.getRawButton(Back) && c == 3) {
			c = 0;
		}
		
		if(c == 1 || c == 2) {
			Comp.stop();
		}else { 
			Comp.start();
		}
		
	    Timer.delay(0.001);
	}

	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
