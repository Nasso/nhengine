package io.github.nasso.test.input;

import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.component.InputComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.event.InputComponentEventHandler;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;

public class TestInputMain implements GameRunner {
	public static final int WIDTH = 800, HEIGHT = 600;
	
	private Game game;
	
	public TestInputMain() {
		this.game = Game.instance();
		
		this.game.init(this, new LaunchSettings().maxFPS(60).videoSize(WIDTH, HEIGHT));
		this.game.start();
	}
	
	public static void main(String[] argv) {
		new TestInputMain();
	}
	
	private GraphicsContext2D gtx;
	private int cvsWidth = 100, cvsHeight = 200;
	
	public void init() {
		CanvasComponent cvs = new CanvasComponent(this.cvsWidth, this.cvsHeight);
		cvs.setScale(this.cvsWidth, this.cvsHeight);
		this.gtx = cvs.getContext2D();
		
		this.gtx.setStroke(1, 1, 1);
		this.gtx.setStrokeSize(2);
		this.gtx.strokeRect(0, 0, 100, 200);
		
		InputComponent input = new InputComponent(this.cvsWidth, this.cvsHeight, new InputComponentEventHandler() {
			public void textInput(int codepoint) {
			}
			
			public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
			}
			
			public void mouseMoved(float newX, float newY, float relX, float relY) {
			}
			
			public void mouseButtonReleased(float x, float y, int btn) {
			}
			
			public void mouseButtonPressed(float x, float y, int btn) {
				System.out.println("Dot at " + x + ";" + y);
				
				TestInputMain.this.gtx.setFill(1, 0, 0);
				TestInputMain.this.gtx.fillRect(x - 1, y - 1, 3, 3);
			}
			
			public void keyTyped(int key) {
			}
			
			public void keyReleased(int key) {
			}
			
			public void keyPressed(int key) {
			}
			
			public void mouseExited(float x, float y, float relX, float relY) {
			}
			
			public void mouseEntered(float x, float y, float relX, float relY) {
			}
			
			public void mouseDragged(float x, float y, float relX, float relY, int button) {
			}
		});
		
		Node inputNode = new Node();
		inputNode.translate(300, 100);
		inputNode.scale(2, 1.5f);
		inputNode.rotate(45f);
		inputNode.addComponents(cvs, input);
		
		Scene sce = new Scene();
		sce.getRoot().addChild(inputNode);
		sce.getCamera().setViewport2D(WIDTH, HEIGHT);
		
		Level lvl = new Level();
		lvl.addOverlayScene(sce);
		this.game.loadLevel(lvl);
	}
	
	public void update(float delta) {
	}
	
	public void dispose() {
	}
}
