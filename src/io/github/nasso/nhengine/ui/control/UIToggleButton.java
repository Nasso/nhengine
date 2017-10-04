package io.github.nasso.nhengine.ui.control;

import java.util.function.Consumer;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.MathUtils;
import io.github.nasso.nhengine.utils.Nhutils;
import penner.easing.Quad;

public class UIToggleButton extends UITextComponent {
	public static final float DOT_SIZE = 24;
	public static final float TEXT_MARGIN = 16;
	
	private UIToggleButtonGroup group;
	
	private Color color = null;
	private Color selectedColor = null;
	private Color unselectedColor = null;
	
	private float roundness = USE_THEME_VALUE;
	
	private boolean selected = false;
	
	private FloatTransition toggleTransition;
	
	private Consumer<UIToggleButton> onAction;
	
	public UIToggleButton() {
		this(null);
	}
	
	public UIToggleButton(CharSequence text) {
		this(text, null, null);
	}
	
	public UIToggleButton(CharSequence text, UIToggleButtonGroup group) {
		this(text, null, group);
	}
	
	public UIToggleButton(CharSequence text, Consumer<UIToggleButton> onAction) {
		this(text, onAction, false);
	}
	
	public UIToggleButton(CharSequence text, Consumer<UIToggleButton> onAction, boolean selected) {
		this(text, onAction, null, selected);
	}
	
	public UIToggleButton(CharSequence text, Consumer<UIToggleButton> onAction, UIToggleButtonGroup group) {
		this(text, onAction, group, false);
	}
	
	public UIToggleButton(CharSequence text, Consumer<UIToggleButton> onAction, UIToggleButtonGroup group, boolean selected) {
		super(text);
		this.onAction = onAction;
		this.selected = selected;
		this.group = group;
		
		if(this.group != null) {
			this.group.add(this);
		}
		
		this.toggleTransition = new FloatTransition(0.2f, this::setTransitionValue, 0, Quad::easeInOut);
	}
	
	public void setTransitionValue(float value) {
		this.repaint();
	}
	
	public boolean mouseClicked(int btn) {
		if(btn != Nhengine.MOUSE_BUTTON_LEFT) return true;
		
		this.setSelected(!this.selected);
		if(this.onAction != null) this.onAction.accept(this);
		return true;
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		float roundness = MathUtils.clamp(this.getRoundness(), 0.0f, 0.5f);
		
		float btnW = DOT_SIZE * 2;
		float btnH = DOT_SIZE;
		
		float dotSize = btnH - 4;
		
		gtx.save();
		gtx.translate(0, this.getHeight() / 2f - btnH / 2f);
		if(!this.toggleTransition.isFinished()) {
			float per = this.toggleTransition.getValue();
			
			// Selecting or unselecting
			gtx.setFill(Nhutils.colorBlend(this.getUnselectedColor(), this.getSelectedColor(), per));
			gtx.fillRoundedRect(0, 0, btnW, btnH, btnH * roundness);
			
			gtx.setFill(this.getColor());
			gtx.fillRoundedRect(2 + per * (btnW - dotSize - 4), 2, dotSize, dotSize, dotSize * roundness);
		} else if(this.selected) {
			// Selected
			gtx.setFill(this.getSelectedColor());
			gtx.fillRoundedRect(0, 0, btnW, btnH, btnH * roundness);
			
			gtx.setFill(this.getColor());
			gtx.fillRoundedRect(btnW - dotSize - 2, 2, dotSize, dotSize, dotSize * roundness);
		} else {
			// Not selected
			gtx.setFill(this.getUnselectedColor());
			gtx.fillRoundedRect(0, 0, btnW, btnH, btnH * roundness);
			
			gtx.setFill(this.getColor());
			gtx.fillRoundedRect(2, 2, dotSize, dotSize, dotSize * roundness);
		}
		gtx.restore();
		
		if(this.getText() != null) {
			gtx.setFill(this.getTextColor());
			gtx.setFont(this.getFont());
			gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
			gtx.fillText(this.getText().toString(), btnW + TEXT_MARGIN, this.getHeight() / 2f);
		}
	}
	
	public void computePreferredSize() {
		this.preferredSize.zero();
		
		if(this.getText() != null && this.getFont() != null) {
			Nhutils.measureText(this.getFont(), this.getText(), this.preferredSize);
			this.preferredSize.x += TEXT_MARGIN;
		}
		
		this.preferredSize.add(DOT_SIZE * 2f, DOT_SIZE);
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void setSelected(boolean selected) {
		if(this.getGroup() != null && !this.getGroup().select(this, selected)) return;
		this.forceSelect(selected);
	}
	
	public Color getColor() {
		return this.getTheme().getColor("toggleButton.color", this.color);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getSelectedColor() {
		return this.getTheme().getColor("toggleButton.selectedColor", this.selectedColor);
	}
	
	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}
	
	public Color getUnselectedColor() {
		return this.getTheme().getColor("toggleButton.unselectedColor", this.unselectedColor);
	}
	
	public void setUnselectedColor(Color unselectedColor) {
		this.unselectedColor = unselectedColor;
	}
	
	public Consumer<UIToggleButton> getOnAction() {
		return this.onAction;
	}
	
	public void setOnAction(Consumer<UIToggleButton> onAction) {
		this.onAction = onAction;
	}
	
	public float getRoundness() {
		return this.getTheme().getFloat("toggleButton.roundness", this.roundness);
	}
	
	public void setRoundness(float roundness) {
		this.roundness = roundness;
	}
	
	public UIToggleButtonGroup getGroup() {
		return this.group;
	}
	
	void setGroup(UIToggleButtonGroup group) {
		this.group = group;
	}
	
	void forceSelect(boolean selected) {
		this.selected = selected;
		this.toggleTransition.setTargetValue(this.selected ? 1 : 0);
	}
}
