package org.usfirst.frc.team8.input;

import edu.wpi.first.wpilibj.Joystick;

public class Joysticks {
	private Joystick driveStick;
	private Joystick turnStick;
	private Joystick shooterStick;
	public Joysticks() {
		driveStick = new Joystick(0);
		turnStick = new Joystick(1);
		shooterStick = new Joystick(2);
	}
	
	public Joystick getDriveStick() {
		return driveStick;
	}
	
	public Joystick getTurnStick() {
		return turnStick;
	}
	
	public Joystick getShooterStick() {
		return shooterStick;
	}
}
