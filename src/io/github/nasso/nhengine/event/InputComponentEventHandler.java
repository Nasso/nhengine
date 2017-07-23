package io.github.nasso.nhengine.event;

public interface InputComponentEventHandler extends InputHandler {
	public void mouseEntered(float x, float y, float relX, float relY);
	
	public void mouseExited(float x, float y, float relX, float relY);
	
	public void mouseDragged(float x, float y, float relX, float relY, int button);
}
