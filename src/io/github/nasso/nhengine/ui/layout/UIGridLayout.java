package io.github.nasso.nhengine.ui.layout;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;

public class UIGridLayout implements UILayout {
	private int rows = 1;
	private int cols = 0;
	private float hgap = 0;
	private float vgap = 0;
	
	/**
	 * @param rows
	 *            number of rows or 0 for unlimited rows (in this case, cols musn't be 0. If it is anyway, then rows will be 1)
	 * @param cols
	 *            number of cols
	 * @param hgap
	 * @param vgap
	 */
	public UIGridLayout(int rows, int cols, float hgap, float vgap) {
		this.rows = rows == 0 && cols == 0 ? 1 : rows;
		this.cols = cols;
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	public UIGridLayout(int rows, int cols) {
		this(rows, cols, 0, 0);
	}
	
	public void apply(UIContainer parent) {
		int count = parent.getChildrenCount();
		int rows = this.rows == 0 ? (int) Math.ceil((float) count / this.cols) : this.rows;
		int cols = this.cols == 0 ? (int) Math.ceil((float) count / this.rows) : this.cols;
		
		float w = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
		float h = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
		
		float totalGapsWidth = (cols - 1) * this.hgap;
		float widthOnComponent = (w - totalGapsWidth) / cols;
		float extraWidthAvailable = (w - (widthOnComponent * cols + totalGapsWidth)) / 2;
		
		float totalGapsHeight = (rows - 1) * this.vgap;
		float heightOnComponent = (h - totalGapsHeight) / rows;
		float extraHeightAvailable = (h - (heightOnComponent * rows + totalGapsHeight)) / 2;
		
		float x = parent.getPaddingLeft() + extraWidthAvailable;
		float y;
		
		for(int c = 0; c < cols; c++, x += widthOnComponent + this.hgap) {
			y = parent.getPaddingTop() + extraHeightAvailable;
			
			for(int r = 0; r < rows; r++, y += heightOnComponent + this.vgap) {
				int i = r * cols + c;
				if(i < count) {
					parent.getChild(i).setBounds(x, y, widthOnComponent, heightOnComponent);
				}
			}
		}
	}
	
	public void getPreferredSize(UIContainer parent, Vector2f dest) {
		int count = parent.getChildrenCount();
		int rows = this.rows == 0 ? (int) Math.ceil((float) count / this.cols) : this.rows;
		int cols = this.cols == 0 ? (int) Math.ceil((float) count / this.rows) : this.cols;
		
		float w = 0, h = 0;
		
		for(int i = 0; i < count; i++) {
			UIComponent child = parent.getChild(i);
			if(child == null) continue;
			
			w = Math.max(w, child.getPreferredSize().x);
			h = Math.max(h, child.getPreferredSize().y);
		}
		
		dest.set(w * cols + parent.getPaddingLeft() + parent.getPaddingRight() + (cols - 1) * this.hgap, h * rows + parent.getPaddingTop() + parent.getPaddingBottom() + (rows - 1) * this.vgap);
	}
	
	public void addComponent(int index, UIComponent comp, Object constraints) {
		// constraints aren't used
		
		// ...
		
		// we don't have to do anything here...
	}
	
	public void removeAllComponents() {
		// beware of the man who speaks in hands
	}
	
	public void removeComponent(UIComponent comp) {
		// Howdy!
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public void setRows(int rows) {
		if(rows == 0 && this.cols == 0) return;
		this.rows = rows;
	}
	
	public int getCols() {
		return this.cols;
	}
	
	public void setCols(int cols) {
		if(cols == 0 && this.rows == 0) return;
		this.cols = cols;
	}
	
	public float getHGap() {
		return this.hgap;
	}
	
	public void setHGap(float hgap) {
		this.hgap = hgap;
	}
	
	public float getVGap() {
		return this.vgap;
	}
	
	public void setVGap(float vgap) {
		this.vgap = vgap;
	}
}
