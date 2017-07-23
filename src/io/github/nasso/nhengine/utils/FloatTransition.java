package io.github.nasso.nhengine.utils;

import io.github.nasso.nhengine.utils.easing.EasingFunction;
import penner.easing.Cubic;
import penner.easing.Linear;

public class FloatTransition {
	private float duration = 1;
	private FloatConsumer callback = null;
	private float delay = 0;
	private float value;
	private float currentTime;
	private float beginningValue;
	private float targetValue;
	
	private EasingFunction easingFunc = EasingFunction.EASE;
	
	public FloatTransition(float duration, FloatConsumer callback, float actualValue, EasingFunction easingFunc, float delay) {
		this.duration = duration;
		this.callback = callback;
		this.value = actualValue;
		this.delay = delay;
		this.currentTime = this.duration;
		this.targetValue = actualValue;
		this.beginningValue = actualValue;
		this.easingFunc = easingFunc;
		
		TimeManager.registerTransition(this);
	}
	
	public FloatTransition(float duration, FloatConsumer callback, float actualValue, EasingFunction easingFunc) {
		this(duration, callback, actualValue, easingFunc, 0);
	}
	
	public FloatTransition(float duration, FloatConsumer callback, float actualValue) {
		this(duration, callback, actualValue, Cubic::easeInOut);
	}
	
	public void step(float delta) {
		if(!this.isFinished()) this.setCurrentTime(this.currentTime + delta / 1000.0f);
	}
	
	public float getTargetValue() {
		return this.targetValue;
	}
	
	public void setTargetValue(float f) {
		this.currentTime = 0;
		this.beginningValue = this.value;
		
		this.targetValue = f;
	}
	
	public float getDuration() {
		return this.duration;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	public FloatConsumer getCallback() {
		return this.callback;
	}
	
	public void setCallback(FloatConsumer callback) {
		this.callback = callback;
	}
	
	public float getValue() {
		return this.value;
	}
	
	public boolean isFinished() {
		return this.currentTime == this.duration + this.delay;
	}
	
	public void restart(float startValue, float endValue, float delay) {
		this.beginningValue = startValue;
		this.targetValue = endValue;
		this.delay = delay;
		this.setCurrentTime(0);
	}
	
	public void restart(float startValue, float endValue) {
		this.restart(startValue, endValue, this.delay);
	}
	
	public float getCurrentTime() {
		return this.currentTime;
	}
	
	public void setCurrentTime(float time) {
		this.currentTime = MathUtils.clamp(time, 0.0f, this.duration + this.delay);
		
		this.value = this.easingFunc.apply(Math.max(this.currentTime - this.delay, 0), this.beginningValue, this.targetValue - this.beginningValue, this.duration);
		
		if(this.callback != null) this.callback.accept(this.value);
	}
	
	public EasingFunction getEasingFunction() {
		return this.easingFunc;
	}
	
	public void setEasingFunction(EasingFunction easingFunc) {
		this.easingFunc = easingFunc == null ? Linear::easeNone : easingFunc;
	}
	
	public float getDelay() {
		return this.delay;
	}
	
	public void setDelay(float delay) {
		this.delay = delay;
	}
}
