package io.github.nasso.test.demo;

import static java.lang.Math.*;

import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.control.UIDialog;
import io.github.nasso.nhengine.ui.control.UILabel;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;

public class DemoPerfDialog extends UIDialog {
	private UILabel fpsLabel, deltaLabel;
	
	public DemoPerfDialog() {
		super("Performances", false);
		
		this.fpsLabel = new UILabel("FPS: nope", UILabel.ANCHOR_LEFT);
		this.deltaLabel = new UILabel("Delta: nope", UILabel.ANCHOR_LEFT);
		
		UIContainer content = this.getContent();
		content.setPadding(16);
		content.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL, 4));
		content.add(this.fpsLabel);
		content.add(this.deltaLabel);
	}
	
	public void update(float delta, float fps) {
		this.fpsLabel.setText("FPS: " + floor(fps));
		this.deltaLabel.setText("Delta: " + floor(delta));
	}
}
