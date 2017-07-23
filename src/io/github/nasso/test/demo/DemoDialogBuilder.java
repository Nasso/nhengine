package io.github.nasso.test.demo;

import java.util.ArrayList;
import java.util.List;

public class DemoDialogBuilder extends DemoDialog {
	private List<DialogPage> pages = new ArrayList<DialogPage>();
	
	protected void appendPage(String txt, int face, int font, int voice, int effect, int speed) {
		this.pages.add(new DialogPage(DemoAssets.getSprite(face), txt, DemoAssets.getSound(voice), DemoAssets.getFont(font), speed, effect, false));
	}
	
	protected void appendPage(String txt, int face, int font, int voice, int effect) {
		this.appendPage(txt, face, font, voice, effect, DEFAULT_DIALOG_SPEED);
	}
	
	protected void appendPage(String txt, int face, int font, int voice) {
		this.appendPage(txt, face, font, voice, 0);
	}
	
	protected void applyPages() {
		if(this.pages.isEmpty()) return;
		
		this.setPage(this.pages.get(0));
		for(int i = 1; i < this.pages.size(); i++) {
			this.pages.get(i - 1).setNext(this.pages.get(i));
		}
		
		this.pages.clear();
	}
}
