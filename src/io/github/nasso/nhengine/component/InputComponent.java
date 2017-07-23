package io.github.nasso.nhengine.component;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.InputComponentEventHandler;
import io.github.nasso.nhengine.event.InputHandler;
import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.utils.MathUtils;

public class InputComponent extends Component {
	private InputComponentEventHandler inputHandler;
	private float inputAreaW, inputAreaH;
	
	public InputComponent(float inputAreaWidth, float inputAreaHeight, InputComponentEventHandler handler) {
		this.inputAreaW = inputAreaWidth;
		this.inputAreaH = inputAreaHeight;
		this.setInputHandler(handler);
		
		// TODO: Change that (it's ugly design)
		// Instead, make the call in the Game class, when looping over the components
		GameWindow win = Game.instance().window();
		win.registerInputHandler(new InputHandler() {
			private Vector3f _vec3 = new Vector3f();
			private Vector2f _vec2 = new Vector2f();
			
			private boolean[] isPressed = new boolean[Nhengine.MOUSE_BUTTON_LAST + 1];
			private float[] pressX = new float[Nhengine.MOUSE_BUTTON_LAST + 1];
			private float[] pressY = new float[Nhengine.MOUSE_BUTTON_LAST + 1];
			
			public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponent.this.getWorldPosition(this._vec3);
				InputComponent.this.getWorldScale(this._vec2);
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				float boxX = this._vec3.x;
				float boxY = this._vec3.y;
				float boxW = InputComponent.this.inputAreaW * this._vec2.x;
				float boxH = InputComponent.this.inputAreaH * this._vec2.y;
				
				if(handler != null && MathUtils.boxContains(x, y, boxX, boxY, boxW, boxH)) {
					handler.mouseWheelMoved(x, y, scrollX, scrollY);
				}
			}
			
			public void mouseMoved(float newX, float newY, float relX, float relY) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponent.this.getWorldPosition(this._vec3);
				InputComponent.this.getWorldScale(this._vec2);
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				float boxX = this._vec3.x;
				float boxY = this._vec3.y;
				float boxW = InputComponent.this.inputAreaW * this._vec2.x;
				float boxH = InputComponent.this.inputAreaH * this._vec2.y;
				
				if(handler != null) {
					if(MathUtils.boxContains(newX, newY, boxX, boxY, boxW, boxH)) {
						if(!MathUtils.boxContains(newX - relX, newY - relY, boxX, boxY, boxW, boxH)) {
							handler.mouseEntered(newX - boxX, newY - boxY, relX, relY);
							return;
						}
						
						for(int i = Nhengine.MOUSE_BUTTON_1; i <= Nhengine.MOUSE_BUTTON_LAST; i++) {
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
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponent.this.getWorldPosition(this._vec3);
				InputComponent.this.getWorldScale(this._vec2);
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				float boxX = this._vec3.x;
				float boxY = this._vec3.y;
				float boxW = InputComponent.this.inputAreaW * this._vec2.x;
				float boxH = InputComponent.this.inputAreaH * this._vec2.y;
				
				if(handler != null && MathUtils.boxContains(x, y, boxX, boxY, boxW, boxH)) {
					this.isPressed[btn] = false;
					
					handler.mouseButtonReleased(x, y, btn);
				}
			}
			
			public void mouseButtonPressed(float x, float y, int btn) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponent.this.getWorldPosition(this._vec3);
				InputComponent.this.getWorldScale(this._vec2);
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				float boxX = this._vec3.x;
				float boxY = this._vec3.y;
				float boxW = InputComponent.this.inputAreaW * this._vec2.x;
				float boxH = InputComponent.this.inputAreaH * this._vec2.y;
				
				if(handler != null && MathUtils.boxContains(x, y, boxX, boxY, boxW, boxH)) {
					this.isPressed[btn] = true;
					this.pressX[btn] = x;
					this.pressY[btn] = y;
					
					handler.mouseButtonPressed(x, y, btn);
				}
			}
			
			public void keyReleased(int key, int scancode) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				
				if(handler != null) handler.keyReleased(key, scancode);
			}
			
			public void keyPressed(int key, int scancode) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				if(handler != null) handler.keyPressed(key, scancode);
			}
			
			public void keyTyped(int key, int scancode) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				if(handler != null) handler.keyTyped(key, scancode);
			}
			
			public void textInput(int codepoint) {
				if(!InputComponent.this.isEnabled()) return;
				
				InputComponentEventHandler handler = InputComponent.this.getInputHandler();
				
				if(handler != null) handler.textInput(codepoint);
			}
			
		});
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
