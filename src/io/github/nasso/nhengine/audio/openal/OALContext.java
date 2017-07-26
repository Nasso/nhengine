package io.github.nasso.nhengine.audio.openal;

import static org.lwjgl.openal.ALC10.*;

public class OALContext {
	long id;
	
	public OALContext(OALDevice device, int... attr) {
		if(attr.length == 0) this.id = nalcCreateContext(device.id, 0);
		else this.id = alcCreateContext(device.id, attr);
	}
	
	public void makeCurrent() {
		alcMakeContextCurrent(this.id);
	}
	
	public void dispose() {
		alcDestroyContext(this.id);
	}
}
