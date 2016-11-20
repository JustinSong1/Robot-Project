package org.usfirst.frc.team8.input;

import edu.wpi.first.wpilibj.Joystick;

public class Joysticks {
	private Joystick driveStick;
	private Joystick turnStick;
	private Joystick shooterStick;
	public Joysticks() {
		Joystick driveStick = new Joystick(0);
		Joystick turnStick = new Joystick(1);
		Joystick shooterStick = new Joystick(2);
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
