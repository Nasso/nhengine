package io.github.nasso.nhengine.ui.control;

import java.util.function.Consumer;

import org.joml.Vector4f;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.Nhutils;

public class UIButton extends UITextComponent {
	private Texture2D image;
	
	private Color color = null;
	private Color hoverColor = null;
	private Color pressedColor = null;
	
	private float imageSize = 24;
	private float imageTextMargin = 2;
	
	private float borderSize = USE_THEME_VALUE;
	private float borderRadius = UIComponent.USE_THEME_VALUE;
	private Color borderColor = null;
	
	private FloatTransition hoverTransition = new FloatTransition(0.1f, this::setTransitionValue, 0);
	private FloatTransition pressTransition = new FloatTransition(0.1f, this::setTransitionValue, 0);
	
	private Consumer<UIButton> onAction;
	
	private Vector4f _rgba = new Vector4f();
	
	public UIButton(CharSequence text) {
		this(text, null);
	}
	
	public UIButton(Texture2D image) {
		this(null, null, image);
	}
	
	public UIButton(Texture2D image, Consumer<UIButton> onAction) {
		this(null, onAction, image);
	}
	
	public UIButton(CharSequence text, Consumer<UIButton> onAction) {
		this(text, onAction, null);
	}
	
	public UIButton(CharSequence text, Consumer<UIButton> onAction, Texture2D image) {
		super(text);
		this.onAction = onAction;
		this.image = image;
		this.setPadding(10);
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		if(btn == Nhengine.MOUSE_BUTTON_LEFT) {
			this.pressTransition.setTargetValue(1);
		}
		return true;
	}
	
	public boolean mouseButtonReleased(float x, float y, int btn) {
		if(btn == Nhengine.MOUSE_BUTTON_LEFT) {
			this.pressTransition.setTargetValue(0);
		}
		return true;
	}
	
	public boolean mouseClicked(int btn) {
		if(this.onAction != null && btn == Nhengine.MOUSE_BUTTON_LEFT) this.onAction.accept(this);
		return true;
	}
	
	public boolean mouseEntered(float newX, float newY, float relX, float relY) {
		this.hoverTransition.setTargetValue(1);
		return true;
	}
	
	public boolean mouseExited(float newX, float newY, float relX, float relY) {
		this.hoverTransition.setTargetValue(0);
		this.pressTransition.setTargetValue(0);
		return true;
	}
	
	public void setTransitionValue(float value) {
		this.repaint();
	}
	
	private void updateColor() {
		Color normal = this.getColor();
		Color hover = this.getHoverColor();
		Color pressed = this.getPressedColor();
		
		if(normal == null) return;
		
		Nhutils.colorBlend(normal, hover, this.hoverTransition.getValue(), this._rgba);
		Nhutils.colorBlend(this._rgba, pressed, this.pressTransition.getValue(), this._rgba);
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		float borderRadius = Math.min(this.getBorderRadius(), Math.min(this.getWidth() / 2f, this.getHeight() / 2f));
		
		gtx.setFill(this.getBorderColor());
		gtx.fillRoundedRect(0, 0, this.getWidth(), this.getHeight(), borderRadius);
		
		this.updateColor();
		
		float borderSize = this.pressTransition.getValue() * this.getBorderSize();
		float faceWidth = this.getWidth() - 2;
		float faceHeight = this.getHeight() - this.getBorderSize() - 2;
		gtx.translate(1, borderSize + 1);
		
		gtx.setFill(this._rgba);
		gtx.fillRoundedRect(0, 0, faceWidth, faceHeight, borderRadius);
		
		if(this.image != null) {
			float imgW = this.image.getWidth();
			float imgH = this.image.getHeight();
			float imgR = imgW / imgH;
			
			gtx.drawImage(this.image, this.getPaddingLeft() - 1, faceHeight / 2 - this.imageSize / 2, this.imageSize * imgR, this.imageSize);
		}
		
		if(this.getText() != null && this.getText().length() > 0) {
			gtx.setFont(this.getFont());
			gtx.setFill(this.getTextColor());
			
			gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
			
			gtx.setTextAlign(TextAlignment.CENTER);
			gtx.fillText(this.getText().toString(), faceWidth / 2, faceHeight / 2);
		}
	}
	
	public void computePreferredSize() {
		this.preferredSize.zero();
		
		if(this.getFont() != null && this.getText() != null) {
			Nhutils.measureText(this.getFont(), this.getText(), this.preferredSize);
		}
		
		if(this.image != null) {
			float imgW = this.image.getWidth();
			float imgH = this.image.getHeight();
			float imgR = imgW / imgH;
			
			this.preferredSize.x += this.imageSize * imgR;
			this.preferredSize.y = Math.max(this.preferredSize.y, this.imageSize);
		}
		
		if(this.image != null && this.getText() != null && this.getFont() != null) {
			this.preferredSize.x += this.imageTextMargin;
		}
		
		this.preferredSize.add(this.getPaddingLeft() + this.getPaddingRight(), this.getPaddingTop() + this.getPaddingBottom() + this.getBorderSize());
	}
	
	public float getBorderRadius() {
		return Float.isNaN(this.borderRadius) && this.getTheme() != null ? this.getTheme().getButtonBorderRadius() : this.borderRadius;
	}
	
	public void setBorderRadius(float borderRadius) {
		this.borderRadius = borderRadius;
	}
	
	public float getBorderSize() {
		return Float.isNaN(this.borderSize) && this.getTheme() != null ? this.getTheme().getButtonBorderSize() : this.borderSize;
	}
	
	public void setBorderSize(float borderSize) {
		this.borderSize = borderSize;
	}
	
	public Color getBorderColor() {
		return this.borderColor == null && this.getTheme() != null ? this.getTheme().getButtonBorderColor() : this.borderColor;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public Color getColor() {
		return this.color == null && this.getTheme() != null ? this.getTheme().getButtonColor() : this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getHoverColor() {
		return this.hoverColor == null && this.getTheme() != null ? this.getTheme().getButtonHoverColor() : this.hoverColor;
	}
	
	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}
	
	public Color getPressedColor() {
		return this.pressedColor == null && this.getTheme() != null ? this.getTheme().getButtonPressedColor() : this.pressedColor;
	}
	
	public void setPressedColor(Color pressedColor) {
		this.pressedColor = pressedColor;
	}
	
	public Consumer<UIButton> getOnAction() {
		return this.onAction;
	}
	
	public void setOnAction(Consumer<UIButton> onAction) {
		this.onAction = onAction;
	}
	
	public Texture2D getImage() {
		return this.image;
	}
	
	public void setImage(Texture2D image) {
		this.image = image;
	}
	
	public float getImageSize() {
		return this.imageSize;
	}
	
	public void setImageSize(float imageSize) {
		this.imageSize = imageSize;
	}
	
	public float getImageTextMargin() {
		return this.imageTextMargin;
	}
	
	public void setImageTextMargin(float imageTextMargin) {
		this.imageTextMargin = imageTextMargin;
	}
}
