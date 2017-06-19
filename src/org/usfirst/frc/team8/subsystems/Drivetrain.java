package org.usfirst.frc.team8.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;


public class Drivetrain extends Subsystem{

	private CANTalon leftMasterTalon;
	private CANTalon leftSlaveTalon;
	private CANTalon rightMasterTalon;
	private CANTalon rightSlaveTalon;
	private Joystick forward;
	private Joystick turn;
	private double speed=1;
	private double time=0;
	private double kTurnConstant = 0.319024;

	public enum DriveState {
		ENCODER_DRIVE,
		TIMER_DRIVE,
		TELEOP,
		DISABLED		
	}

	private DriveState state = DriveState.DISABLED;

	public Drivetrain(Joystick forward, Joystick turn) {
		leftMasterTalon = new CANTalon(2);
		leftSlaveTalon = new CANTalon(3);
		rightMasterTalon = new CANTalon(4);
		rightSlaveTalon = new CANTalon(1);
		rightMasterTalon.setInverted(true);
		this.forward = forward;
		this.turn = turn;
	}

	@Override
	public void init() {	
		leftSlaveTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlaveTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftMasterTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		leftSlaveTalon.set(leftMasterTalon.getDeviceID());
		rightMasterTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		rightSlaveTalon.set(rightMasterTalon.getDeviceID());
		rightMasterTalon.configEncoderCodesPerRev(40);
		leftMasterTalon.configEncoderCodesPerRev(40);
	}

	@Override
	public void update() {
		switch (state) {
		case ENCODER_DRIVE:
			if(Math.abs(leftMasterTalon.getError()) < 5 && leftMasterTalon.get() == 0 && Math.abs(rightMasterTalon.getError()) < 5 && leftMasterTalon.get() == 0) {
				leftMasterTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				rightMasterTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				state = DriveState.TELEOP;
			}
			break;
		case TIMER_DRIVE:
			if(System.currentTimeMillis() > time) {
				state = DriveState.DISABLED;
				break;
			}
			leftMasterTalon.set(speed);
			leftSlaveTalon.set(speed);
			rightMasterTalon.set(speed);
			rightMasterTalon.set(speed);
			break;
		case TELEOP:
			double leftSpeed = Math.min(Math.max(-1.0,-forward.getY() + turn.getX() ), 1.0);
			double rightSpeed = Math.min(Math.max(-1.0, -forward.getY() - turn.getX()), 1.0);
			leftMasterTalon.set(leftSpeed);
			leftSlaveTalon.set(leftSpeed);
			rightMasterTalon.set(rightSpeed);
			rightSlaveTalon.set(rightSpeed);
			System.out.println("left encoder: " + leftMasterTalon.getEncPosition());
			System.out.println("right encoder: " + rightMasterTalon.getEncPosition());
			break;
		case DISABLED:
			leftMasterTalon.set(0);
			leftSlaveTalon.set(0);
			rightMasterTalon.set(0);
			rightSlaveTalon.set(0);
			break;

		}
	}

	@Override
	public void disable() {

	}

	public void setDriveTIme() {

	}

	public void setDriveSpeed(double leftSpeed, double rightSpeed) {
		leftMasterTalon.set(leftSpeed);
		rightMasterTalon.set(rightSpeed);
	}

	public void setSetpoint() {

	}

	public void driveTime(double speed, double time) {
		speed = this.speed;
		time = (this.time * 1000) + System.currentTimeMillis();
		state = DriveState.TIMER_DRIVE;


	}

	public void setState(DriveState stateSet) {
		state = stateSet;
	}

	public DriveState getState() {
		return state;
	}

	public CANTalon getLeftMasterTalon() {
		return leftMasterTalon;
	}

	public CANTalon getRightMasterTalon() {
		return rightMasterTalon;
	}

	public void setEncoderDrive(double targetDistance, double p, double i, double d) {
		state = DriveState.ENCODER_DRIVE;
		leftMasterTalon.setEncPosition(0);
		rightMasterTalon.setEncPosition(0);
		leftMasterTalon.changeControlMode(CANTalon.TalonControlMode.Position);
		rightMasterTalon.changeControlMode(CANTalon.TalonControlMode.Position);
		leftMasterTalon.set(targetDistance);
		rightMasterTalon.set(targetDistance);
		leftMasterTalon.setPID(p, i, d);
		rightMasterTalon.setPID(p, i, d);
	}

}
