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
	
	private boolean mouseOver = false;
	
	/**
	 * Constructs an input component with the specified size. The rectangle's top-left corner is at the origin (0; 0) of the component.
	 * 
	 * @param inputAreaWidth
	 *            The input rectangle width.
	 * @param inputAreaHeight
	 *            The input rectangle height.
	 * @param handler
	 *            The event handler.
	 */
	public InputComponent(float inputAreaWidth, float inputAreaHeight, InputEventHandler handler) {
		this.inputAreaW = inputAreaWidth;
		this.inputAreaH = inputAreaHeight;
		this.setInputHandler(handler);
	}
	
	/**
	 * Triggers a "mouse wheel" event.<br>
	 * This is called automatically.
	 * 
	 * @param x
	 *            The <code>x</code> location of the event, in local space.
	 * @param y
	 *            The <code>y</code> location of the event, in local space.
	 * @param scrollX
	 *            The amount of horizontal scroll.
	 * @param scrollY
	 *            The amount of vertical scroll.
	 */
	public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.mouseWheelMoved(x, y, scrollX, scrollY);
	}
	
	/**
	 * Triggers a "mouse entered" event.<br>
	 * This is called automatically.
	 * 
	 * @param newX
	 *            The new <code>x</code> position of the mouse, in local space.
	 * @param newY
	 *            The new <code>y</code> position of the mouse, in local space.
	 * @param relX
	 *            The new <code>x</code> position of the mouse, relative to its last position.
	 * @param relY
	 *            The new <code>y</code> position of the mouse, relative to its last position.
	 */
	public void mouseEntered(float newX, float newY, float relX, float relY) {
		this.mouseOver = true;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.mouseEntered(newX, newY, relX, relY);
	}
	
	/**
	 * Triggers a "mouse exited" event.<br>
	 * This is called automatically.
	 * 
	 * @param newX
	 *            The new <code>x</code> position of the mouse, in local space.
	 * @param newY
	 *            The new <code>y</code> position of the mouse, in local space.
	 * @param relX
	 *            The new <code>x</code> position of the mouse, relative to its last position.
	 * @param relY
	 *            The new <code>y</code> position of the mouse, relative to its last position.
	 */
	public void mouseExited(float newX, float newY, float relX, float relY) {
		this.mouseOver = false;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.mouseExited(newX, newY, relX, relY);
	}
	
	/**
	 * Triggers a "mouse moved" event.<br>
	 * This is called automatically.
	 * 
	 * @param newX
	 *            The new <code>x</code> position of the mouse, in local space.
	 * @param newY
	 *            The new <code>y</code> position of the mouse, in local space.
	 * @param relX
	 *            The new <code>x</code> position of the mouse, relative to its last position.
	 * @param relY
	 *            The new <code>y</code> position of the mouse, relative to its last position.
	 */
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
	
	/**
	 * Triggers a "mouse button released" event.<br>
	 * This is called automatically.
	 * 
	 * @param x
	 *            The <code>x</code> position of the event, in local space.
	 * @param y
	 *            The <code>y</code> position of the event, in local space.
	 * @param btn
	 *            The released button, one of:
	 *            <ul>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_1 MOUSE_BUTTON_1/MOUSE_BUTTON_LEFT}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_2 MOUSE_BUTTON_2/MOUSE_BUTTON_RIGHT}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_3 MOUSE_BUTTON_3/MOUSE_BUTTON_MIDDLE}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_4 MOUSE_BUTTON_4}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_5 MOUSE_BUTTON_5}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_6 MOUSE_BUTTON_6}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_7 MOUSE_BUTTON_7}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_8 MOUSE_BUTTON_8}</li>
	 *            </ul>
	 */
	public void mouseButtonReleased(float x, float y, int btn) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) {
			this.isPressed[btn] = false;
			handler.mouseButtonReleased(x, y, btn);
		}
	}
	
	/**
	 * Triggers a "mouse button pressed" event.<br>
	 * This is called automatically.
	 * 
	 * @param x
	 *            The <code>x</code> position of the event, in local space.
	 * @param y
	 *            The <code>y</code> position of the event, in local space.
	 * @param btn
	 *            The pressed button, one of:
	 *            <ul>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_1 MOUSE_BUTTON_1/MOUSE_BUTTON_LEFT}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_2 MOUSE_BUTTON_2/MOUSE_BUTTON_RIGHT}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_3 MOUSE_BUTTON_3/MOUSE_BUTTON_MIDDLE}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_4 MOUSE_BUTTON_4}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_5 MOUSE_BUTTON_5}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_6 MOUSE_BUTTON_6}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_7 MOUSE_BUTTON_7}</li>
	 *            <li>{@link Nhengine#MOUSE_BUTTON_8 MOUSE_BUTTON_8}</li>
	 *            </ul>
	 */
	public void mouseButtonPressed(float x, float y, int btn) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
		if(handler != null) {
			this.isPressed[btn] = true;
			
			handler.mouseButtonPressed(x, y, btn);
		}
	}
	
	/**
	 * Triggers a "key released" event.<br>
	 * This is called automatically.
	 * 
	 * @param key
	 *            The released key code.
	 */
	public void keyReleased(int key) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
		if(handler != null) handler.keyReleased(key);
	}
	
	/**
	 * Triggers a "key pressed" event.<br>
	 * This is called automatically.
	 * 
	 * @param key
	 *            The pressed key code.
	 */
	public void keyPressed(int key) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.keyPressed(key);
	}
	
	/**
	 * Triggers a "key typed" event.<br>
	 * This is called automatically.
	 * 
	 * @param key
	 *            The typed key code.
	 */
	public void keyTyped(int key) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		if(handler != null) handler.keyTyped(key);
	}
	
	/**
	 * Triggers a "text input" event.<br>
	 * This is called automatically.
	 * 
	 * @param codepoint
	 *            The typed codepoint.
	 */
	public void textInput(int codepoint) {
		if(!this.isEnabled()) return;
		
		InputEventHandler handler = this.getInputHandler();
		
		if(handler != null) handler.textInput(codepoint);
	}
	
	/**
	 * @return The width of the input area.
	 */
	public float getInputAreaWidth() {
		return this.inputAreaW;
	}
	
	/**
	 * @param w
	 *            The new width of the input area.
	 */
	public void setInputAreaWidth(float w) {
		this.inputAreaW = w;
	}
	
	/**
	 * @return The height of the input area.
	 */
	public float getInputAreaHeight() {
		return this.inputAreaH;
	}
	
	/**
	 * @param h
	 *            The new height of the input area.
	 */
	public void setInputAreaHeight(float h) {
		this.inputAreaH = h;
	}
	
	/**
	 * @return The current input handler.
	 */
	public InputEventHandler getInputHandler() {
		return this.inputHandler;
	}
	
	/**
	 * @param inputHandler
	 *            The new input handler.
	 */
	public void setInputHandler(InputEventHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	/**
	 * The "over" state of the mouse is changed when the mouse enters and exits this component,
	 * i.e. when {@link #mouseEntered(float, float, float, float)} and {@link #mouseExited(float, float, float, float)} are called.
	 * 
	 * @return True if the mouse is over this component.
	 */
	public boolean isMouseOver() {
		return this.mouseOver;
	}
}
