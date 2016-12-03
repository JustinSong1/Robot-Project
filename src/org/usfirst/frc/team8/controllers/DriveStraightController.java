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
	
	
	public DriveStraightController(double distance, double maxSpeed, double maxTime, Drivetrain drivetrain) {
		this.distance = distance;
		this.maxSpeed = maxSpeed;
		this.maxTime = maxTime;		
		this.drivetrain = drivetrain;
	}
	
	@Override
	public void init() {
		endTime = System.currentTimeMillis() + (maxTime * 1000);
		targetDistance = drivetrain.getLeftEncoder().getDistance() + distance;
		lastLeft = targetDistance - drivetrain.getLeftEncoder().getDistance();
		derivativeLeft = 0;
	}

	@Override
	public void update() {
		double errorLeft = targetDistance - drivetrain.getLeftEncoder().getDistance();
		derivativeLeft = (errorLeft - lastLeft) * 50;
		lastLeft = errorLeft;
		double speed = Math.max(Math.min((kP * errorLeft) + (kD * derivativeLeft), maxSpeed), -maxSpeed);
		drivetrain.setDriveSpeed(speed, speed);
	}

	@Override
	public void finished() {
		drivetrain.disable();
	}

	@Override
	public boolean checkForFinished() {
		if(Math.abs(targetDistance - drivetrain.getLeftEncoder().getDistance()) < 2 && derivativeLeft == 0){
			return true;
		}
		if(System.currentTimeMillis() >= endTime) {
			return true;
		}
		return false;
	}

}
