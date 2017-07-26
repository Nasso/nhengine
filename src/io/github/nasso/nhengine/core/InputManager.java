package io.github.nasso.nhengine.core;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import io.github.nasso.nhengine.component.InputComponent;
import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.utils.MathUtils;

public class InputManager {
	private List<InputComponent> components = new ArrayList<InputComponent>();
	
	private Matrix4f invWorld = new Matrix4f();
	
	public InputManager() {
	}
	
	private float transformX(float x, float y, Matrix4fc xform) {
		// transform (x, y, 0, 1) with the matrix, return only the resulting x component
		/*
		        S S S S
		        r r r r
		        c c c c
		
		        x y z w
		        " " " "
		Dest x: 1 0 0 0 <-
		Dest y: 0 1 0 0
		Dest z: 0 0 1 0
		Dest w: 0 0 0 1
		*/
		
		return x * xform.m00() + y * xform.m10() + xform.m30();
	}
	
	private float transformY(float x, float y, Matrix4fc xform) {
		return x * xform.m01() + y * xform.m11() + xform.m31();
	}
	
	private void prepareNode(Node root) {
		for(int ic = 0; ic < root.getComponents().size(); ic++) {
			Component c = root.getComponents().get(ic);
			if(!c.isEnabled()) continue;
			
			if(c instanceof InputComponent) {
				this.components.add((InputComponent) c);
			}
		}
		
		for(int i = 0; i < root.getChildren().size(); i++) {
			this.prepareNode(root.getChildren().get(i));
		}
	}
	
	public void processInput(Level lvl) {
		this.components.clear();
		
		for(int i = 0; i < lvl.getOverlayScenes().size(); i++) {
			this.prepareNode(lvl.getOverlayScene(i).getRoot());
		}
		
		GameWindow win = Game.instance().window();
		
		for(int ic = 0; ic < this.components.size(); ic++) {
			Component c = this.components.get(ic);
			if(!c.isEnabled()) continue;
			
			if(c instanceof InputComponent) {
				InputComponent in = (InputComponent) c;
				in.getWorldMatrix(true).invert(this.invWorld);
				
				// - MOUSE - Affected only if in the area
				// Buttons
				List<Integer> mouseInputs = win.getLastMouseInputs();
				for(int i = 0; i < mouseInputs.size();) {
					int action = mouseInputs.get(i++);
					int x = mouseInputs.get(i++);
					int y = mouseInputs.get(i++);
					int button = mouseInputs.get(i++);
					
					float tx = this.transformX(x, y, this.invWorld);
					float ty = this.transformY(x, y, this.invWorld);
					if(!MathUtils.boxContains(tx, ty, 0, 0, in.getInputAreaWidth(), in.getInputAreaHeight())) continue;
					
					switch(action) {
						case Nhengine.PRESS:
							in.mouseButtonPressed(tx, ty, button);
							break;
						case Nhengine.RELEASE:
							in.mouseButtonReleased(tx, ty, button);
							break;
					}
				}
				
				float relX = win.getMouseRelX();
				float relY = win.getMouseRelY();
				
				// Movement
				if(relX != 0 || relY != 0) {
					float tx = this.transformX(win.getMouseX(), win.getMouseY(), this.invWorld);
					float ty = this.transformY(win.getMouseX(), win.getMouseY(), this.invWorld);
					
					float toldx = this.transformX(win.getMouseX() - win.getMouseRelX(), win.getMouseY() - win.getMouseRelY(), this.invWorld);
					float toldy = this.transformY(win.getMouseX() - win.getMouseRelX(), win.getMouseY() - win.getMouseRelY(), this.invWorld);
					
					if(MathUtils.boxContains(tx, ty, 0, 0, in.getInputAreaWidth(), in.getInputAreaHeight())) {
						if(!in.isMouseOver()) {
							in.mouseEntered(tx, ty, tx - toldx, ty - toldy);
						}
						
						in.mouseMoved(tx, ty, tx - toldx, ty - toldy);
					} else if(in.isMouseOver()) {
						in.mouseExited(tx, ty, tx - toldx, ty - toldy);
					}
				}
				
				// Wheel
				if(win.getScrollRelX() != 0 || win.getScrollRelY() != 0) {
					float tx = this.transformX(win.getMouseX(), win.getMouseY(), this.invWorld);
					float ty = this.transformY(win.getMouseX(), win.getMouseY(), this.invWorld);
					if(!MathUtils.boxContains(tx, ty, 0, 0, in.getInputAreaWidth(), in.getInputAreaHeight())) continue;
					
					in.mouseWheelMoved(tx, ty, win.getScrollRelX(), win.getScrollRelY());
				}
				
				// - KEYBOARD - Always trigerred
				// Keys
				List<Integer> keyInputs = win.getLastKeyInputs();
				for(int i = 0; i < keyInputs.size();) {
					int action = keyInputs.get(i++);
					int key = keyInputs.get(i++);
					
					switch(action) {
						case Nhengine.PRESS:
							in.keyPressed(key);
							// Pressing == typing, but typing != pressing
						case Nhengine.REPEAT:
							in.keyTyped(key);
							break;
						case Nhengine.RELEASE:
							in.keyReleased(key);
							break;
					}
				}
				
				// Text input
				List<Integer> textInputs = win.getLastTextInputs();
				for(int i = 0; i < textInputs.size(); i++) {
					in.textInput(textInputs.get(i));
				}
			}
		}
	}
}
