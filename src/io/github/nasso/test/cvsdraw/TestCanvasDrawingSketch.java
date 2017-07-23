package io.github.nasso.test.cvsdraw;

import java.io.IOException;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.data.TrueTypeFont;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.LineCap;
import io.github.nasso.nhengine.graphics.TextBaseline;
import penner.easing.Sine;

public class TestCanvasDrawingSketch {
	private float time = 0;
	
	private TrueTypeFont fnt;
	
	public void setup() {
		try {
			this.fnt = new TrueTypeFont("res/fonts/Ubuntu-R.ttf", 18, false, true);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose() {
		this.fnt.dispose();
	}
	
	public void update(float delta) {
		this.time += delta / 1000.0f;
	}
	
	private void drawMyNick(GraphicsContext2D gtx, float w, float h) {
		float t = (this.time % 3f) / 3f;
		float d = (this.time % 3f) / 0.5f;
		d = d > 1 ? 1 : d;
		
		t = Sine.easeOut(t, 0, 1, 1);
		d = Sine.easeOut(d, 0, 1, 1);
		
		gtx.translate(w / 2, h / 2);
		gtx.scale(1.0f + t * 0.1f, 1.0f + t * 0.1f);
		
		float charSize = 64;
		float charSpace = 20;
		float charCount = 5; // n,a,s,s,o
		float lineWidth = 8;
		float aoBaseMargin = 4;
		
		float cshalf = charSize / 2;
		
		gtx.translate(-(charSize * (charCount - 1) + charSpace * (charCount - 1)) / 2, 0);
		gtx.setStrokeSize(lineWidth);
		gtx.setLineCap(LineCap.ROUND);
		
		gtx.beginPath();
		// n
		gtx.save();
		{
			float nLength = cshalf * 2 + 3.141593f * cshalf;
			
			gtx.moveTo(-cshalf, cshalf);
			
			if(d * nLength < cshalf) {
				gtx.lineTo(-cshalf, cshalf * (1 - d / (cshalf / nLength)));
			} else if(d * nLength < cshalf + 3.141593f * cshalf) {
				gtx.arc(0, 0, cshalf, 180, 180 + (d * nLength - cshalf) / (3.141593f * cshalf) * 180, true);
			} else {
				gtx.arc(0, 0, cshalf, 180, 0, true);
				
				gtx.lineTo(cshalf, (d * nLength - (cshalf + 3.141593f * cshalf)) / cshalf * cshalf);
			}
		}
		gtx.restore();
		gtx.translate(charSize + charSpace, 0);
		
		// as
		gtx.save();
		{
			// a
			gtx.moveTo(cshalf, cshalf - lineWidth - aoBaseMargin);
			gtx.arc(0, 0, cshalf, 0, -270, false);
			gtx.lineTo(cshalf, cshalf);
			
			// translate to s position
			gtx.translate(charSize + charSpace, 0);
			
			// s
			gtx.arc(cshalf / 2, cshalf / 2, cshalf / 2, 90, -90, false);
			gtx.arc(-cshalf / 2, -cshalf / 2, cshalf / 2, 90, 270, true);
			gtx.lineTo(cshalf, -cshalf);
		}
		gtx.restore();
		
		// Translate 2 times because we wrote 2 letters
		gtx.translate(charSize + charSpace, 0);
		gtx.translate(charSize + charSpace, 0);
		
		// so
		gtx.save();
		{
			// s
			gtx.moveTo(-cshalf, cshalf);
			gtx.arc(cshalf / 2, cshalf / 2, cshalf / 2, 90, -90, false);
			gtx.arc(-cshalf / 2, -cshalf / 2, cshalf / 2, 90, 270, true);
			
			// translate to o position
			gtx.translate(charSize + charSpace, 0);
			gtx.arc(0, 0, cshalf, 270, 180, true);
			gtx.lineTo(-cshalf, -cshalf + lineWidth + aoBaseMargin);
		}
		gtx.restore();
		
		gtx.stroke();
	}
	
	public void draw(GraphicsContext2D gtx) {
		gtx.setFill(Color.BLACK);
		gtx.fillRect(0, 0, TestCanvasDrawingMain.WIDTH, TestCanvasDrawingMain.HEIGHT);
		
		gtx.setFont(this.fnt);
		gtx.setFill(Color.WHITE);
		gtx.setStroke(Color.WHITE);
		
		gtx.save();
		{
			this.drawMyNick(gtx, TestCanvasDrawingMain.WIDTH, TestCanvasDrawingMain.HEIGHT);
		}
		gtx.restore();
		
		gtx.setTextBaseline(TextBaseline.TOP);
		gtx.fillText("FPS: " + Game.instance().getFPS(), 4, 4);
	}
}
