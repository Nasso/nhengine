package io.github.nasso.nhengine.utils;

import java.util.ArrayList;
import java.util.List;

public class AnimationChain extends Animation {
	private boolean animationSide = false;
	
	private int currentCycle = 0;
	private int currentIndex = 0;
	
	private float value = 0.0f;
	
	private List<Animation> anims = new ArrayList<Animation>();
	
	public void step(float delta) {
		if(this.isFinished()) return;
		
		Animation anim = this.anims.get(this.currentIndex);
		if(anim.isFinished()) {
			if(!this.isRestartOnEnd() && this.animationSide) this.currentIndex--;
			else this.currentIndex++;
			
			if(this.currentIndex >= this.anims.size() || this.currentIndex < 0) {
				this.animationSide = !this.animationSide;
				
				if(this.getCycleCount() >= 0) {
					this.currentCycle++;
					
					if(this.currentCycle < this.getCycleCount()) {
						if(!this.isRestartOnEnd() && this.animationSide) this.currentIndex = this.anims.size() - 1;
						else this.currentIndex = 0;
						
						for(Animation a : this.anims) {
							if(!this.isRestartOnEnd() && this.animationSide) {
								a.toEnd();
								a.setSpeed(-1);
							} else {
								a.reset();
							}
						}
					} else if(this.currentCycle >= this.getCycleCount()) return;
				} else {
					for(Animation a : this.anims) {
						if(!this.isRestartOnEnd() && this.animationSide) {
							a.toEnd();
							a.setSpeed(-1);
						} else {
							a.reset();
						}
					}
					
					this.currentIndex = 0;
				}
			}
			
			anim = this.anims.get(this.currentIndex);
		}
		
		anim.step(delta);
		this.value = anim.getValue();
	}
	
	public void reset() {
		for(Animation anim : this.anims) {
			anim.reset();
		}
		
		this.currentIndex = 0;
		this.currentCycle = 0;
	}
	
	public float getValue() {
		return this.value;
	}
	
	public boolean isFinished() {
		return this.getCycleCount() == 0 || (this.getCycleCount() > 0 && this.currentCycle >= this.getCycleCount());
	}
	
	public int addAnimation(Animation anim) {
		this.anims.add(anim);
		
		return this.anims.size() - 1;
	}
	
	public void toEnd() {
		for(Animation a : this.anims) {
			a.toEnd();
		}
		
		this.currentIndex = this.anims.size() - 1;
		this.currentCycle = this.getCycleCount() - 1;
		this.animationSide = false;
	}
}
