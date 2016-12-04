
package org.usfirst.frc.team8;

import org.usfirst.frc.team8.controllers.Controller;
import org.usfirst.frc.team8.controllers.DriveStraightController;
import org.usfirst.frc.team8.input.Joysticks;
import org.usfirst.frc.team8.subsystems.Drivetrain;
import org.usfirst.frc.team8.subsystems.Drivetrain.DriveState;
import org.usfirst.frc.team8.subsystems.Shooter.ShooterState;
import org.usfirst.frc.team8.subsystems.Intake;
import org.usfirst.frc.team8.subsystems.Intake.IntakeState;
import org.usfirst.frc.team8.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Joysticks joysticks = new Joysticks();
	Drivetrain drivetrain = new Drivetrain(joysticks.getDriveStick(), joysticks.getTurnStick());
	Intake intake = new Intake(joysticks.getShooterStick());
	Shooter shooter = new Shooter(joysticks.getShooterStick());
	Controller drivetrainController;
	int numberOfTimesRun = 0;
	double startTime;
	
	
	public enum AutoStates {
		DRIVING_FORWARDS,
		DRIVING_BACKWARDS,
		EXPELLING
	}
	
	public AutoStates autoState = AutoStates.DRIVING_FORWARDS;

	/**
	 * Initializes the drivetrain, intake, and shooter.
	 */
	 @Override
    public void robotInit() {
		 drivetrain.init();
		 intake.init();
		 shooter.init();
    }
	
	 /**
	  * When autonomous starts the robot state is set to 
	  * driving forwards.
	  */
    @Override
    public void autonomousInit() {
    	autoState = AutoStates.DRIVING_FORWARDS;
    }
    
    /**
     * During the autnomous period has a case for each of the
     * things the robot does during the autonomous period.
     * 
     * In the Driving_Forwards case, the first time it is run, 
     * it creates a setpoint. Afterwards it checks if the drivetrainController
     * is finished, if it is, it sets the states fo the shooter and intake to 
     * shooting and moves on to the next state.
     * 
     * In the Expelling case, the robot shoots for 2 seconds and then is set
     * to idle. The auto then move on to the next state.
     * 
     * In the Driving_Backwards case, the Robot does the same thing but instead
     * drives backwards.
     */
    @Override
    public void autonomousPeriodic() {
    	switch (autoState) {
    	case DRIVING_FORWARDS:
    		//Checks if it is the first time being run
    		if(numberOfTimesRun == 0) {
        		drivetrain.setSetpoint(100, .5, 5);
        		numberOfTimesRun++;
    		} 
    		//Checks if the drivetrainController is finished
    		if(drivetrainController.checkForFinished()) {	
    			numberOfTimesRun = 0;
    			startTime = System.currentTimeMillis();
    			shooter.setState(ShooterState.SHOOTING);
        		intake.setState(IntakeState.SHOOTING);
        		autoState = AutoStates.EXPELLING;
    		}		
    		break;
        case EXPELLING:
        	//Checks if more than 2 seconds have elapsed
        	if((System.currentTimeMillis() - startTime) > 2000) {
    			shooter.setState(ShooterState.IDLE);
	    		intake.setState(IntakeState.IDLE);
    			autoState = AutoStates.DRIVING_BACKWARDS;
    		}
	    	break;
       	case DRIVING_BACKWARDS:
       		//Checks if it is the first time being run
       		if(numberOfTimesRun == 0) {
        		drivetrain.setSetpoint(100, -.5, 5);
        		numberOfTimesRun++;
    		} 
       		//Checks if the drivetrainController is finshed
       		//If it is it disables it
    		if(drivetrainController.checkForFinished()) {
    			numberOfTimesRun = 0;
    			drivetrain.disable();
    		}		
    		break;
    	}
    	//Updates the subsystems
    	drivetrain.update();
    	shooter.update();
        intake.update();
    }
    
    /**
     * 
     * @param state The state which you want to change the state to
     * 
     * Changes the state to the given state.
     */
    public void setAutoState(AutoStates state) {
    	autoState = state;
    }
    
    /**
     * Sets the state of the drivetrain to Telop
     */
    @Override
    public void teleopInit() {
    	drivetrain.setState(DriveState.TELEOP);
    }
    
    /**
     * Updates all of the subsystems
     * Also had routine for driving forward 100 inches at by 
     * pressing button 4
     */
    @Override
    public void teleopPeriodic() {
        drivetrain.update();
        shooter.update();
        intake.update();
        
        if(joysticks.getDriveStick().getRawButton(1)) {
        	drivetrain.setState(DriveState.TELEOP);
        } else if(joysticks.getDriveStick().getRawButton(4)) {
        	drivetrain.setSetpoint(100, .5, 5);
        }
    }
    
    @Override
    public void testPeriodic() {
    
    }
    
    /**
     * Sets all subsystems to idle
     */
    @Override
    public void disabledInit() {
    	drivetrain.setState(DriveState.IDLE);
    	shooter.setState(ShooterState.IDLE);
    	intake.setState(IntakeState.IDLE);
    }
    
    @Override
    public void disabledPeriodic() {
    	
    }
    
}
