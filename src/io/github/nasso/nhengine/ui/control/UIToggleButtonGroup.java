package io.github.nasso.nhengine.ui.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.github.nasso.nhengine.utils.MathUtils;

public class UIToggleButtonGroup {
	private List<UIToggleButton> toggleButtons = new ArrayList<UIToggleButton>();
	
	private List<UIToggleButton> selectedButtons = new LinkedList<UIToggleButton>();
	
	private int minSelected = 0;
	private int maxSelected = Integer.MAX_VALUE;
	
	public UIToggleButtonGroup() {
		this(1, 1);
	}
	
	public UIToggleButtonGroup(int min, int max) {
		this.setMinSelected(min);
		this.setMaxSelected(max);
	}
	
	public boolean select(UIToggleButton btn, boolean selected) {
		if(selected) {
			while(this.selectedButtons.size() >= this.maxSelected) {
				this.selectedButtons.remove(0).forceSelect(false);
			}
			
			return this.selectedButtons.add(btn);
		}
		
		if(this.selectedButtons.size() <= this.minSelected) return false;
		return this.selectedButtons.remove(btn);
	}
	
	public void removeAll() {
		for(int i = 0; i < this.toggleButtons.size(); i++) {
			this.toggleButtons.get(i).setGroup(null);
		}
		
		this.toggleButtons.clear();
	}
	
	public void add(UIToggleButton... tbtn) {
		for(int i = 0; i < tbtn.length; i++) {
			UIToggleButton btn = tbtn[i];
			
			this.toggleButtons.add(btn);
			btn.setGroup(this);
			
			if(btn.isSelected()) {
				this.selectedButtons.add(btn);
			}
		}
	}
	
	public void remove(UIToggleButton... tbtn) {
		for(int i = 0; i < tbtn.length; i++) {
			this.toggleButtons.remove(tbtn[i]);
			tbtn[i].setGroup(null);
		}
	}
	
	public UIToggleButton remove(int index) {
		UIToggleButton btn = this.toggleButtons.remove(index);
		btn.setGroup(null);
		return btn;
	}
	
	public int getMinSelected() {
		return this.minSelected;
	}
	
	public void setMinSelected(int minSelected) {
		this.minSelected = MathUtils.clamp(minSelected, 0, this.maxSelected);
	}
	
	public int getMaxSelected() {
		return this.maxSelected;
	}
	
	public void setMaxSelected(int maxSelected) {
		this.maxSelected = Math.max(maxSelected, Math.max(this.minSelected, 1));
	}
}
