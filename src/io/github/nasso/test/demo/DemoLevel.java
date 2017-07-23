package io.github.nasso.test.demo;

import java.util.LinkedList;
import java.util.Queue;

import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.InputHandler;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Node;

public class DemoLevel extends Level implements InputHandler {
	private static final int ORI_RIGHT = 2;
	private static final int ORI_LEFT = 1;
	private static final int ORI_UP = 3;
	private static final int ORI_DOWN = 0;
	
	// Instance de la scene du monde du niveau
	private DemoWorldScene worldScene = new DemoWorldScene();
	private DemoGameDialogScene gameHudScene = new DemoGameDialogScene();
	private DemoUIScene uiScene = new DemoUIScene();
	private DemoTestDialog sansDial;
	
	// La vitesse de la caméra
	private float cameraSpeed = 4.0f / 1000.0f;
	
	// La vitesse du joueur, en metres par millisecondes
	private float playerSpeed = 3f / 1000.0f;
	private float playerAnimPos = 0.0f;
	private float playerAnimSpeed = 2.0f;
	
	private float playerPrecisePosX = 0, playerPrecisePosY = 0;
	private float pixelSize = 1f / 16f;
	private int playerOrientation = ORI_DOWN;
	private Queue<Integer> orientationCommands = new LinkedList<Integer>();
	
	private Node player;
	private TiledSpriteComponent characterTilesComp;
	
	public DemoLevel() {
		this.addOverlayScene(this.worldScene);
		this.addOverlayScene(this.gameHudScene);
		this.addOverlayScene(this.uiScene);
		
		this.player = this.worldScene.getPlayer();
		this.characterTilesComp = this.worldScene.getPlayerTiledSprite();
		
		this.sansDial = new DemoTestDialog();
		
		// Et pour finir, on enregistre this en tant que InputHandler (oui, cette classe implémente l'interface InputHandler).
		// Comme ça on peut écouter les inputs sous forme de callbacks, plus simple a gérer dans certains cas!
		Game.instance().window().registerInputHandler(this);
	}
	
	public void update(float delta) {
		// Aller aller aller! Cette méthode ne doit pas perdre de temps! On applique la méchanique de jeu et tout ce qu'on veut ici, les contrôles, enfin tout ce qui est
		// fait dans une boucle de jeu! Sauf le rendu, ça c'est pas (plus) notre boulot! Ça se fait tout seul derrière, après tout, ya rien de compliquer à dessiner 3 images, non?
		
		// Tiens, on a un argument! "delta" ! Bon, je n'apprens rien à personne ici (je l'espère), le delta time est le temps écoulé depuis la dernière frame! Ici, c'est en millisecondes!
		
		// Bon, on commence par récupérer la fenêtre. Oui, j'aurais pu la récupérer une fois et la stocker en tant que membre de la classe, mais bon c'est pas grave, j'ai la flemme.
		GameWindow win = Game.instance().window();
		
		// Cette fenêtre va nous servir a récupérer les évenements, ce qu'il s'est passé depuis la dernière fois. Bon, rien de bien compliqué ici, le nom des méthodes est simple:
		// isDown: retourne true si la touche est enfoncée
		// isPressed: retourne true si la touche a été enfoncée lors de la dernière frame (donc qu'une fois généralement)
		// isReleased: retourne true si la touche a été relachée lors de la dernière frame.
		
		// Bon, on va faire bouger le personnage avec les touches fléchées. Il nous suffit de vérifier si elles sont enfoncées, et de faire bouger notre joueur, en le multipliant par
		// delta!
		if(!this.orientationCommands.isEmpty()) {
			this.playerOrientation = this.orientationCommands.peek();
		}
		
		int animFrame = (int) (1.0f / this.playerSpeed + this.playerAnimPos) % 4;
		if(animFrame == 4) animFrame = 0;
		if(win.isDown(DemoMain.GAME_KEY_LEFT)) {
			this.playerPrecisePosX -= this.playerSpeed * delta;
			this.characterTilesComp.setActiveCell(animFrame, this.playerOrientation);
		} else if(win.isDown(DemoMain.GAME_KEY_RIGHT)) {
			this.playerPrecisePosX += this.playerSpeed * delta;
			this.characterTilesComp.setActiveCell(animFrame, this.playerOrientation);
		}
		
		if(win.isDown(DemoMain.GAME_KEY_UP)) {
			this.playerPrecisePosY -= this.playerSpeed * delta;
			this.characterTilesComp.setActiveCell(animFrame, this.playerOrientation);
		} else if(win.isDown(DemoMain.GAME_KEY_DOWN)) {
			this.playerPrecisePosY += this.playerSpeed * delta;
			this.characterTilesComp.setActiveCell(animFrame, this.playerOrientation);
		}
		
		this.player.setPosition((float) (Math.floor(this.playerPrecisePosX / this.pixelSize)) * this.pixelSize, (float) (Math.floor(this.playerPrecisePosY / this.pixelSize)) * this.pixelSize);
		
		this.playerAnimPos += this.playerSpeed * this.playerAnimSpeed * delta;
		
		// Les touches Z, Q, S et D vont nous servir a bouger la caméra, de la même façon!
		// Attention ceci dit, GLFW ne fais pas vraiment de différence entre les clavier AZERTY et QWERTY, donc pour les touches Z, Q, S et D, il faut en réalité demander
		// les touches W, A, S et D.
		if(win.isDown(Nhengine.KEY_D)) {
			this.worldScene.getCamera().translateX(this.cameraSpeed * delta);
		}
		
		if(win.isDown(Nhengine.KEY_A)) {
			this.worldScene.getCamera().translateX(-this.cameraSpeed * delta);
		}
		
		if(win.isDown(Nhengine.KEY_W)) {
			this.worldScene.getCamera().translateY(-this.cameraSpeed * delta);
		}
		
		if(win.isDown(Nhengine.KEY_S)) {
			this.worldScene.getCamera().translateY(this.cameraSpeed * delta);
		}
		
		if(win.isPressed(Nhengine.KEY_F5)) {
			this.gameHudScene.startDialog(this.sansDial.getPage());
		}
		
		// Voilà, la boucle est bouclée!
	}
	
	public void textInput(int codepoint) {
	}
	
	public void keyPressed(int key, int scancode) {
		// Touche pressée!
		
		switch(key) {
			case DemoMain.GAME_KEY_LEFT:
				this.playerAnimPos = 0;
				if(!this.orientationCommands.contains(ORI_LEFT)) this.orientationCommands.add(ORI_LEFT);
				break;
			case DemoMain.GAME_KEY_RIGHT:
				this.playerAnimPos = 0;
				if(!this.orientationCommands.contains(ORI_RIGHT)) this.orientationCommands.add(ORI_RIGHT);
				break;
			case DemoMain.GAME_KEY_UP:
				this.playerAnimPos = 0;
				if(!this.orientationCommands.contains(ORI_UP)) this.orientationCommands.add(ORI_UP);
				break;
			case DemoMain.GAME_KEY_DOWN:
				this.playerAnimPos = 0;
				if(!this.orientationCommands.contains(ORI_DOWN)) this.orientationCommands.add(ORI_DOWN);
				break;
		}
	}
	
	public void keyReleased(int key, int scancode) {
		// Touche relachée!
		
		switch(key) {
			case DemoMain.GAME_KEY_LEFT:
				this.characterTilesComp.setActiveCell(0, this.playerOrientation);
				if(this.orientationCommands.contains(ORI_LEFT)) this.orientationCommands.remove(ORI_LEFT);
				break;
			case DemoMain.GAME_KEY_RIGHT:
				this.characterTilesComp.setActiveCell(0, this.playerOrientation);
				if(this.orientationCommands.contains(ORI_RIGHT)) this.orientationCommands.remove(ORI_RIGHT);
				break;
			case DemoMain.GAME_KEY_UP:
				this.characterTilesComp.setActiveCell(0, this.playerOrientation);
				if(this.orientationCommands.contains(ORI_UP)) this.orientationCommands.remove(ORI_UP);
				break;
			case DemoMain.GAME_KEY_DOWN:
				this.characterTilesComp.setActiveCell(0, this.playerOrientation);
				if(this.orientationCommands.contains(ORI_DOWN)) this.orientationCommands.remove(ORI_DOWN);
				break;
		}
	}
	
	public void mouseButtonPressed(float x, float y, int btn) {
		// Bouton de souris pressé!
	}
	
	public void mouseButtonReleased(float x, float y, int btn) {
		// Bouton de souris relaché!
	}
	
	public void mouseMoved(float newX, float newY, float relX, float relY) {
		// Souris bougée!
	}
	
	public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		// Roulette de la souris bougée!
	}
	
	public DemoWorldScene getWorldScene() {
		return this.worldScene;
	}
	
	public void dispose() {
		super.dispose();
		
		this.worldScene.dispose();
	}
	
	public void keyTyped(int key, int scancode) {
	}
}
