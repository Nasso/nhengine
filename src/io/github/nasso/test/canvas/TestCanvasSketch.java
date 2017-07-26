package io.github.nasso.test.canvas;

import java.io.IOException;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.data.ScalableVectorGraphics;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.data.TrueTypeFont;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Nhutils;
import io.github.nasso.nhengine.utils.easing.CubicBezierEasing;

public class TestCanvasSketch {
	private float time = 0;
	
	private static final String[] EMOJI_CODES = { "1f60a", "1f602", "1f606", "1f60c", "1f608", "1f60d", "2668" };
	
	private TrueTypeFont fnt, emojiFnt;
	private Texture2D img;
	
	private String lorem, emojiStr;
	
	private ScalableVectorGraphics svg;
	private ScalableVectorGraphics[] svgList = new ScalableVectorGraphics[EMOJI_CODES.length];
	
	private CubicBezierEasing easing = new CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f);
	
	public void setup() {
		// @format:off
		this.emojiStr = "lol";// @format:on
		
		try {
			this.lorem = Nhutils.readFile("res/demo/lorem.txt", true);
			this.fnt = new TrueTypeFont("res/fonts/Ubuntu-R.ttf", 18, false, true);
			this.emojiFnt = new TrueTypeFont("res/fonts/NotoEmoji-Regular.ttf", 48, false, true);
			
			this.img = TextureIO.loadTexture2D("res/demo/images/image1.jpg", 4, false, false, false, true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < EMOJI_CODES.length; i++) {
			try {
				final int index = i;
				Nhutils.getURLInputStream("https://twemoji.maxcdn.com/svg/" + EMOJI_CODES[i] + ".svg", (in) -> {
					try {
						this.svgList[index] = new ScalableVectorGraphics(in);
						in.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				});
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void dispose() {
		this.fnt.dispose();
	}
	
	public void update(GameWindow win, float delta) {
		this.time += delta / 1000.0f;
		
		this.svg = this.svgList[(int) (this.time % this.svgList.length)];
	}
	
	public void draw(GraphicsContext2D gtx) {
		gtx.setFill(Color.WHITE);
		gtx.fillRect(0, 0, TestCanvasMain.WIDTH, TestCanvasMain.HEIGHT);
		
		gtx.setFont(this.fnt);
		
		gtx.save();
		{
			gtx.translate(100, 0);
			gtx.rotate(45);
			
			gtx.setStroke(Color.RED);
			gtx.strokeRect(50, 50, 100, 100);
			
			gtx.clip(50, 50, 100, 100);
			
			gtx.translate(20, 0);
			
			gtx.setFill(Color.GREEN);
			gtx.fillRoundedRect(10, 20, 100, 100, 25);
			
			gtx.setFill(Color.BLACK);
			gtx.fillText("Hello world!", 70, 125);
			
			gtx.strokeRect(0, 0, 70, 70);
			gtx.clip(0, 0, 70, 70);
			
			gtx.setFill(Color.BLUE);
			gtx.fillRoundedRect(0, 0, 100, 100, 25);
			
			gtx.setFill(Color.BLACK);
			gtx.fillText("Hello world!", 20, 60);
		}
		gtx.restore();
		
		gtx.save();
		{
			gtx.translate(300 + ((float) Math.cos(this.time * 2)) * 100, 100);
			
			gtx.setStroke(Color.RED);
			gtx.strokeRect(50, 50, 100, 100);
			
			gtx.clip(50, 50, 100, 100);
			
			gtx.translate(20, 0);
			
			gtx.setFill(Color.GREEN);
			gtx.fillRoundedRect(10, 20, 100, 100, 25);
			
			gtx.setFill(Color.BLACK);
			gtx.fillText("Hello world!", 70, 125);
			
			gtx.setFill(Color.BLUE);
			gtx.fillRoundedRect(0, 0, 100, 100, 25);
		}
		gtx.restore();
		
		gtx.save();
		{
			gtx.translate(0, 210);
			
			gtx.setStroke(Color.RED);
			gtx.strokeRect(50, 50, 100, 100);
			
			gtx.clip(50, 50, 100, 100);
			
			gtx.drawImage(this.img, 30, 50);
		}
		gtx.restore();
		
		if(this.svg != null) {
			gtx.save();
			{
				gtx.translate(580, 10);
				gtx.scale(4, 4);
				this.svg.draw(gtx);
			}
			gtx.restore();
		}
		
		gtx.save();
		{
			gtx.setStroke(0, 0, 0);
			gtx.setStrokeSize(0.02f);
			
			gtx.translate(600, 350);
			gtx.scale(120, -120);
			
			float t = this.time % 4f / 4f * 2;
			t = t > 1 ? 1 - t + 1 : t;
			
			float x = t;
			float y = this.easing.apply(t, 0, 1, 1);
			
			gtx.beginPath();
			gtx.moveTo(0, 0);
			gtx.bezierTo(this.easing.x1, this.easing.y1, this.easing.x2, this.easing.y2, 1, 1);
			gtx.stroke();
			
			gtx.setStroke(1, 0, 0);
			gtx.beginPath();
			gtx.moveTo(0, y);
			gtx.lineTo(1, y);
			gtx.moveTo(x, 0);
			gtx.lineTo(x, 1);
			gtx.stroke();
		}
		gtx.restore();
		
		gtx.save();
		{
			gtx.setFill(Color.DARK_GRAY);
			gtx.fillRect(0, 390, TestCanvasMain.WIDTH, TestCanvasMain.HEIGHT - 390);
			
			gtx.translate(TestCanvasMain.WIDTH / 2f, 400 + (TestCanvasMain.HEIGHT - 400) * 0.5f);
			
			gtx.setTextAlign(TextAlignment.CENTER);
			gtx.setTextBaseline(TextBaseline.MIDDLE);
			
			gtx.setFill(Color.GRAY);
			gtx.fillText(this.lorem, 0, 0);
			
			gtx.setFill(255f / 255f, 204f / 255f, 77f / 255f);
			gtx.setFont(this.emojiFnt);
			gtx.fillText(this.emojiStr, 0, 0);
		}
		gtx.restore();
		
		gtx.setFill(Color.BLACK);
		gtx.setTextBaseline(TextBaseline.TOP);
		gtx.fillText("FPS: " + Game.instance().getFPS(), 4, 4);
	}
}
