package io.github.nasso.nhengine.event;

public interface InputHandler {
	public void textInput(int codepoint);
	
	public void keyPressed(int key, int scancode);
	
	public void keyTyped(int key, int scancode);
	
	public void keyReleased(int key, int scancode);
	
	public void mouseButtonPressed(float x, float y, int btn);
	
	public void mouseButtonReleased(float x, float y, int btn);
	
	public void mouseMoved(float newX, float newY, float relX, float relY);
	
	public void mouseWheelMoved(float x, float y, float scrollX, float scrollY);
}
