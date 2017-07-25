package io.github.nasso.nhengine.component;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.InputComponentEventHandler;
import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.utils.MathUtils;

public class InputComponent extends Component {
	private Vector3f _vec3 = new Vector3f();
	private Vector2f _vec2 = new Vector2f();
	
	private boolean[] isPressed = new boolean[Nhengine.MOUSE_BUTTON_LAST + 1];
	private float[] pressX = new float[Nhengine.MOUSE_BUTTON_LAST + 1];
	private float[] pressY = new float[Nhengine.MOUSE_BUTTON_LAST + 1];
	
	private InputComponentEventHandler inputHandler;
	private float inputAreaW, inputAreaH;
	
	public InputComponent(float inputAreaWidth, float inputAreaHeight, InputComponentEventHandler handler) {
		this.inputAreaW = inputAreaWidth;
		this.inputAreaH = inputAreaHeight;
		this.setInputHandler(handler);
	}
	
	public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		if(!this.isEnabled()) return;
		
		this.getWorldPosition(this._vec3);
		this.getWorldScale(this._vec2);
		
		InputComponentEventHandler handler = this.getInputHandler();
		float boxX = this._vec3.x;
		float boxY = this._vec3.y;
		float boxW = this.inputAreaW * this._vec2.x;
		float boxH = this.inputAreaH * this._vec2.y;
		
		if(handler != null && MathUtils.boxContains(x, y, boxX, boxY, boxW, boxH)) {
			handler.mouseWheelMoved(x, y, scrollX, scrollY);
		}
	}
	
	public void mouseMoved(float newX, float newY, float relX, float relY) {
		if(!this.isEnabled()) return;
		
		this.getWorldPosition(this._vec3);
		this.getWorldScale(this._vec2);
		
		InputComponentEventHandler handler = this.getInputHandler();
		float boxX = this._vec3.x;
		float boxY = this._vec3.y;
		float boxW = this.inputAreaW * this._vec2.x;
		float boxH = this.inputAreaH * this._vec2.y;
		
		if(handler != null) {
			if(MathUtils.boxContains(newX, newY, boxX, boxY, boxW, boxH)) {
				if(!MathUtils.boxContains(newX - relX, newY - relY, boxX, boxY, boxW, boxH)) {
					handler.mouseEntered(newX - boxX, newY - boxY, relX, relY);
					return;
				}
				
				for(int i = 0; i < this.isPressed.length; i++) {
					if(this.isPressed[i]) {
						handler.mouseDragged(newX - this.pressX[i], newY - this.pressY[i], relX, relY, i);
					}
				}
				
				handler.mouseMoved(newX - boxX, newY - boxY, relX, relY);
			} else if(MathUtils.boxContains(newX - relX, newY - relY, boxX, boxY, boxW, boxH)) {
				handler.mouseExited(newX - boxX, newY - boxY, relX, relY);
			}
		}
	}
	
	public void mouseButtonReleased(float x, float y, int btn) {
		if(!this.isEnabled()) return;
		
		this.getWorldPosition(this._vec3);
		this.getWorldScale(this._vec2);
		
		InputComponentEventHandler handler = this.getInputHandler();
		float boxX = this._vec3.x;
		float boxY = this._vec3.y;
		float boxW = this.inputAreaW * this._vec2.x;
		float boxH = this.inputAreaH * this._vec2.y;
		
		if(handler != null && MathUtils.boxContains(x, y, boxX, boxY, boxW, boxH)) {
			this.isPressed[btn] = false;
			
			handler.mouseButtonReleased(x, y, btn);
		}
	}
	
	public void mouseButtonPressed(float x, float y, int btn) {
		if(!this.isEnabled()) return;
		
		this.getWorldPosition(this._vec3);
		this.getWorldScale(this._vec2);
		
		InputComponentEventHandler handler = this.getInputHandler();
		float boxX = this._vec3.x;
		float boxY = this._vec3.y;
		float boxW = this.inputAreaW * this._vec2.x;
		float boxH = this.inputAreaH * this._vec2.y;
		
		if(handler != null && MathUtils.boxContains(x, y, boxX, boxY, boxW, boxH)) {
			this.isPressed[btn] = true;
			this.pressX[btn] = x;
			this.pressY[btn] = y;
			
			handler.mouseButtonPressed(x, y, btn);
		}
	}
	
	public void keyReleased(int key) {
		if(!this.isEnabled()) return;
		
		InputComponentEventHandler handler = this.getInputHandler();
		
		if(handler != null) handler.keyReleased(key);
	}
	
	public void keyPressed(int key) {
		if(!this.isEnabled()) return;
		
		InputComponentEventHandler handler = this.getInputHandler();
		if(handler != null) handler.keyPressed(key);
	}
	
	public void keyTyped(int key) {
		if(!this.isEnabled()) return;
		
		InputComponentEventHandler handler = this.getInputHandler();
		if(handler != null) handler.keyTyped(key);
	}
	
	public void textInput(int codepoint) {
		if(!this.isEnabled()) return;
		
		InputComponentEventHandler handler = this.getInputHandler();
		
		if(handler != null) handler.textInput(codepoint);
	}
	
	public float getInputAreaWidth() {
		return this.inputAreaW;
	}
	
	public void setInputAreaWidth(float w) {
		this.inputAreaW = w;
	}
	
	public float getInputAreaHeight() {
		return this.inputAreaH;
	}
	
	public void setInputAreaHeight(float h) {
		this.inputAreaH = h;
	}
	
	public InputComponentEventHandler getInputHandler() {
		return this.inputHandler;
	}
	
	public void setInputHandler(InputComponentEventHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
}
