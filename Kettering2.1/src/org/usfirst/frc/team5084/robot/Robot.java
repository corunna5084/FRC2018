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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
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
	private static final String Forward = "50/50 left Auto";
	private static final String Switch = "Switch Auto";
	private static final String Test = "Testing";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	ADIS16448_IMU Gyro = new ADIS16448_IMU();

	private MecanumDrive mech;
	private Joystick X52;
	
	AnalogInput LimitT = new AnalogInput(1);
	AnalogInput LimitB = new AnalogInput(0);	
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
	
	 Timer time = new Timer();
		
	Victor Lift = new Victor(0);
	Victor Grab1 = new Victor(9);
	Victor Grab2 = new Victor(8);

	int E = 8;
	int i = 30;
	int Trigger = 1;
	int C = 5;
	int D = 7;
	
	double FinnalPos;
	int TimeOut = 10;
	
	double lP = 0.0;
	double lI = 0.0;
	double lD = 0.0;
	double lF = 0.189;
	int lV = 4066;
	
	double rP = 0.0;
	double rI = 0.0;
	double rD = 0.0; 
	double rF = 0.183;
	int rV = 4198;
	
	int flag;
	int First = 7175;
	int Second = 11305; 
	int Third = 15219;
	String FMS;
	int Flag = 0;
	
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

		X52 = new Joystick(0);
		
		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);
		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		
		rearRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TimeOut);
		
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
		Gyro.reset();
		Gyro.calibrate();
		
		m_chooser.addDefault("Basic Auto", Forward);
		m_chooser.addObject("Test", Test);
		m_chooser.addObject("Switch Auto", Switch);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		CameraServer.getInstance().startAutomaticCapture();

		rearLeft.setSafetyEnabled(false); 
		frontLeft.setSafetyEnabled(false);
		rearRight.setSafetyEnabled(false);
		frontRight.setSafetyEnabled(false);
		
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
		int Auto = 30437; // pulses for 140 inches
		
//		Lift.set(.75);
//		Timer.delay(3);
//		Lift.set(0);
		rearLeft.set(-0.5);
		rearRight.set(.5);
		frontLeft.set(-0.5);
		frontRight.set(0.5);
		Timer.delay(3);
		rearLeft.set(0.0);
		rearRight.set(0.0);
		frontLeft.set(0.0);
		frontRight.set(0.0);

		flag = 42;
		
	}
	
	public void FirstMovement() {
		rearLeft.set(ControlMode.Position, First);
		rearRight.set(ControlMode.Position, First);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
		
		flag = 1;
	}
	
	public void SecondMovement() {
	
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
	
	flag = 2;
}
	
	public void ThirdMovement() {
		rearLeft.set(ControlMode.Position, Third);
		rearRight.set(ControlMode.Position, Third);
		frontLeft.set(ControlMode.PercentOutput, rearLeft.getMotorOutputPercent());
		frontRight.set(ControlMode.PercentOutput, rearRight.getMotorOutputPercent());
		
		flag = 3;
	}
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
		
		rearLeft.setIntegralAccumulator(0, 0, TimeOut);
		rearRight.setIntegralAccumulator(0, 0, TimeOut);
		
		flag= 0;
		FMS = DriverStation.getInstance().getGameSpecificMessage();
		
		time.start();

	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		SmartDashboard.putNumber("right", rearRight.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("left", rearLeft.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("flag", flag);
		SmartDashboard.putNumber("Left Current Position", rearLeft.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Left Error", rearLeft.getClosedLoopError(0));
		SmartDashboard.putNumber("Left Encoder Velocity", rearLeft.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Left Output Percent", rearLeft.getMotorOutputPercent());
		SmartDashboard.putNumber("TIME", time.get());
		SmartDashboard.putNumber("Right Current Position", rearRight.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Error", rearRight.getClosedLoopError(0));
		SmartDashboard.putNumber("Right Encoder Velocity", rearRight.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Right Output Percent", rearRight.getMotorOutputPercent());



		
		switch (m_autoSelected) {
			case Switch:
				FirstMovement();
				
				SecondMovement();
				
				Lift.set(.5);
				
				Lift.set(0);
				ThirdMovement();
				
				Grab1.set(0.3); Grab2.set(0.3);
				
				
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
				
				if(time.get() < 3) {  	
				rearLeft.set(ControlMode.PercentOutput, 0.3);
				rearRight.set(ControlMode.PercentOutput, -0.3); 
				frontLeft.set(ControlMode.PercentOutput, 0.3);
				frontRight.set(ControlMode.PercentOutput, -0.3);
				}else {
					if(FMS.charAt(0)=='R') {
						if(time.get() < 4.5 && time.get() > 3 ) {
							Lift.set(1);
						}else if(time.get() < 5.5 && time.get() > 4.5) {
							mech.driveCartesian(0, 0, -0.4);
						}else if(time.get() > 5.5 && time.get() < 6.5) { 
							mech.driveCartesian(0.0, 0.3, 0.0); 
							Lift.set(0.0);
						}else if(time.get() > 6.5) {
							mech.driveCartesian(0.0, 0.0, 0.0);
							Grab1.set(0.4);
							Grab2.set(0.4);

						}
					}else {
						rearLeft.set(ControlMode.PercentOutput, 0.0);
						rearRight.set(ControlMode.PercentOutput, 0.0);
						frontLeft.set(ControlMode.PercentOutput, 0.0);
						frontRight.set(ControlMode.PercentOutput, 0.0);
					}
			}
				
			break;
		}
		Timer.delay(0.01);
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		// Use the joystick X axis for lateral movement, Y axis for forward
		// movement, and Z axis for rotation.

		

		
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
		double Top = LimitT.getVoltage();
		double Bottom = LimitB.getVoltage();
		
		if(X52.getRawButton(E) && Top < 4.5) {
			Lift.set(Speed);
		}else if(X52.getRawButton(i) && Bottom < 4.5) {
			Lift.set(-1*Speed);
		}else { 
			Lift.set(0);
		}
		
		if(X52.getRawButton(C)) {
			Grab1.set(0.45);
			Grab2.set(0.45);
		}else if(X52.getRawButton(Trigger)) {
			Grab1.set(-1);  
			Grab2.set(-1);
		}else if(rZ != 0 || X52.getRawButton(D)) {
			Grab1.set(-0.4);
			Grab2.set(-0.4);
		}else {
			Grab1.set(0);
			Grab2.set(0);
		}
		
		
		
		SmartDashboard.putNumber("Gyro-X", Gyro.getAngleX());
	    SmartDashboard.putNumber("Gyro-Y", Gyro.getAngleY());
	    SmartDashboard.putNumber("Gyro-Z", Gyro.getAngleZ());
		
		
	    Timer.delay(0.01);
	}

	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
