package io.github.nasso.nhengine.opengl;

import static org.lwjgl.opengl.GL11.*;

public class OGLStateManager {
	public static final OGLStateManager INSTANCE = new OGLStateManager();
	
	private boolean blend = false;
	private boolean depthTest = false;
	private boolean culling = false;
	private boolean stencilTest = false;
	
	private int blendSrc = GL_SRC_ALPHA, blendDst = GL_ONE_MINUS_SRC_ALPHA;
	private int stencilFunc = GL_ALWAYS, stencilFunc_ref = 0,
			stencilFunc_mask = 0;
	private int stencilOp_sfail = GL_KEEP, stencilOp_dpfail = GL_KEEP,
			stencilOp_dppass = GL_KEEP;
	
	private OGLStateManager() {
		
	}
	
	public void refreshState() {
		if(this.blend) glEnable(GL_BLEND);
		else glDisable(GL_BLEND);
		
		if(this.depthTest) glEnable(GL_DEPTH_TEST);
		else glDisable(GL_DEPTH_TEST);
		
		if(this.culling) glEnable(GL_CULL_FACE);
		else glDisable(GL_CULL_FACE);
		
		if(this.stencilTest) glEnable(GL_STENCIL_TEST);
		else glDisable(GL_STENCIL_TEST);
		
		glBlendFunc(this.blendSrc, this.blendDst);
		
		glStencilFunc(this.stencilFunc, this.stencilFunc_ref, this.stencilFunc_mask);
		glStencilOp(this.stencilOp_sfail, this.stencilOp_dpfail, this.stencilOp_dppass);
	}
	
	public void stencilFunc(int func, int ref, int mask) {
		if(this.stencilFunc == func && this.stencilFunc_ref == ref && this.stencilFunc_mask == mask) return;
		
		this.stencilFunc = func;
		this.stencilFunc_ref = ref;
		this.stencilFunc_mask = mask;
		
		glStencilFunc(func, ref, mask);
	}
	
	public void stencilOp(int sfail, int dpfail, int dppass) {
		if(this.stencilOp_sfail == sfail && this.stencilOp_dpfail == dpfail && this.stencilOp_dppass == dppass) return;
		
		this.stencilOp_sfail = sfail;
		this.stencilOp_dpfail = dpfail;
		this.stencilOp_dppass = dppass;
		
		glStencilOp(sfail, dpfail, dppass);
	}
	
	public void blendFunc(int src, int dst) {
		if(this.blendSrc == src && this.blendDst == dst) return;
		
		this.blendSrc = src;
		this.blendDst = dst;
		
		glBlendFunc(src, dst);
	}
	
	public void blend(boolean val) {
		if(this.blend == val) return;
		
		this.blend = val;
		
		if(val) {
			glEnable(GL_BLEND);
		} else {
			glDisable(GL_BLEND);
		}
	}
	
	public void culling(boolean val) {
		if(this.culling == val) return;
		
		this.culling = val;
		
		if(val) {
			glEnable(GL_CULL_FACE);
		} else {
			glDisable(GL_CULL_FACE);
		}
	}
	
	public void depthTest(boolean val) {
		if(this.depthTest == val) return;
		
		this.depthTest = val;
		
		if(val) {
			glEnable(GL_DEPTH_TEST);
		} else {
			glDisable(GL_DEPTH_TEST);
		}
	}
	
	public void stencilTest(boolean val) {
		if(this.stencilTest == val) return;
		
		this.stencilTest = val;
		
		if(val) {
			glEnable(GL_STENCIL_TEST);
		} else {
			glDisable(GL_STENCIL_TEST);
		}
	}
}
