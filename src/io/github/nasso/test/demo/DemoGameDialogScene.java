package io.github.nasso.test.demo;

import static io.github.nasso.test.demo.DemoDialog.*;

import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.test.demo.DemoDialog.DialogPage;

public class DemoGameDialogScene extends Scene {
	private static final int DIALOG_BOX_LINE_COUNT = 3;
	
	private StringBuffer textLines[] = new StringBuffer[DIALOG_BOX_LINE_COUNT];
	private StringBuffer textColors[] = new StringBuffer[DIALOG_BOX_LINE_COUNT];
	private int lineWriteCursor[] = new int[DIALOG_BOX_LINE_COUNT];
	
	private DialogPage page = null;
	
	private char currentColor = DemoDialog.MACRO_VALUE_DEFAULT;
	
	private CanvasComponent cvs;
	private GraphicsContext2D gtx;
	
	private int skippedFramesCounter = 0;
	private boolean hasSkipped = false;
	
	public DemoGameDialogScene() {
		super("Game Dialog Scene");
		
		this.cvs = new CanvasComponent(DemoMain.GAME_WIDTH, DemoMain.GAME_HEIGHT);
		this.cvs.setScale(DemoMain.GAME_WIDTH, DemoMain.GAME_HEIGHT);
		this.gtx = this.cvs.getContext2D();
		
		this.getRoot().addComponent(this.cvs);
		
		this.getCamera().setViewport2D(DemoMain.GAME_WIDTH, DemoMain.GAME_HEIGHT);
		
		for(int i = 0; i < DIALOG_BOX_LINE_COUNT; i++) {
			this.textLines[i] = new StringBuffer();
			this.textColors[i] = new StringBuffer();
			this.lineWriteCursor[i] = 0;
		}
	}
	
	private void redraw2D() {
		this.gtx.discardAll();
		
		this.gtx.clear();
		
		this.gtx.save();
		{
			this.gtx.translate(32, 320);
			this.gtx.scale(2, 2);
			
			this.gtx.setFill(Color.WHITE);
			this.gtx.fillRect(0, 0, 289, 76);
			this.gtx.setFill(Color.BLACK);
			this.gtx.fillRect(3, 3, 283, 70);
			
			if(this.page.getFace() != null) {
				this.gtx.drawImage(this.page.getFace(), 16, 14);
			}
			
			this.gtx.setFill(Color.WHITE);
			this.gtx.setFont(this.page.getFont());
			this.gtx.setTextBaseline(TextBaseline.TOP);
			
			char color = MACRO_VALUE_DEFAULT;
			char oldColor = MACRO_VALUE_DEFAULT;
			for(int i = 0; i < DIALOG_BOX_LINE_COUNT; i++) {
				float cursx = 72;
				
				for(int x = 0; x < this.textLines[i].length(); x++) {
					color = this.textColors[i].charAt(x);
					
					if(oldColor != color) {
						oldColor = color;
						switch(color) {
							case MACRO_VALUE_DEFAULT:
								this.gtx.setFill(1, 1, 1);
								break;
							case MACRO_VALUE_RED:
								this.gtx.setFill(1, 0, 0);
								break;
							case MACRO_VALUE_BLUE:
								this.gtx.setFill(0, 0, 1);
								break;
							case MACRO_VALUE_GREEN:
								this.gtx.setFill(0, 1, 0);
								break;
							case MACRO_VALUE_PURPLE:
								this.gtx.setFill(1, 0, 1);
								break;
							case MACRO_VALUE_YELLOW:
								this.gtx.setFill(1, 1, 0);
								break;
						}
					}
					
					this.gtx.fillText(this.textLines[i].substring(x, x + 1), cursx, 13 + 18 * i);
					
					cursx += this.page.getFont().getPackedGlyph(this.textLines[i].charAt(x)).xadvance();
				}
			}
		}
		this.gtx.restore();
	}
	
	public void startDialog(DialogPage page) {
		if(page != null) {
			this.currentColor = DemoDialog.MACRO_VALUE_DEFAULT;
			this.hasSkipped = false;
			this.page = page;
			
			for(int i = 0; i < DIALOG_BOX_LINE_COUNT; i++) {
				this.textLines[i].setLength(0);
				this.textColors[i].setLength(0);
				this.lineWriteCursor[i] = 0;
			}
		}
	}
	
	private boolean typeDial(String str, boolean skipAll) {
		if(!skipAll && this.skippedFramesCounter < this.page.getSpeed()) {
			this.skippedFramesCounter++;
			return false;
		}
		
		this.skippedFramesCounter = 0;
		
		int line = 0;
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			
			if(c == DemoDialog.MACRO_LINE_SEPARATOR) {
				line++;
				continue;
			}
			
			while(c == '\\') {
				this.currentColor = str.charAt(++i);
				
				if(++i >= str.length()) return true;
				
				c = str.charAt(i);
			}
			
			if(i < this.lineWriteCursor[line]) {
				continue;
			}
			
			this.lineWriteCursor[line] = i + 1;
			
			if(c != '\0') {
				this.textLines[line].append(c);
				this.textColors[line].append(this.currentColor);
				if(this.page.getVoice() != null && !skipAll) Game.instance().getAudioPlayer().playSound(this.page.getVoice(), 0.8f);
			}
			
			return false;
		}
		
		return true;
	}
	
	private void updatePage(DialogPage textPage) {
		GameWindow win = Game.instance().window();
		
		this.hasSkipped |= textPage.isSkippable() && win.isPressed(DemoMain.GAME_KEY_RETURN);
		
		boolean hasDialEnded = this.typeDial(this.page.getText(), this.hasSkipped);
		this.redraw2D();
		
		if(hasDialEnded) {
			// Dialog end
			if(win.isPressed(DemoMain.GAME_KEY_ACT)) {
				this.startDialog(textPage.getNext());
			}
		}
	}
	
	public void update(float delta) {
		if(this.page != null) {
			this.updatePage(this.page);
		}
	}
}
