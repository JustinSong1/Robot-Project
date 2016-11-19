
package org.usfirst.frc.team8;

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
	Joysticks shooterStick = new Joysticks();
	Drivetrain drivetrain = new Drivetrain(joysticks.getDriveStick(), joysticks.getTurnStick());
	Intake intake = new Intake(shooterStick.getShooterStick());
	Shooter shooter = new Shooter(shooterStick.getShooterStick());
	
	 @Override
    public void robotInit() {
		 
    }
	 
    @Override
    public void autonomousInit() {
    	
    }
    
    @Override
    public void autonomousPeriodic() {
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
