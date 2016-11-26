package org.usfirst.frc.team8.controllers;

public abstract class Controller {
	
	public abstract void init();
	public abstract void update();
	public abstract void finished();
	public abstract boolean checkForFinished();
	
}
