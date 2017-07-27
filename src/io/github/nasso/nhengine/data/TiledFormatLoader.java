package io.github.nasso.nhengine.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Nhutils;

/**
 * 
 * @author Kadau
 * @author nasso
 */
public class TiledFormatLoader {
	private TiledFormatLoader() {
	}
	
	private static class TileSet {
		String image;
		
		int columns;
		int tilecount;
		int tileheight;
		int tilewidth;
	}
	
	private static Gson gson = new Gson();
	
	public static TileMapComponent loadJSON(String mapFilePathStr, boolean inJar) throws IOException {
		// 1 - Read the tile-map json:
		Path mapFilePath = Paths.get(mapFilePathStr);
		JsonObject tileMapJSON = gson.fromJson(Nhutils.readFile(mapFilePathStr, inJar), JsonObject.class);
		boolean isometric = tileMapJSON.get("orientation").getAsString().equals("isometric");
		
		// 2 - Read the tile-set json:
		Path tileSetPath = mapFilePath.getParent().resolve(tileMapJSON.get("tilesets").getAsJsonArray().get(0).getAsJsonObject().get("source").getAsString());
		TileSet tileSet = gson.fromJson(Nhutils.readFile(tileSetPath.toString(), inJar), TileSet.class);
		
		// 3 - Load the tile-set texture:
		Texture2D sprite = TextureIO.loadTexture2D(tileSetPath.getParent().resolve(tileSet.image).toString(), 4, false, false, false, inJar);
		
		// 4 - Construct the tiles of the tile-set:
		TiledSpriteComponent[] tiles = new TiledSpriteComponent[tileSet.tilecount];
		for(int i = 0; i < tiles.length; i++) {
			int x = i % tileSet.columns;
			int y = i / tileSet.columns;
			
			tiles[i] = new TiledSpriteComponent(sprite, tileSet.columns, tileSet.tilecount / tileSet.columns, x, y);
			tiles[i].setSize(tileSet.tilewidth, tileSet.tileheight);
		}
		
		// 5 - Construct the actual tile-map:
		JsonArray dataArray = tileMapJSON.get("layers").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonArray();
		
		TileMapComponent tMap = new TileMapComponent(tileMapJSON.get("width").getAsInt(), tileMapJSON.get("height").getAsInt(), tileMapJSON.get("tilewidth").getAsInt(), tileMapJSON.get("tileheight").getAsInt());
		for(int i = 0; i < dataArray.size(); i++) {
			int id = dataArray.get(i).getAsInt() - 1;
			int x = i % tMap.getSizeX();
			int y = i / tMap.getSizeX();
			
			tMap.setSpriteComponent(x, y, tiles[id]);
		}
		
		tMap.setIsometric(isometric);
		
		return tMap;
	}
}
