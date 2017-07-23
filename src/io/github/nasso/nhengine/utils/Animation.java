package io.github.nasso.nhengine.utils;

public abstract class Animation {
	private float speed = 1;
	
	private int cycleCount = 1;
	
	private boolean restartOnLoop = true;
	
	public abstract void step(float delta);
	
	public abstract float getValue();
	
	public abstract boolean isFinished();
	
	public abstract void reset();
	
	public abstract void toEnd();
	
	public boolean isRestartOnEnd() {
		return this.restartOnLoop;
	}
	
	public void setRestartOnEnd(boolean restartOnEnd) {
		this.restartOnLoop = restartOnEnd;
	}
	
	public int getCycleCount() {
		return this.cycleCount;
	}
	
	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
