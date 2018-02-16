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
	
	WPI_TalonSRX talon = new WPI_TalonSRX(0);
	

	@Override
	public void robotInit() {
		WPI_TalonSRX frontLeft = new WPI_TalonSRX(3);
		WPI_TalonSRX rearLeft = new WPI_TalonSRX(5);
		WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
		WPI_TalonSRX rearRight = new WPI_TalonSRX(4);
		
		Victor Lift = new Victor(0);

		// Invert the left side motors.
		// You may need to change or remove this to match your robot.
//		frontLeft.setInverted(true);
//		rearLeft.setInverted(true);

		m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

		X52 = new Joystick(kJoystickChannel);
		
	}

	@Override
	public void teleopPeriodic() {
		// Use the joystick X axis for lateral movement, Y axis for forward
		// movement, and Z axis for rotation.
		m_robotDrive.driveCartesian(X52.getX(), X52.getY(), X52.getZ(), Gyro.getAngleZ());
		
		
		double Speed = (X52.getRawAxis(6)+1)/2;
		
		
	}
}
