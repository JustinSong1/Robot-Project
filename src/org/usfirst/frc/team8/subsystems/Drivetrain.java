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
	Controller drivetrainController;
	Joysticks joysticks = new Joysticks();
	private Drivetrain drivetrain = new Drivetrain(joysticks.getDriveStick(), joysticks.getTurnStick());

	
	public enum DriveState {
		ENCODER_DRIVE,
		TIMER_DRIVE,
		TELEOP,
		IDLE		
	}
	
	private DriveState state = DriveState.IDLE;
	
	/**
	 * 
	 * @param forward the joystick used for front and back movement
	 * @param turn the joystick used for turning the robot
	 * This is used for contructing the drivetrain. It constructs the four talons
	 * used for controlling the speed and also inverts the right side CANTalons
	 */
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
	
	/**
	 * Sets the distance per pulse on each of the encoders.
	 */
	@Override
	public void init() {
		rightEncoder.setDistancePerPulse(1);
		leftEncoder.setDistancePerPulse(.07116171);
	}
	
	
	/**
	 * Method that is repeatedly called. This has four case for
	 * different things the drivetrain can currently be doing.
	 * 
	 * In the Encoder_Drive, the update method checks for if
	 * the drivetrainController has finished, if it has not 
	 * finished then it updates, otherwise it sets the status
	 * to finished and switches the state to idle.
	 * 
	 * In the Timer_Drive, the update method checks if it has
	 * been driving for a certain amount of time. If it has it
	 * stops the drivetrain otherwise it continues on untill the time
	 * is reached.
	 * 
	 * In the Teleop, the update method checks the joysticks for input
	 * and sets the speed of each of the CANTalons based off the input
	 * 
	 * In the Idle, the update method sets the speeds of all the
	 * CANTalons to 0.
	 */
	@Override
	public void update() {
        switch (state) {
        case ENCODER_DRIVE:
        	//Checks if the drivetrainController isn't finished
        	if(!drivetrainController.checkForFinished()) {
        		drivetrainController.update();
        	//If it is finished it sets it's status to finished and changes
        	//the state to idle.
        	} else {
        		drivetrainController.finished();
        		drivetrainController = null;
        		state = DriveState.IDLE;
        	} 	
        	break;
        case TIMER_DRIVE:
        	//Checks if the current time is more than the max time
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
        	//Makes sure that the speeds don't exeed the limit
        	double leftSpeed = Math.min(Math.max(-1.0,-forward.getY() + turn.getX() ), 1.0);
            double rightSpeed = Math.min(Math.max(-1.0, -forward.getY() - turn.getX()), 1.0);
            frontLeft.set(leftSpeed);
            backLeft.set(leftSpeed);
            frontRight.set(rightSpeed);
            backRight.set(rightSpeed);
        	break;
        case IDLE:
        	frontLeft.set(0);
        	backLeft.set(0);
        	frontRight.set(0);
        	backRight.set(0);
        	break;	
        }
	}
	
	/**
	 * Sets the Drivetrain to Idle.
	 */
	@Override
	public void disable() {
		state = DriveState.IDLE;
	}
	
	/**
	 * 
	 * @param leftSpeed speed of the left talons
	 * @param rightSpeed speed of the right talons
	 * 
	 * Sets the speed of the talons 
	 */
	public void setDriveSpeed(double leftSpeed, double rightSpeed) {
		frontLeft.set(leftSpeed);
        backLeft.set(leftSpeed);
        frontRight.set(rightSpeed);
        backRight.set(rightSpeed);
	}
	
	/**
	 * 
	 * @param setpoint A point where the robot should reach
	 * @param maxSpeed A certain speed at which the robot shouldn't go over
	 * @param maxTime The maximum time getting to this point should take
	 * 
	 * Creates a DriveStraightController with the given inputs
	 */
	public void setSetpoint(double setpoint, double maxSpeed, double maxTime) {
		drivetrainController = new DriveStraightController(setpoint, maxSpeed, maxTime, drivetrain);
		drivetrainController.init();
	}
	
	/**
	 * 
	 * @param speed How fast the robot should drive
	 * @param time How long the robot should drive for
	 * 
	 * Sets the speed and the time to the inputs and then changes
	 * the state of the drivetrain to Timer_Drive.
	 */
	public void driveTime(double speed, double time) {
		speed = this.speed;
		time = (this.time * 1000) + System.currentTimeMillis();
		state = DriveState.TIMER_DRIVE;
	
	}
	
	/**
	 * 
	 * @param stateSet A drivetrain state
	 * 
	 * Sets the state of the drivetrain to the input
	 */
	public void setState(DriveState stateSet) {
		state = stateSet;
	}
	
	/**
	 * 
	 * @return Returns the current state
	 * 
	 */
	public DriveState getState() {
		return state;
	}
	
	/**
	 * @return The left encoder
	 */
	public Encoder getLeftEncoder() {
		return leftEncoder;
	}
	
	/**
	 * 
	 * @return The right encoder
	 */
	public Encoder getRightEncoder() {
		return rightEncoder;
	}
	
}
