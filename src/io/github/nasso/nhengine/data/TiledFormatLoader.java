package io.github.nasso.nhengine.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.utils.Nhutils;

/**
 * 
 * @author Kadau
 * @author nasso
 */
public class TiledFormatLoader {
	private TiledFormatLoader() {
	}
	
	private static class TileSetDef {
		String image;
		
		int columns;
		int tilecount;
		
		int firstgid;
		
		public boolean containsGlobalID(int id) {
			id -= this.firstgid - 1;
			return id >= 0 && id < this.tilecount;
		}
	}
	
	private static class LayerDef {
		String type;
		
		int[] data;
		
		float offsetx;
		float offsety;
		
		float opacity;
		boolean visible;
	}
	
	private static class TileMapDef {
		int width;
		int height;
		
		int tilewidth;
		int tileheight;
		
		String orientation;
		
		LayerDef[] layers;
		TileSetDef[] tilesets;
	}
	
	private static Gson gson = new Gson();
	
	public static TileMapComponent loadJSON(String mapFilePathStr, boolean inJar) throws IOException {
		// 1 - Read the tile-map json:
		Path mapFilePath = Paths.get(mapFilePathStr);
		TileMapDef tileMapDef = gson.fromJson(Nhutils.readFile(mapFilePathStr, inJar), TileMapDef.class);
		
		// 2 - Create the TileMapComponent
		TileMapComponent tMap = new TileMapComponent(tileMapDef.width, tileMapDef.height, tileMapDef.tilewidth, tileMapDef.tileheight);
		tMap.setIsometric(tileMapDef.orientation.equals("isometric"));
		
		// 3 - Create the tile-sets:
		for(int i = 0; i < tileMapDef.tilesets.length; i++) {
			TileSetDef tileSetDef = tileMapDef.tilesets[i];
			
			tMap.createTileSet(
					TextureIO.loadTexture2D(mapFilePath.getParent().resolve(tileSetDef.image).toString(), 4, false, false, false, inJar),
					tileSetDef.columns,
					tileSetDef.tilecount / tileSetDef.columns
			);
		}
		
		// 4 - Create the layers:
		LayerDef[] layers = tileMapDef.layers;
		
		for(int i = 0; i < layers.length; i++) {
			LayerDef layerDef = layers[i];
			
			if(layerDef.type.equals("tilelayer")) {
				int[] dataArray = layerDef.data;
				TileMapComponent.Layer layer = tMap.createLayer();
				layer.setOpacity(layerDef.opacity);
				layer.setVisible(layerDef.visible);
				layer.setHorizontalOffset(layerDef.offsetx);
				layer.setVerticalOffset(layerDef.offsety);
				
				for(int j = 0; j < dataArray.length; j++) {
					int id = dataArray[j];
					int x = j % tMap.getMapWidth();
					int y = j / tMap.getMapWidth();
					
					for(int s = 0; s < tileMapDef.tilesets.length; s++) {
						TileSetDef tileSetDef = tileMapDef.tilesets[s];
						
						if(tileSetDef.containsGlobalID(id)) {
							// To Tiled local
							id -= tileSetDef.firstgid;
							
							// To Nhengine global
							id += tMap.getTileSet(s).getFirstGlobalID();
							break;
						}
					}
					
					layer.setTileAt(x, y, id);
				}
			}
		}
		
		return tMap;
	}
}
