package org.usfirst.frc.team8.subsystems;

import org.usfirst.frc.team8.Robot.AutoStates;
import org.usfirst.frc.team8.controllers.Controller;
import org.usfirst.frc.team8.controllers.DriveStraightController;
import org.usfirst.frc.team8.input.Joysticks;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain extends Subsystem{
	
	private CANTalon frontLeft;
	private CANTalon backLeft;
	private CANTalon frontRight;
	private CANTalon backRight;
	private Joystick forward;
	private Joystick turn;
	private double speed=1;
	private double time=0;
	private Encoder leftEncoder = new Encoder(3, 2);
	private Encoder rightEncoder = new Encoder(0, 1);
	private double setpoint;
	Controller driveTrainController;
	Joysticks joysticks = new Joysticks();
	private Drivetrain drivetrain = new Drivetrain(joysticks.getDriveStick(), joysticks.getTurnStick());

	
	public enum DriveState {
		ENCODER_DRIVE,
		TIMER_DRIVE,
		TELEOP,
		IDLE		
	}
	
	private DriveState state = DriveState.IDLE;
	
	public Drivetrain(Joystick forward, Joystick turn) {
		frontLeft = new CANTalon(2);
		backLeft = new CANTalon(3);
		frontRight = new CANTalon(4);
		backRight = new CANTalon(1);
		frontRight.setInverted(true);
		backRight.setInverted(true);
		this.forward = forward;
		this.turn = turn;
	}
	
	@Override
	public void init() {
		rightEncoder.setDistancePerPulse(1);
		leftEncoder.setDistancePerPulse(.07116171);
	}
	
	
	
	@Override
	public void update() {
        switch (state) {
        case ENCODER_DRIVE:
        	while(leftEncoder.getDistance() < setpoint) {
        		frontLeft.set(1);
            	backLeft.set(1);
            	frontRight.set(1);
            	backRight.set(1);
        	}
        	state = DriveState.IDLE;
        	break;
        case TIMER_DRIVE:
        	if(System.currentTimeMillis() > time) {
        		state = DriveState.IDLE;
        		break;
        	}
        	frontLeft.set(speed);
    		backLeft.set(speed);
    		frontRight.set(speed);
    		frontRight.set(speed);
        	break;
        case TELEOP:
        	double leftSpeed = Math.min(Math.max(-1.0,-forward.getY() + turn.getX() ), 1.0);
            double rightSpeed = Math.min(Math.max(-1.0, -forward.getY() - turn.getX()), 1.0);
            frontLeft.set(leftSpeed);
            backLeft.set(leftSpeed);
            frontRight.set(rightSpeed);
            backRight.set(rightSpeed);
            System.out.println("left encoder: " + leftEncoder.getDistance());
            System.out.println("right encoder: " + rightEncoder.getDistance());
        	break;
        case IDLE:
        	frontLeft.set(0);
        	backLeft.set(0);
        	frontRight.set(0);
        	backRight.set(0);
        	break;
        	
        }
	}

	@Override
	public void disable() {
		state = DriveState.IDLE;
	}
	
	public void setDriveSpeed(double leftSpeed, double rightSpeed) {
		frontLeft.set(leftSpeed);
        backLeft.set(leftSpeed);
        frontRight.set(rightSpeed);
        backRight.set(rightSpeed);
	}
	
	public void setSetpoint(double setpoint, double maxSpeed, double maxTime) {
		int numberOfTimesRun = 0;
		if(numberOfTimesRun == 0) {
			driveTrainController = new DriveStraightController(setpoint, .5, 5, drivetrain);
			driveTrainController.init();
			numberOfTimesRun++;
		} 
		if(!driveTrainController.checkForFinished()) {
			driveTrainController.update();
		} else {
			driveTrainController.finished();
			numberOfTimesRun = 0;
		}
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
	
	public Encoder getLeftEncoder() {
		return leftEncoder;
	}
	
	public Encoder getRightEncoder() {
		return rightEncoder;
	}
	
}
