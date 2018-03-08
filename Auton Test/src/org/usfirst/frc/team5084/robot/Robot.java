/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
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
	private static final String Forward = "Default";
	private static final String Switch = "Switch Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	
	String gameData;		
	int TimeOut = 10;
	
	double lP = 0.68;
	double lI = 0.0065;
	double lD = 6.8;
	double lF = 0.3365;
	int lV = 2280;
	
	double rP = 2.224;
	double rI = 0.03;
	double rD = 22.24;
	double rF = 0.3545;
	int rV = 2165;
	
	int flag;
	
	
	int First = 7175;
	int Second = 11305;
	int Third = 15219;
	String FMS;
	
	
	AnalogInput LimitT = new AnalogInput(0);
	AnalogInput LimitB = new AnalogInput(1);
	AnalogInput HallEffect = new AnalogInput(3);
		
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
	WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
	
	Victor Lift = new Victor(0);
	Victor Grabl = new Victor(9);
	Victor Grabr = new Victor(8);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", Forward);
		m_chooser.addObject("Switch Auto", Switch);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		CameraServer.getInstance().startAutomaticCapture();

		
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
		frontLeft.setInverted(true);
		frontRight.setInverted(false);
		
//		rearLeft.setPID();
		
		rearLeft.setSensorPhase(true);
		rearLeft.setInverted(true);
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
		
		rearLeft.setSelectedSensorPosition(0, 0, TimeOut);
		rearRight.setSelectedSensorPosition(0, 0, TimeOut);
		
		rearLeft.setIntegralAccumulator(0, 0, TimeOut);
		rearRight.setIntegralAccumulator(0, 0, TimeOut);
		
		flag= 0;

	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putNumber("right", rearRight.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("left", rearLeft.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("flag", flag);

		
		switch (m_autoSelected) {
			case Switch:
				if(gameData.charAt(0) == 'L' || gameData.charAt(0) == 'l') {
					//no code
				}
				
				break;
			case Forward:
			default:
				FirstMovement();
				Timer.delay(1);
				SecondMovement();
				Timer.delay(1);
				Lift.set(.5);
				Timer.delay(3);
				ThirdMovement();
				Timer.delay(1);
				Grabl.set(0.3); Grabr.set(0.3);
				Timer.delay(3);	
				
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
