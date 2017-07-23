package io.github.nasso.nhengine.ui.layout;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;

public class UICardLayout implements UILayout {
	private List<UIComponent> cards = new ArrayList<UIComponent>();
	
	private UIComponent currentCard = null;
	
	public void apply(UIContainer parent) {
		for(int i = 0; i < parent.getChildrenCount(); i++) {
			UIComponent child = parent.getChild(i);
			if(child != null) child.setBounds(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom());
		}
	}
	
	public void getPreferredSize(UIContainer parent, Vector2f dest) {
		if(this.currentCard == null) dest.zero();
		else dest.set(this.currentCard.getPreferredSize());
	}
	
	public void addComponent(int index, UIComponent comp, Object constraints) {
		this.cards.add(index, comp);
		
		if(this.currentCard == null) this.setCurrentCard(index);
		else if(comp != null) comp.setVisible(false);
	}
	
	public void removeComponent(UIComponent comp) {
		this.cards.remove(comp);
	}
	
	public void removeAllComponents() {
		this.cards.clear();
		this.currentCard = null;
	}
	
	public UIComponent getCurrentCard() {
		return this.currentCard;
	}
	
	public int getCurrentCardIndex() {
		return this.cards.indexOf(this.currentCard);
	}
	
	public void setCurrentCard(int index) {
		if(index < 0 || index >= this.cards.size() || this.getCurrentCardIndex() == index) return;
		if(this.currentCard != null) this.currentCard.setVisible(false);
		
		this.currentCard = this.cards.get(index);
		if(this.currentCard != null) this.currentCard.setVisible(true);
	}
	
	public void next() {
		if(this.cards.isEmpty()) return;
		
		if(this.currentCard == null) {
			this.first();
			return;
		}
		
		int index = this.getCurrentCardIndex() + 1;
		if(index >= this.cards.size()) index = 0;
		
		this.setCurrentCard(index);
	}
	
	public void previous() {
		if(this.cards.isEmpty()) return;
		
		if(this.currentCard == null) {
			this.last();
			return;
		}
		
		int index = this.getCurrentCardIndex() - 1;
		if(index < 0) index = this.cards.size() - 1;
		
		this.setCurrentCard(index);
	}
	
	public void first() {
		if(this.cards.isEmpty()) return;
		
		this.setCurrentCard(0);
	}
	
	public void last() {
		if(this.cards.isEmpty()) return;
		
		this.setCurrentCard(this.cards.size() - 1);
	}
}
