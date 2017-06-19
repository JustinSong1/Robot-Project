package org.usfirst.frc.team8.controllers;

import org.usfirst.frc.team8.subsystems.Drivetrain;

public class DriveStraightController extends Controller {
	private double distance;
	private double maxSpeed;
	private double maxTime;
	private double endTime;
	private double targetDistance;
	private double kP = 1;
	private double lastLeft;
	private double lastRight;
	private double kD = 9;
	private double acceptableError = 2;
	private Drivetrain drivetrain;
	private double derivativeLeft;
	private double derivativeRight;
	
	
	public DriveStraightController(double distance, double maxSpeed, double maxTime, Drivetrain drivetrain) {
		this.distance = distance;
		this.maxSpeed = maxSpeed;
		this.maxTime = maxTime;		
		this.drivetrain = drivetrain;
	}
	
	@Override
	public void init() {
		endTime = System.currentTimeMillis() + (maxTime * 1000);
		targetDistance = drivetrain.getRightMasterTalon().getEncPosition() + distance;
		lastRight = targetDistance -  drivetrain.getRightMasterTalon().getEncPosition();
		lastLeft = targetDistance -  drivetrain.getRightMasterTalon().getEncPosition();
		derivativeLeft = 0;
		derivativeRight = 0;
	}

	@Override
	public void update() {
		double errorRight = targetDistance -  drivetrain.getRightMasterTalon().getEncPosition();
		derivativeRight = (errorRight - lastRight) * 50;
		lastRight = errorRight;
		double errorLeft = targetDistance -  drivetrain.getRightMasterTalon().getEncPosition();
		derivativeLeft = (errorLeft - lastLeft) * 50;
		lastLeft = errorLeft;
		drivetrain.setDriveSpeed((kP * errorLeft) + (kD * derivativeLeft), ((kP * errorRight) + (kD * derivativeRight)));
	}

	@Override
	public void finished() {
		drivetrain.setDriveSpeed(0, 0);
	}

	@Override
	public boolean checkForFinished() {
		if(Math.abs(targetDistance -  drivetrain.getRightMasterTalon().getEncPosition()) < 2 && Math.abs(targetDistance -  drivetrain.getLeftMasterTalon().getEncPosition()) < 2 && derivativeLeft == 0 && derivativeRight == 0){
			return true;
		}
		if(System.currentTimeMillis() >= endTime) {
			return true;
		}
		return false;
	}

}
