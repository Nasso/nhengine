package io.github.nasso.test.demo;

import java.io.IOException;

import org.joml.Vector2f;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.utils.CachedPerlin;

public class DemoWorldScene extends Scene {
	// Deux grilles pour le terrain:
	private TileMapComponent terrainBase; // la base: herbe ou eau
	private TileMapComponent terrainDecorationLayer; // les décorations: arbre, jonctions eau/terres (rivage)
	
	// La taille des grilles
	private int terrainSize = 64;
	
	// Node du joueur
	private Node terrainNode;
	private Node player;
	private float playerHeight = 1f; // W / H
	
	// Deux tile sets, un pour le terrain ("basictiles.png") et un pour les personnages ("characters.png")
	private Texture2D basicTiles;
	private Texture2D characterTiles;
	
	// Le sprite du joueur, comme on prend un tile d'une tileset, mieux vaut utiliser la classe TiledSpriteComponent
	private TiledSpriteComponent characterTilesComp;
	
	// Tout les components possibles pour le terrain:
	private TiledSpriteComponent grass; // De l'herbe
	private TiledSpriteComponent heavyGrass; // Beaucoup d'herbe
	private TiledSpriteComponent water; // De l'eau
	
	private TiledSpriteComponent tree; // un arbre
	
	// Jonctions possibles du rivage (eau/terres)
	private TiledSpriteComponent wgTop;
	private TiledSpriteComponent wgBot;
	private TiledSpriteComponent wgLeft;
	private TiledSpriteComponent wgRight;
	// ..
	private TiledSpriteComponent wgTopLeft;
	private TiledSpriteComponent wgTopRight;
	private TiledSpriteComponent wgBotLeft;
	private TiledSpriteComponent wgBotRight;
	// ..
	private TiledSpriteComponent wgTopLeftBot;
	private TiledSpriteComponent wgTopRightBot;
	private TiledSpriteComponent wgLeftBotRight;
	private TiledSpriteComponent wgLeftTopRight;
	// ..
	private TiledSpriteComponent wgLeftRight;
	private TiledSpriteComponent wgTopBot;
	// ..
	private TiledSpriteComponent wgAll;
	// ---------------------------------------------
	
	private Vector2f frameSize = new Vector2f();
	
	public DemoWorldScene() {
		super("World Scene");
		
		try {
			// On charge le tileset des tiles basiques...
			this.basicTiles = TextureIO.loadTexture2D("res/demo/textures/basictiles.png", 4, false, false, false, true);
			
			// ...et des characters
			this.characterTiles = TextureIO.loadTexture2D("res/demo/textures/cyber_kid.png", 4, false, false, false, true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		// Puis on setup tout les sprites du terrain
		// Arguments:
		// - Texture2D: la texture (le tileset des tiles basique)
		// - int: nombre de colonnes totales
		// - int: nombre de lignes totales
		// - int: colonne de la tile a utiliser
		// - int: ligne de la tile a utiliser
		// De cette manière, on obtient un sprite correspondant a un endroit spécifique dans le tileset, sans avoir a charger tout plein de fois la même image.
		this.tree = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 6, 4);
		this.grass = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 8);
		this.heavyGrass = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 8);
		this.water = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 5, 1);
		// ..
		this.wgTop = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 3);
		this.wgBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 5);
		this.wgLeft = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 4);
		this.wgRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 4);
		// ..
		this.wgTopLeft = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 3);
		this.wgTopRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 3);
		this.wgBotLeft = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 5);
		this.wgBotRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 5);
		// ..
		this.wgTopLeftBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 12);
		this.wgTopRightBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 12);
		this.wgLeftBotRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 13);
		this.wgLeftTopRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 13);
		// ..
		this.wgLeftRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 13);
		this.wgTopBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 12);
		// ..
		this.wgAll = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 11);
		
		// y compris celui du joueur, qu'on initialise par défaut a une position vers le bas
		this.characterTilesComp = new TiledSpriteComponent(this.characterTiles, 4, 4, 0, 2);
		this.characterTilesComp.setOpaque(false);
		
		this.playerHeight = (float) this.characterTiles.getHeight() / this.characterTiles.getWidth();
		
		// On récupère le noeud racine du niveau. C'est d'ici que tout part.
		Node root = this.getRoot();
		
		//
		// Terrain
		//
		// -- IMPORTANT --
		// Voici une petite démo montrant comment utiliser les ComponentCellRetriever.
		// Ce n'est qu'une démo, est ce n'est en AUCUN CAS la meilleure méthode, ni la
		// méthode recommendée de générer un terrain. Les components doivent être
		// prédéfinis, et le terrain devrait être chargé, et non pas généré a l'intérieur
		// de la méthode "getComponentAt". Cette méthode est appelée A CHAQUE FRAME pour
		// TOUTES LES CELLS visibles. Elle doit donc être assez rapide afin de garantir un
		// framerate correct.
		//
		
		// Un perlin noise, afin de générer un terrain aléatoire
		// Le premier argument prit par le constructeur est le seed. Si on le change, bah le terrain change.
		// Le second est un facteur d'echelle
		// Le dernier est la puissance, les valeurs sont tout simplement montées cette puissance (de manière centrée sur 0.5).
		CachedPerlin p = new CachedPerlin(0, 0.2f, 0.4f, this.terrainSize, this.terrainSize);
		
		// Le niveau de l'eau: en dessous de cette valeur, les tiles seront de l'eau
		float waterLevel = 0.3f;
		// Niveau au desssus duquel il y a beaucoup d'herbe
		float grassLevel = 0.7f;
		// Le niveau des arbres: au dessus de cette valeur, on aura des arbres.
		float treeLevel = 0.75f;
		
		// La base du terrain: une grille où on placera soit un sprite d'herbe, soit un sprite d'eau
		this.terrainBase = new TileMapComponent(this.terrainSize, this.terrainSize, 1, 1);
		
		// Les décorations: une grille également, de la même taille, qui viens ce superposer. On y place un arbre, ou bien un sprite de rivage adéquat.
		this.terrainDecorationLayer = new TileMapComponent(this.terrainSize, this.terrainSize, 1, 1);
		
		for(int x = 0; x < this.terrainSize; x++) {
			for(int y = 0; y < this.terrainSize; y++) {
				// On récupère la valeur que nous donne le perlin noise au coordonnées actuelles...
				// La valeur retournée est entre 0.0 et 1.0 
				float c = p.getValueAt(x, y);
				
				// Puis on choisi entre eau et herbe, en comparant cette valeur avec le waterLevel.
				if(c < waterLevel) this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.water);
				else if(c < grassLevel) this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.grass);
				else if(c < treeLevel) this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.heavyGrass);
				else this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.grass);
				
				// Décorations
				if(c < waterLevel) { // water
					// 4 booléens. Un pour chaque côté (en haut, en bas, a gauche et a droite). true si c'est des terres, false si c'est de l'eau.
					boolean ct = p.getValueAt(x, y - 1) > waterLevel;
					boolean cb = p.getValueAt(x, y + 1) > waterLevel;
					boolean cl = p.getValueAt(x - 1, y) > waterLevel;
					boolean cr = p.getValueAt(x + 1, y) > waterLevel;
					
					// Puis un gros if, pour tester tout les cas de figure, et choisir le bon sprite.
					if(ct && cb && cl && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgAll);
					else if(ct && cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopLeftBot);
					else if(ct && !cl && cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopRightBot);
					else if(!ct && cl && cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeftBotRight);
					else if(ct && cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeftTopRight);
					else if(!ct && cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeftRight);
					else if(ct && !cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopBot);
					else if(ct && cl && !cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopLeft);
					else if(ct && !cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopRight);
					else if(!ct && cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgBotLeft);
					else if(!ct && !cl && cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgBotRight);
					else if(ct && !cl && !cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTop);
					else if(!ct && cl && !cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeft);
					else if(!ct && !cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgBot);
					else if(!ct && !cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgRight);
				} else if(c > treeLevel) {
					// Sinon, on est sur terre. On vérifie si un arbre est censé être là, et si oui, on le plante! Pour ça, on compare la valeur du perlin avec le treeLevel.
					this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.tree);
				}
			}
		}
		
		// La position dans le plan des terrains (plus grand = plus proche de la caméra = passe devant les objets plus loin)
		this.terrainBase.setDepth(0); // La base
		this.terrainDecorationLayer.setDepth(1); // Les décos
		// La base est donc DERRIÈRE les décorations. Donc la base sera dessinée "avant" les décoration. Si on fait pas ça, les déco seront derrière et donc on verrait rien.
		
		// On créé le joueur...
		this.player = new Node();
		this.player.setDepth(2); // Au dessus des décorations
		this.player.setPosition(2, 2); // Une position initiale, parceque pourquoi pas.
		this.player.addComponent(this.characterTilesComp); // Et on lui assigne un sprite.
		this.characterTilesComp.setScale(1, this.playerHeight);
		
		this.terrainNode = new Node();
		this.terrainNode.setPosition(-this.terrainSize / 2f, -this.terrainSize / 2f);
		this.terrainNode.addComponents(this.terrainBase, this.terrainDecorationLayer);
		
		// Enfin, il nous suffit de tout rajouter a la racine du niveau, et hop, c'est fini!
		root.addChild(this.terrainNode);
		root.addChild(this.player); // Le joueur!
		this.getCamera().setScale(6);
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		// Enfin, ne pas oublier de changer automatiquement le ratio de la caméra lorsque la fenêtre est redimensionnée! Sinon, on aurait un truc qui s'applatti et tout, pas beau!
		if(win.getFrameWidth() != this.frameSize.x || win.getFrameHeight() != this.frameSize.y) {
			this.frameSize.set(win.getFrameWidth(), win.getFrameHeight());
			
			// Le ratio est calculé de manière très simple, en divisant la longueur par la hauteur.
			// On a donc le choix entre deux surcharge pour la méthode "setAspectRatio":
			// - Une qui prend un float, étant le ratio. On doit faire la division nous même EN S'ASSURANT QUE LA DIVISION EST UNE DIVISION A VIRGULE!!!
			// - Une qui prend la longueur et la largeur, en float. Java nous fait automatiquement la conversion int -> float pour les appels de fonctions, donc elle est plus simple!
			//   	en plus, cette méthode se charge de faire la division correctement ducoup, donc bon, c'est généralement mieux de l'utiliser pour le coup.
			
			// Là voici, là voilà!
			this.getCamera().setAspectRatio(this.frameSize.x, this.frameSize.y);
		}
	}
	
	public Node getPlayer() {
		return this.player;
	}
	
	public TiledSpriteComponent getPlayerTiledSprite() {
		return this.characterTilesComp;
	}
	
	public void dispose() {
		super.dispose();
		
		this.basicTiles.dispose();
		this.characterTiles.dispose();
	}
}
