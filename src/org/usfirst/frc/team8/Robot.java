
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
    			drivetrain.setSetpoint(100, .5, 5);
    			if(driveTrainController.checkForFinished()) {
    				autoState = AutoStates.EXPELLING;
    			}
    		break;
        	case EXPELLING:
        		shooter.setState(ShooterState.SHOOTING);
        			intake.setState(IntakeState.SHOOTING);
    		break;
        	case DRIVING_BACKWARDS:
        		drivetrain.setSetpoint(100, -.5, 5);
        		break;
    	}
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
