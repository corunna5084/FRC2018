/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import com.analog.adis16448.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
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
	
	ADIS16448_IMU Gyro = new ADIS16448_IMU();

	private MecanumDrive mech;
	Joystick X52;
	
	AnalogInput LimitT = new AnalogInput(0);
	AnalogInput LimitB = new AnalogInput(1);
	AnalogInput HallEffect = new AnalogInput(3);
	
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
//	Victor Lift = new Victor(0);
//	Victor Grab1 = new Victor(9);
//	Victor Grab2 = new Victor(8);

	double FinnalPos;
	double lDist;
	int lPos;
	double rDist;
	int rPos;
	String gameData;
	double P = 0.0;
	double I = 0.0;
	double D = 0.0;
	double F = 0.0248;
	double angle = Gyro.getAngleZ();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
		
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		frontLeft.setSafetyEnabled(false);
		rearLeft.setSafetyEnabled(false);
		frontRight.setSafetyEnabled(false);
		rearRight.setSafetyEnabled(false);
		
//		Lift.setExpiration(0.1);
//		Grab1.setExpiration(0.1);
//		Grab2.setExpiration(0.1);

		mech = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
		X52 = new Joystick(0);

		rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);		
		rearLeft.setSelectedSensorPosition(0, 0, 10);
		rearLeft.setSensorPhase(true);
		rearRight.setSensorPhase(true);
		

		
		Gyro.reset();
		Gyro.calibrate();
		
		rearLeft.selectProfileSlot(0, 0);
		rearLeft.config_kP(0, P, 10);
		rearLeft.config_kI(0, I, 10);
		rearLeft.config_kD(0, D, 10);
		rearLeft.config_kF(0, F, 10);


		
	
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
		
		rearLeft.setSelectedSensorPosition(0, 0, 10);
		FinnalPos = 0;

	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {

		lPos = rearLeft.getSelectedSensorPosition(0);
		lDist = ((lPos)*6*Math.PI)/4096;
		rPos = rearRight.getSelectedSensorPosition(0);
		rDist = ((rPos)*6*Math.PI)/4096;
		
		SmartDashboard.putNumber("lPosition", lPos);
		SmartDashboard.putNumber("lDist", lDist);
		SmartDashboard.putNumber("rPosition", rPos);
		SmartDashboard.putNumber("rDist", rDist);
		SmartDashboard.putNumber("Final Position", FinnalPos);
		SmartDashboard.putString("First Switch", gameData);
		
		

		switch (m_autoSelected) {
			case kCustomAuto:
				rearLeft.set(ControlMode.Position, 4096);
				frontLeft.set(0);
				frontRight.set(0);
				rearRight.set(0);
				
				break;
			case kDefaultAuto:
			default:
				rearLeft.set(ControlMode.Position, 100);
				frontLeft.set(0);
				frontRight.set(0);
				rearRight.set(0);
 
				break;
		}
		Timer.delay(0.01);
	}
	
	@Override
	public void teleopInit() {

		Gyro.reset();
		Gyro.calibrate();

	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
//		lPos = rearLeft.getSelectedSensorPosition(0);
//		lDist = ((lPos)*6*Math.PI)/4096;
//		rPos = rearRight.getSelectedSensorPosition(0);
//		rDist = ((rPos)*6*Math.PI)/4096;
		
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
			rZ = Math.pow(X52.getRawAxis(5), 3);
		}else {
			rZ = 0;
		}
		mech.driveCartesian(X, Y, rZ, angle);
		
		SmartDashboard.putNumber("lPosition", lPos);
		SmartDashboard.putNumber("lDist", lDist);
		SmartDashboard.putNumber("rPosition", rPos);
		SmartDashboard.putNumber("rDist", rDist);
//		SmartDashboard.putNumber("Final Position", FinnalPos);
		
		SmartDashboard.putNumber("Gyro-X", Gyro.getAngleX());
	    SmartDashboard.putNumber("Gyro-Y", Gyro.getAngleY());
	    SmartDashboard.putNumber("Gyro-Z", Gyro.getAngleZ());
	    SmartDashboard.putNumber("Accel-X", Gyro.getAccelX());
	    SmartDashboard.putNumber("Accel-Y", Gyro.getAccelY());
	    SmartDashboard.putNumber("Accel-Z", Gyro.getAccelZ());
	    SmartDashboard.putNumber("Pitch", Gyro.getPitch());
	    SmartDashboard.putNumber("Roll", Gyro.getRoll());
	    SmartDashboard.putNumber("Yaw", Gyro.getYaw());
	    SmartDashboard.putNumber("Pressure: ", Gyro.getBarometricPressure());
	    SmartDashboard.putNumber("Temperature: ", Gyro.getTemperature()); 
	    
	    SmartDashboard.putNumber("Hall Effect Sensor Voltage", HallEffect.getVoltage());
	    
	    
	    
	    
	    
		Timer.delay(0.01);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
