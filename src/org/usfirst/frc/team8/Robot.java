
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

	 @Override
    public void robotInit() {
		 drivetrain.init();
		 intake.init();
		 shooter.init();
    }
	 
    @Override
    public void autonomousInit() {
    	autoState = AutoStates.DRIVING_FORWARDS;
    }
    
    @Override
    public void autonomousPeriodic() {
    	switch (autoState) {
    	case DRIVING_FORWARDS:
    		if(numberOfTimesRun == 0) {
        		drivetrain.setSetpoint(100, .5, 5);
        		numberOfTimesRun++;
    		} 
    		if(drivetrainController.checkForFinished()) {	
    			numberOfTimesRun = 0;
    			startTime = System.currentTimeMillis();
    			shooter.setState(ShooterState.SHOOTING);
        		intake.setState(IntakeState.SHOOTING);
        		autoState = AutoStates.EXPELLING;
    		}		
    		break;
        case EXPELLING:
        	if((System.currentTimeMillis() - startTime) > 2000) {
    			shooter.setState(ShooterState.IDLE);
	    		intake.setState(IntakeState.IDLE);
    			autoState = AutoStates.DRIVING_BACKWARDS;
    		}
	    	break;
       	case DRIVING_BACKWARDS:
       		if(numberOfTimesRun == 0) {
        		drivetrain.setSetpoint(100, -.5, 5);
        		numberOfTimesRun++;
    		} 
    		if(drivetrainController.checkForFinished()) {
    			numberOfTimesRun = 0;
    			drivetrain.disable();
    		}		
    		break;
    	}
    	drivetrain.update();
    	shooter.update();
        intake.update();
    }
    
    public void setAutoState(AutoStates state) {
    	autoState = state;
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
        
        if(joysticks.getDriveStick().getRawButton(4)) {
        	drivetrain.setSetpoint(100, .5, 5);
        }
    }
    
    @Override
    public void testPeriodic() {
    
    }
    
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
