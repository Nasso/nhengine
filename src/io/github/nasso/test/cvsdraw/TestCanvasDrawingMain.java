package io.github.nasso.test.cvsdraw;

import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.LaunchSettings.VideoMode;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Scene;

public class TestCanvasDrawingMain implements GameRunner {
	public static void main(String[] args) {
		new TestCanvasDrawingMain();
	}
	
	public static final int WIDTH = 800, HEIGHT = 600;
	
	private Game game;
	private Level lvl;
	private Scene sce;
	private CanvasComponent cvs;
	private GraphicsContext2D gtx;
	
	private TestCanvasDrawingSketch sketch;
	
	public TestCanvasDrawingMain() {
		this.game = Game.instance();
		this.game.init(this, new LaunchSettings().maxFPS(60).videoMode(VideoMode.WINDOWED).videoSize(WIDTH, HEIGHT).resizable(false).windowTitle("Canvas tests"));
		
		this.game.start();
	}
	
	public void init() {
		this.lvl = new Level();
		this.sce = new Scene();
		
		this.cvs = new CanvasComponent(WIDTH, HEIGHT);
		this.cvs.setScale(WIDTH, HEIGHT);
		this.gtx = this.cvs.getContext2D();
		
		this.sce.getRoot().addComponent(this.cvs);
		this.sce.getCamera().setViewport2D(WIDTH, HEIGHT);
		
		this.lvl.addOverlayScene(this.sce);
		
		this.game.loadLevel(this.lvl);
		
		this.sketch = new TestCanvasDrawingSketch();
		this.sketch.setup();
	}
	
	public void update(float delta) {
		this.sketch.update(delta);
		this.sketch.draw(this.gtx);
	}
	
	public void dispose() {
		this.sketch.dispose();
	}
}
