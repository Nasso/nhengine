package io.github.nasso.nhengine.openal;

import static org.lwjgl.openal.ALC10.*;

public class OALDevice {
	long id;
	
	public OALDevice() {
		this.id = nalcOpenDevice(0);
	}
	
	public OALDevice(String name) {
		this.id = alcOpenDevice(name);
	}
	
	public boolean isValid() {
		return this.id != 0;
	}
	
	public void dispose() {
		alcCloseDevice(this.id);
	}
}
