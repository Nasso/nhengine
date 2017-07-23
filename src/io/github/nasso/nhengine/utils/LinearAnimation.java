package io.github.nasso.nhengine.utils;

public class LinearAnimation extends Animation {
	private boolean animationSide = false;
	
	private int currentCycle = 0;
	
	private float from, to;
	
	private float currentTime;
	private float length;
	
	public LinearAnimation(float from, float to, float length, int cycleCount, boolean restartOnEnd) {
		this.reset(from, to, length, cycleCount, restartOnEnd);
	}
	
	public LinearAnimation(float from, float to, float length) {
		this(from, to, length, 1, true);
	}
	
	public void step(float delta) {
		if(this.getCycleCount() > 0 && (this.getCycleCount() == 0 || this.currentCycle >= this.getCycleCount())) return;
		
		this.currentTime += delta * this.getSpeed();
		
		if(this.getSpeed() < 0.0f) {
			if(this.currentTime <= 0) {
				this.animationSide = !this.animationSide;
				
				if(this.getCycleCount() >= 0) {
					this.currentCycle--;
					
					if(this.currentCycle >= 0) this.currentTime = this.length;
					else if(this.currentCycle < 0) return;
				} else {
					this.currentTime = this.length;
				}
			}
		} else {
			if(this.currentTime >= this.length) {
				this.animationSide = !this.animationSide;
				
				if(this.getCycleCount() >= 0) {
					this.currentCycle++;
					
					if(this.currentCycle < this.getCycleCount()) this.currentTime = 0;
					else if(this.currentCycle >= this.getCycleCount()) return;
				} else {
					this.currentTime = 0;
				}
			}
		}
		
		this.currentTime = MathUtils.clamp(this.currentTime, 0, this.length);
	}
	
	public float getValue() {
		if(!this.isRestartOnEnd() && this.animationSide) return MathUtils.lerp(this.to, this.from, MathUtils.clamp(this.currentTime / this.length, 0.0f, 1.0f));
		else return MathUtils.lerp(this.from, this.to, MathUtils.clamp(this.currentTime / this.length, 0.0f, 1.0f));
	}
	
	public float getLength() {
		return this.length;
	}
	
	public boolean isFinished() {
		return this.getCycleCount() == 0 || (this.getCycleCount() > 0 && (this.getSpeed() < 0.0f ? this.currentTime <= 0.0f : this.currentTime >= this.length));
	}
	
	public void reset() {
		this.reset(this.from, this.to, this.length);
	}
	
	public void reset(float length) {
		this.reset(this.from, this.to, length, this.getCycleCount(), this.isRestartOnEnd());
	}
	
	public void reset(float from, float to) {
		this.reset(from, to, this.length, this.getCycleCount(), this.isRestartOnEnd());
	}
	
	public void reset(float from, float to, float length) {
		this.reset(from, to, length, this.getCycleCount(), this.isRestartOnEnd());
	}
	
	public void reset(float from, float to, float length, int cycleCount) {
		this.reset(from, to, length, cycleCount, this.isRestartOnEnd());
	}
	
	public void reset(float from, float to, float length, int cycleCount, boolean restartOnEnd) {
		this.from = from;
		this.to = to;
		this.length = length;
		this.setCycleCount(cycleCount);
		this.setRestartOnEnd(restartOnEnd);
		this.setSpeed(1);
		this.currentCycle = 0;
		this.currentTime = 0;
	}
	
	public void toEnd() {
		this.currentTime = this.length;
		this.currentCycle = Math.max(this.getCycleCount() - 1, 0);
		this.animationSide = false;
	}
}
