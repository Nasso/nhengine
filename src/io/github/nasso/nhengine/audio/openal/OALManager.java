package io.github.nasso.nhengine.audio.openal;

import static org.lwjgl.openal.AL10.*;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.Nhengine;

public class OALManager {
	public static final boolean DEBUG = Nhengine.DEBUG;
	
	private static OALManager singleton;
	
	private final OALDevice device;
	private final OALContext ctx;
	public final OALPlayer audioPlayer;
	
	public OALManager(LaunchSettings settings) {
		this.device = new OALDevice();
		
		if(!this.device.isValid()) {
			this.ctx = null;
			this.audioPlayer = null;
			System.err.println("Couldn't create the audio device");
			return;
		}
		
		this.ctx = new OALContext(this.device, 0);
		this.ctx.makeCurrent();
		
		AL.createCapabilities(ALC.createCapabilities(this.device.id));
		
		this.audioPlayer = new OALPlayer();
	}
	
	private void disposeInst() {
		this.device.dispose();
		if(this.ctx != null) this.ctx.dispose();
		if(this.audioPlayer != null) this.audioPlayer.dispose();
	}
	
	public static OALManager get() {
		return OALManager.singleton;
	}
	
	public static OALManager init(LaunchSettings settings) {
		if(OALManager.singleton == null) OALManager.singleton = new OALManager(settings);
		
		return OALManager.singleton;
	}
	
	public static void dispose() {
		OALBuffers.dispose();
		OALSources.dispose();
		
		OALManager.singleton.disposeInst();
		OALManager.singleton = null;
	}
	
	// Utils
	public static void fastCheckError(String where) {
		if(!DEBUG) return;
		
		String name = OALManager.getError(alGetError());
		
		if(name != null) System.err.println("OpenAL error at '" + where + "': " + name);
	}
	
	private static String getError(int err) {
		String error = null;
		
		switch(err) {
			case AL_INVALID_NAME:
				error = "AL_INVALID_NAME";
				break;
			case AL_INVALID_ENUM:
				error = "AL_INVALID_ENUM";
				break;
			case AL_INVALID_VALUE:
				error = "AL_INVALID_VALUE";
				break;
			case AL_INVALID_OPERATION:
				error = "AL_INVALID_OPERATION";
				break;
			case AL_OUT_OF_MEMORY:
				error = "AL_OUT_OF_MEMORY";
				break;
		}
		
		return error;
	}
	
}
