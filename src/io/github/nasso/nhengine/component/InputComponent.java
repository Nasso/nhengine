package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.InputEventHandler;
import io.github.nasso.nhengine.level.Component;

/**
 * A component that can react to user input. Mouse inputs are trigerred only if they're in the specified rectangle.
 * 
 * @author nasso
 */
public class InputComponent extends Component {
	private boolean[] isPressed = new boolean[Nhengine.MOUSE_BUTTON_LAST + 1];
	
	private InputEventHandler inputHandler;
	private float inputAreaW, inputAreaH;
	
	private boolean mouseHover = false;
	
	/**
	 * Constructs an input component with the specified size. The rectangle's top-left corner is at the origin (0; 0) of the component.
	 * 
	 * @param inputAreaWidth
	 *            The input rectangle width
	 * @param inputAreaHeight
	 *            The input rectangle height
	 * @param handler
	 *            The event handler
	 */
	public InputComponent(float inputAreaWidth, float inputAreaHeight, InputEventHandler handler) {
		this.inputAreaW = inputAreaWidth;
		this.inputAreaH = inputAreaHeight;
		this.setInputHandler(handler);
	}
	
	public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.mouseWheelMoved(x, y, scrollX, scrollY);
	}
	
	public void mouseEntered(float newX, float newY, float relX, float relY) {
		this.mouseHover = true;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.mouseEntered(newX, newY, relX, relY);
	}
	
	public void mouseExited(float newX, float newY, float relX, float relY) {
		this.mouseHover = false;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.mouseExited(newX, newY, relX, relY);
	}
	
	public void mouseMoved(float newX, float newY, float relX, float relY) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
		if(handler != null) {
			for(int i = 0; i < this.isPressed.length; i++) {
				if(this.isPressed[i]) {
					handler.mouseDragged(newX, newY, relX, relY, i);
				}
			}
			
			handler.mouseMoved(newX, newY, relX, relY);
		}
	}
	
	public void mouseButtonReleased(float x, float y, int btn) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) {
			this.isPressed[btn] = false;
			handler.mouseButtonReleased(x, y, btn);
		}
	}
	
	public void mouseButtonPressed(float x, float y, int btn) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
		if(handler != null) {
			this.isPressed[btn] = true;
			
			handler.mouseButtonPressed(x, y, btn);
		}
	}
	
	public void keyReleased(int key) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
		if(handler != null) handler.keyReleased(key);
	}
	
	public void keyPressed(int key) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.keyPressed(key);
	}
	
	public void keyTyped(int key) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.keyTyped(key);
	}
	
	public void textInput(int codepoint) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
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
	
	public InputEventHandler getInputHandler() {
		return this.inputHandler;
	}
	
	public void setInputHandler(InputEventHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	public boolean isMouseHover() {
		return this.mouseHover;
	}
}
