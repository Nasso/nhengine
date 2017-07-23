package io.github.nasso.nhengine.opengl;

import static org.lwjgl.opengl.GL11.*;

public class OGLStateManager {
	public static final OGLStateManager INSTANCE = new OGLStateManager();
	
	private boolean blend = false;
	private boolean depthTest = false;
	private boolean culling = false;
	private boolean stencilTest = false;
	
	private int blendSrc, blendDst;
	private int stencilFunc, stencilFunc_ref, stencilFunc_mask;
	private int stencilOp_sfail, stencilOp_dpfail, stencilOp_dppass;
	
	private OGLStateManager() {
		
	}
	
	public void refreshState() {
		this.blend = glIsEnabled(GL_BLEND);
		this.depthTest = glIsEnabled(GL_DEPTH_TEST);
		this.culling = glIsEnabled(GL_CULL_FACE);
		this.stencilTest = glIsEnabled(GL_STENCIL_TEST);
		
		this.blendSrc = glGetInteger(GL_BLEND_SRC);
		this.blendDst = glGetInteger(GL_BLEND_DST);
		
		this.stencilFunc = glGetInteger(GL_STENCIL_FUNC);
		this.stencilFunc_ref = glGetInteger(GL_STENCIL_REF);
		this.stencilFunc_mask = glGetInteger(GL_STENCIL_VALUE_MASK);
		
		this.stencilOp_sfail = glGetInteger(GL_STENCIL_FAIL);
		this.stencilOp_dpfail = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL);
		this.stencilOp_dppass = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS);
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
