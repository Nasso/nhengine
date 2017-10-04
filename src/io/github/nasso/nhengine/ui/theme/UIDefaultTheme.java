package io.github.nasso.nhengine.ui.theme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import io.github.nasso.nhengine.data.TrueTypeFont;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.FontFamily;
import io.github.nasso.nhengine.utils.Nhutils;

public abstract class UIDefaultTheme extends UITheme {
	
	public UIDefaultTheme(boolean fontSmoothing, String descPath) {
		try {
			int size = 18;
			
			FontFamily font = new FontFamily();
			font.setCondensed(new TrueTypeFont("res/fonts/Ubuntu-C.ttf", size, fontSmoothing, true));
			font.setLight(new TrueTypeFont("res/fonts/Ubuntu-L.ttf", size, fontSmoothing, true));
			font.setLightItalic(new TrueTypeFont("res/fonts/Ubuntu-LI.ttf", size, fontSmoothing, true));
			font.setRegular(new TrueTypeFont("res/fonts/Ubuntu-R.ttf", size, fontSmoothing, true));
			font.setRegularItalic(new TrueTypeFont("res/fonts/Ubuntu-RI.ttf", size, fontSmoothing, true));
			font.setMedium(new TrueTypeFont("res/fonts/Ubuntu-M.ttf", size, fontSmoothing, true));
			font.setMediumItalic(new TrueTypeFont("res/fonts/Ubuntu-MI.ttf", size, fontSmoothing, true));
			font.setBold(new TrueTypeFont("res/fonts/Ubuntu-B.ttf", size, fontSmoothing, true));
			font.setBoldItalic(new TrueTypeFont("res/fonts/Ubuntu-BI.ttf", size, fontSmoothing, true));
			
			this.putFontFamily("globalFont", font);
			
			this.loadDesc(descPath);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDesc(String descPath) throws IOException {
		String desc = Nhutils.readFile(descPath, true);
		
		BufferedReader reader = new BufferedReader(new StringReader(desc));
		String line = null;
		
		while((line = reader.readLine()) != null) {
			if(line.isEmpty()) continue;
			
			line = line.replaceAll(" ", "");
			String[] members = line.split("=");
			
			if(members[1].startsWith("rgba")) {
				String[] values = members[1].substring(5, members[1].length() - 1).split(",");
				
				this.putColor(members[0], Color.get(
					Float.valueOf(values[0]),
					Float.valueOf(values[1]),
					Float.valueOf(values[2]),
					Float.valueOf(values[3])
				));
			} else if(members[1].startsWith("rgb")) {
				String[] values = members[1].substring(4, members[1].length() - 1).split(",");

				this.putColor(members[0], Color.get(
					Float.valueOf(values[0]),
					Float.valueOf(values[1]),
					Float.valueOf(values[2])
				));
			} else if(members[1].startsWith("gray")) {
				this.putColor(members[0], Color.get(
						Float.valueOf(members[1].substring(5, members[1].length() - 1))
				));
			} else { // number
				this.putNumber(members[0], Float.valueOf(members[1]));
			}
		}
	}
}
