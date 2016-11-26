
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
	Controller driveTrainController;
	
	public enum autoStates {
		DRIVING_FORWARDS,
		DRIVING_BACKWARDS,
		EXPELLING
	}
	
	private autoStates state = autoStates.DRIVING_FORWARDS;

	 @Override
    public void robotInit() {
		 
    }
	 
    @Override
    public void autonomousInit() {
    	state = autoStates.DRIVING_FORWARDS;
    }
    
    @Override
    public void autonomousPeriodic() {
    	int numberOfTimesRun = 0;
    	switch (state) {
    	case DRIVING_FORWARDS:
    		if(numberOfTimesRun == 0) {
    			driveTrainController = new DriveStraightController(100, .5, 5, drivetrain);
    			driveTrainController.init();
    			numberOfTimesRun++;
    		} 
    		if(!driveTrainController.checkForFinished()) {
    			driveTrainController.update();
    		} else {
    			driveTrainController.finished();
    			numberOfTimesRun = 0;
    			state = autoStates.EXPELLING;
    		}
    		break;
        	case EXPELLING:
    		break;
        	case DRIVING_BACKWARDS:
        		if(numberOfTimesRun == 0) {
        			driveTrainController = new DriveStraightController(100, -.5, 5, drivetrain);
        			driveTrainController.init();
        			numberOfTimesRun++;
        		} 
        		if(!driveTrainController.checkForFinished()) {
        			driveTrainController.update();
        		} else {
        			driveTrainController.finished();
        			numberOfTimesRun = 0;
        			state = autoStates.EXPELLING;
        		}
        		break;
    	}
    }
    
    @Override
    public void teleopInit() {
    	drivetrain.setState(DriveState.TELEOP);
    }
    
    @Override
    public void teleopPeriodic() {
        drivetrain.update();
        shooter.update();
        intake.update();
    }
    
    @Override
    public void testPeriodic() {
    
    }
    
    @Override
    public void disabledInit() {
    	drivetrain.setState(DriveState.DISABLED);
    	shooter.setState(ShooterState.IDLE);
    	intake.setState(IntakeState.IDLE);
    }
    
    @Override
    public void disabledPeriodic() {
    	
    }
    
}
