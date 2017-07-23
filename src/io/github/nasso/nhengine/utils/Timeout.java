package io.github.nasso.nhengine.utils;

public class Timeout {
	Runnable callback;
	float timeMS;
	float timer;
	
	public Timeout(Runnable callback, float timeMS) {
		this.callback = callback;
		this.timeMS = timeMS < 0 ? 0 : timeMS;
	}
}
