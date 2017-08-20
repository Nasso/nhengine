package io.github.nasso.nhengine.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TileMapComponent.TileSet;
import io.github.nasso.nhengine.utils.Nhutils;

/**
 * 
 * @author Kadau
 * @author nasso
 */
public class TiledFormatLoader {
	private TiledFormatLoader() {
	}
	
	private static class TileMapDef {
		private static class TileMapTileSetEntryDef {
			String source;
		}
		
		private static class TileMapLayerDef {
			int[] data;
			
		}
		
		int width;
		int height;
		
		int tilewidth;
		int tileheight;
		
		String orientation;
		TileMapTileSetEntryDef[] tilesets;
		
		TileMapLayerDef[] layers;
	}
	
	private static class TileSetDef {
		String image;
		
		int columns;
		int tilecount;
	}
	
	private static Gson gson = new Gson();
	
	public static TileMapComponent loadJSON(String mapFilePathStr, boolean inJar) throws IOException {
		// 1 - Read the tile-map json:
		Path mapFilePath = Paths.get(mapFilePathStr);
		TileMapDef tileMapDef = gson.fromJson(Nhutils.readFile(mapFilePathStr, inJar), TileMapDef.class);
		boolean isometric = tileMapDef.orientation.equals("isometric");
		
		// 2 - Read the tile-set json:
		Path tileSetPath = mapFilePath.getParent().resolve(tileMapDef.tilesets[0].source);
		TileSetDef tileSetDef = gson.fromJson(Nhutils.readFile(tileSetPath.toString(), inJar), TileSetDef.class);
		
		// 3 - Create the tile-set:
		TileSet tileSet = new TileSet(TextureIO.loadTexture2D(tileSetPath.getParent().resolve(tileSetDef.image).toString(), 4, false, false, false, inJar), tileSetDef.columns, tileSetDef.tilecount / tileSetDef.columns);
		
		// 4 - Construct the actual tile-map:
		int[] dataArray = tileMapDef.layers[0].data;
		
		TileMapComponent tMap = new TileMapComponent(tileSet, tileMapDef.width, tileMapDef.height, tileMapDef.tilewidth, tileMapDef.tileheight);
		
		for(int i = 0; i < dataArray.length; i++) {
			int id = dataArray[i] - 1;
			int x = i % tMap.getMapSizeX();
			int y = i / tMap.getMapSizeX();
			
			tMap.setTileAt(x, y, id);
		}
		
		tMap.setIsometric(isometric);
		
		return tMap;
	}
}
