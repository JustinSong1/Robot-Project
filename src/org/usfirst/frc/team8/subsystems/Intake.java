package org.usfirst.frc.team8.subsystems;

import org.usfirst.frc.team8.subsystems.Drivetrain.DriveState;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class Intake extends Subsystem{
	Joystick joystick;
	CANTalon intakeMotor;
	
	public Intake(Joystick joystick){
		this.joystick = joystick;
		intakeMotor = new CANTalon(7);
	}
	
	private IntakeState state = IntakeState.IDLE;
	
	public enum IntakeState {	
		INTAKING,
		SHOOTING,
		IDLE	
	}
	
	public void setState(IntakeState stateSet) {
		state = stateSet;
	}

	public IntakeState getState() {
		return state;
	}
	
	@Override
	public void init() {
		
		
	}
	
	@Override
	public void update() {		
		if(DriverStation.getInstance().isOperatorControl()) {
			if(joystick.getRawButton(5)) {
				setState(IntakeState.INTAKING);
			} else if(joystick.getRawButton(6)) {
				setState(IntakeState.SHOOTING);
			} else {
				setState(IntakeState.IDLE);
			}
		}
		switch (state) {
		case INTAKING:
			intakeMotor.set(1);
			break;
		case SHOOTING:
			intakeMotor.set(-1);
			break;
		case IDLE:
			intakeMotor.set(0);
			break;
		}
	}
	
	@Override
	public void disable() {
		
	}
	
}
