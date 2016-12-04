package org.usfirst.frc.team8.input;

import edu.wpi.first.wpilibj.Joystick;

public class Joysticks {
	private Joystick driveStick;
	private Joystick turnStick;
	private Joystick shooterStick;
	
	/**
	 * Creates three Joysticks
	 * One for driving
	 * One for turing
	 * One for shooting
	 */
	public Joysticks() {
		driveStick = new Joystick(0);
		turnStick = new Joystick(1);
		shooterStick = new Joystick(2);
	}
	
	/**
	 * 
	 * @return The Drive Stick
	 */
	public Joystick getDriveStick() {
		return driveStick;
	}
	
	/**
	 * 
	 * @return The Turn Stick
	 */
	public Joystick getTurnStick() {
		return turnStick;
	}
	
	/**
	 * 
	 * @return The Shooter Stick
	 */
	public Joystick getShooterStick() {
		return shooterStick;
	}
}
