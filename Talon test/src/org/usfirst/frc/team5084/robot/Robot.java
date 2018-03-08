/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5084.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {  
	TalonSRX _talon = new TalonSRX(3);  
	Joystick _joy = new Joystick(0); 
	StringBuilder _sb = new StringBuilder(); 


public void robotInit() {     
	/* first choose the sensor */   
	_talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);   
	_talon.setSensorPhase(true);   _talon.setInverted(false);      
	/* Set relevant frame periods to be at least as fast as periodic rate*/   
	_talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);  
	_talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,  Constants.kTimeoutMs); 


 /* set the peak and nominal outputs */   
_talon.configNominalOutputForward(0, Constants.kTimeoutMs);  
_talon.configNominalOutputReverse(0, Constants.kTimeoutMs);   
_talon.configPeakOutputForward(1, Constants.kTimeoutMs);  
_talon.configPeakOutputReverse(-1, Constants.kTimeoutMs);      
/* set closed loop gains in slot0 - see documentation */   
_talon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);  
_talon.config_kF(0, 0.2, Constants.kTimeoutMs);   
_talon.config_kP(0, 0.2, Constants.kTimeoutMs);  
_talon.config_kI(0, 0, Constants.kTimeoutMs);  
_talon.config_kD(0, 0, Constants.kTimeoutMs);   
/* set acceleration and vcruise velocity - see documentation */  
_talon.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);  
_talon.configMotionAcceleration(6000, Constants.kTimeoutMs);   
/* zero the sensor */  
_talon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);  
} 


/**   * This function is called periodically during operator control   */ 
public void teleopPeriodic() {   
	/* get gamepad axis - forward stick is positive */   
	double leftYstick = -1.0 * _joy.getY();   
	/* calculate the percent motor output */   
	double motorOutput = _talon.getMotorOutputPercent();   
	/* prepare line to print */  
	_sb.append("\tOut%:");  
	_sb.append(motorOutput);  
	_sb.append("\tVel:");   
	_sb.append(_talon.getSelectedSensorVelocity(Constants.kPIDLoopIdx)); 


 if (_joy.getRawButton(1)) {   
	 /* Motion Magic */    
	 double targetPos = leftYstick * 4096 * 10.0; 
	 /* 4096 ticks/rev * 10 Rotations in either direction*/   
	 _talon.set(ControlMode.MotionMagic, targetPos);  
 

  /* append more signals to print when in speed mode. */   
 _sb.append("\terr:");  
 _sb.append(_talon.getClosedLoopError(Constants.kPIDLoopIdx));
 _sb.append("\ttrg:"); 
 _sb.append(targetPos); 
 } else {   
	 /* Percent output mode */   
	 _talon.set(ControlMode.PercentOutput, leftYstick);  
	 }   /* instrumentation */  
 //Instrum.Process(_talon, _sb); 
 
}
}