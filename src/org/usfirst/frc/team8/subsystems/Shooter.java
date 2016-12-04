package org.usfirst.frc.team8.subsystems;

import org.usfirst.frc.team8.subsystems.Intake.IntakeState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter extends Subsystem {
	Joystick joystick;
	VictorSP shooterMotor;
	public Shooter(Joystick joystick) {
		this.joystick = joystick;
		shooterMotor = new VictorSP(0);
	}
	//See Intake for documentation on methods that 
	//perform the same tasks.
	private ShooterState state = ShooterState.IDLE;
	
	public enum ShooterState {	
		INTAKING,
		SHOOTING,
		IDLE	
	}
	
	public void setState(ShooterState stateSet) {
		state = stateSet;
	}

	public ShooterState getState() {
		return state;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void update() {
		if(DriverStation.getInstance().isOperatorControl()) {
			if(joystick.getRawButton(5)) {
				setState(ShooterState.INTAKING);
			} else if(joystick.getRawButton(6)) {
				setState(ShooterState.SHOOTING);
			} else {
				setState(ShooterState.IDLE);
			}
		}
		switch (state) {
		case INTAKING:
			shooterMotor.set(1);
			break;
		case SHOOTING:
			shooterMotor.set(-1);
			break;
		case IDLE:
			shooterMotor.set(0);
			break;
		}
	}
	
	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}
	
}
