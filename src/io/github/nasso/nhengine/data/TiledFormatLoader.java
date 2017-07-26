package io.github.nasso.nhengine.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.graphics.Texture2D;

public class TiledFormatLoader {
	// Return tileMapComponent
	public static TileMapComponent parseTiledJSON(String mapFilePath) throws IOException{
		
		Gson parser = new Gson(); // JSON Parser see: https://github.com/google/gson
		FileReader mapReader = new FileReader(mapFilePath); // JSON Map file reader
		TileMapComponent tMap; // Return map
		JsonObject parsedMap; // Parsen & Readen JSON file
		JsonObject parsedTileMap; // Parsen & Readen JSON Map file
		String tilesetSource = ""; // Tileset path
		TiledSpriteComponent[] tilesSprite; // Tile sprites
		Texture2D sprite = null; // Tilemap image
		int[] tiles = null; // Tiles IDs
		
		parsedMap = parser.fromJson(mapReader, JsonObject.class); // Parse JSON String to JsonObject
		
		// Instantiate map
		tMap = new TileMapComponent(parsedMap.get("width").getAsInt(), parsedMap.get("height").getAsInt(), parsedMap.get("tilewidth").getAsInt(), parsedMap.get("tileheight").getAsInt());
				
		// Get tileset path
		tilesetSource = parsedMap.get("tilesets").getAsJsonArray().get(0).getAsJsonObject().get("source").getAsString(); // Only supports 1 Tileset	
		
		// Get tilemap infos json
		parsedTileMap = parser.fromJson(new FileReader(new File(mapFilePath).getParent() + "/" + tilesetSource), JsonObject.class);
		
		// Allocate array space for tiles from tilemap
		tilesSprite = new TiledSpriteComponent[parsedTileMap.get("tilecount").getAsInt()];
		
		// Get tilemap image
		sprite = TextureIO.loadTexture2D(new File(mapFilePath).getParent() + "/" + parsedTileMap.get("image").getAsString()); // Get tileset image
		
		// Get main array
		JsonArray arrayInts = parsedMap.get("layers").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonArray();
		
		// Allocate array space for level 
		tiles = new int[tMap.getSizeX()*tMap.getSizeY()];
		
		// Reads ints tiles from json array
		for(int i=0; i<tMap.getSizeX()*tMap.getSizeY(); i++){ 
			tiles[i] = arrayInts.get(i).getAsInt();
		}
		
		// Add tilesets readen from file to tilemap
		addTilesetToTilemap(tMap, tilesSprite, parsedMap, parsedTileMap, sprite);
		
		// Add tiles to level map
		addTilesToLevel(tiles, tMap, tilesSprite);
		
		System.out.println("[Tiled Importer] Importing tilemap, Tiled version: " + parsedMap.get("tiledversion").getAsString());
		
		return tMap;
	}
	
	private static void addTilesetToTilemap(TileMapComponent map, TiledSpriteComponent[] tilesSprites, JsonObject parsedMap, JsonObject parsedTileMap, Texture2D tilemap){
		int x = 0;
		int y = 0;
		
		// Add tilesets to tilemap 
		for(int i=0; i<parsedTileMap.get("tilecount").getAsInt(); i++){
			if(x == parsedTileMap.get("columns").getAsInt()){ // Increment Y on max X
				y++;
				x = 0;		
			}

			TiledSpriteComponent tsp = new TiledSpriteComponent(tilemap, parsedTileMap.get("columns").getAsInt(), parsedTileMap.get("tilecount").getAsInt()/parsedTileMap.get("columns").getAsInt(), x, y);
			
			tsp.setSize(parsedMap.get("tilewidth").getAsInt(), parsedTileMap.get("tileheight").getAsInt());
			
			// Add tile to array
			tilesSprites[i] = tsp;
					
			// Skip to next tile
			x++;
		}
	}
	
	private static void addTilesToLevel(int[] tilesID, TileMapComponent tileMap, TiledSpriteComponent[] tiledSprites){
		int x = 0;
		int y = 0;
		
		// Add tiles to level
		for (int tiledSpriteComponentID : tilesID) {
			tileMap.setSpriteComponent(x, y, getSpriteFromID(tiledSpriteComponentID, tiledSprites));				
			x++;		
			if(x == tileMap.getSizeX()){
				y++;
				x=0;
			}				
		}
	}
	
	private static int getIdFromPos(int x, int y, int mapX){
		int ID = x;
		ID += y*mapX;
		return ID;
	}
	
	private static TiledSpriteComponent getSpriteFromID(int id, TiledSpriteComponent[] array){
		return array[id-1];
	}
	
}
