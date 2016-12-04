package org.usfirst.frc.team8.controllers;

import org.usfirst.frc.team8.subsystems.Drivetrain;

public class DriveStraightController extends Controller {
	private Drivetrain drivetrain;
	private double distance;
	private double maxSpeed;
	private double maxTime;
	private double endTime;
	private double targetDistance;
	private double kP = 1;
	private double lastLeft;
	private double kD = 9;
	private double acceptableError = 2;
	private double derivativeLeft;
	
	/**
	 * 
	 * @param distance How far the robot should travel
	 * @param maxSpeed The maximum speed the robot should go
	 * @param maxTime The maximum time that this should take
	 * @param drivetrain The drivetrain
	 * 
	 * Sets the instance variables equal to the params
	 */
	public DriveStraightController(double distance, double maxSpeed, double maxTime, Drivetrain drivetrain) {
		this.distance = distance;
		this.maxSpeed = maxSpeed;
		this.maxTime = maxTime;		
		this.drivetrain = drivetrain;
	}
	
	/**
	 * Sets the end time, target distance, lastErrorLeft, and
	 * Sets the left derivative to 0.
	 */
	@Override
	public void init() {
		endTime = System.currentTimeMillis() + (maxTime * 1000);
		targetDistance = drivetrain.getLeftEncoder().getDistance() + distance;
		lastLeft = targetDistance - drivetrain.getLeftEncoder().getDistance();
		derivativeLeft = 0;
	}

	/**
	 * Calculates the speed of the drivetrain using PID.
	 */
	@Override
	public void update() {
		double errorLeft = targetDistance - drivetrain.getLeftEncoder().getDistance();
		derivativeLeft = (errorLeft - lastLeft) * 50;
		lastLeft = errorLeft;
		double speed = Math.max(Math.min((kP * errorLeft) + (kD * derivativeLeft), maxSpeed), -maxSpeed);
		drivetrain.setDriveSpeed(speed, speed);
	}

	/**
	 * Disables the drivetrain
	 */
	@Override
	public void finished() {
		drivetrain.disable();
	}

	/**
	 * Checks if the drivetrainController is finished
	 */
	@Override
	public boolean checkForFinished() {
		//Checks if it has gone the given distance
		if(Math.abs(targetDistance - drivetrain.getLeftEncoder().getDistance()) < 2 && derivativeLeft == 0){
			return true;
		}
		//Checks if the maximum time has elapsed
		if(System.currentTimeMillis() >= endTime) {
			return true;
		}
		return false;
	}

}
