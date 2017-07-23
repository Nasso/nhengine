package io.github.nasso.nhengine.utils;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

public class TimeManager {
	private TimeManager() {
		
	}
	
	private static List<FloatTransition> transitions = new ArrayList<FloatTransition>();
	private static List<Timeout> timeouts = new ArrayList<Timeout>();
	
	public static void step(float delta) {
		for(int i = 0; i < transitions.size(); i++) {
			transitions.get(i).step(delta);
		}
		
		for(int i = 0, l = timeouts.size(); i < l; i++) {
			Timeout t = timeouts.get(i);
			t.timer += delta;
			
			if(t.timer > t.timeMS) {
				t.callback.run();
				timeouts.remove(i);
				i--;
				l--;
			}
		}
	}
	
	public static void registerTransition(FloatTransition trans) {
		if(!transitions.contains(trans)) transitions.add(trans);
	}
	
	public static void removeTransition(FloatTransition trans) {
		transitions.remove(trans);
	}
	
	public static Timeout setTimeout(Runnable r, float ms) {
		return setTimeout(new Timeout(r, ms));
	}
	
	public static Timeout setTimeout(Timeout t) {
		if(t.callback == null) return t;
		
		synchronized(timeouts) {
			timeouts.add(t);
		}
		
		return t;
	}
	
	public static void cancelTimeout(Timeout t) {
		synchronized(timeouts) {
			t.timer = 0;
			timeouts.remove(t);
		}
	}
	
	public static void runLater(Runnable r) {
		setTimeout(new Timeout(r, 0));
	}
	
	/**
	 * Returns the time since the GLFW intialization.
	 * 
	 * @return
	 */
	public static float getTimeSec() {
		return (float) GLFW.glfwGetTime();
	}
	
	public static float getTimeMS() {
		return getTimeSec() * 1000.0f;
	}
}
