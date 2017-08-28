package io.github.nasso.test.soundtestroom;

import java.io.IOException;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.component.AudioSourceComponent;
import io.github.nasso.nhengine.component.AudioSourceComponent.Status;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.LaunchSettings.VideoMode;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Scene;

public class SoundTestRoom implements GameRunner {
	public static void main(String[] argv) {
		new SoundTestRoom();
	}
	
	private Game game = Game.instance();
	private Level lvl = new Level(); // What LVL stands for? Why, Level dummy.
	private Scene sce = new Scene();
	private AudioSourceComponent audioSource = new AudioSourceComponent();
	
	private Sound snd;
	private GameWindow win;
	
	public SoundTestRoom() {
		this.game.init(this,
			new LaunchSettings()
				.windowTitle("SoundTest Room")
				.videoMode(VideoMode.WINDOWED)
				.videoSize(640, 480)
		);
		
		this.game.start();
	}

	public void init() {
		this.win = this.game.window();
		
		try {
			this.snd = Sound.load("res/demo/audio/mus_story.ogg", true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.audioSource.setSound(this.snd);
		this.audioSource.setLooping(false);
		this.audioSource.setPitch(0.9f);
		
		this.sce.getRoot().addComponent(this.audioSource);
		
		this.lvl.addOverlayScene(this.sce);
		
		this.game.loadLevel(this.lvl);
	}
	
	public void update(float delta) {
		if(this.win.isKeyPressed(Nhengine.KEY_SPACE)) {
			if(this.audioSource.getStatus() == Status.PLAYING) this.audioSource.pause(); 
			else this.audioSource.play();
		}
		
		if(this.win.isKeyPressed(Nhengine.KEY_S)) {
			this.audioSource.stop();
		}
	}
	
	public void dispose() {
	}
}
