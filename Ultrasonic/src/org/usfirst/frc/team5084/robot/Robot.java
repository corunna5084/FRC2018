/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a sample program demonstrating how to use an ultrasonic sensor and
 * proportional control to maintain a set distance from an object.
 */

public class Robot extends IterativeRobot {
	// distance in inches the robot wants to stay from an object
	private static final double kHoldDistance = 12.0;

	// factor to convert sensor values to a distance in inches
	private static final double kValueToInches = 0.125;

	// proportional speed constant
	private static final double kP = 0.05;

	private static final int kLeftMotorPort = 0;
	private static final int kRightMotorPort = 1;
	private static final int kUltrasonicPort = 0;

	private AnalogInput m_ultrasonic = new AnalogInput(kUltrasonicPort);
	private DifferentialDrive m_robotDrive
			= new DifferentialDrive(new Spark(kLeftMotorPort),
			new Spark(kRightMotorPort));

	/**
	 * Tells the robot to drive to a set distance (in inches) from an object
	 * using proportional control.
	 */
	public void teleopPeriodic() {
		// sensor returns a value from 0-4095 that is scaled to inches
		double currentDistance = m_ultrasonic.getValue() * kValueToInches;

		// convert distance error to a motor speed
		double currentSpeed = (kHoldDistance - currentDistance) * kP;

		// drive robot
		m_robotDrive.arcadeDrive(currentSpeed, 0);
	}
}
