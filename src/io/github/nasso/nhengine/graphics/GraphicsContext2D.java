package io.github.nasso.nhengine.graphics;

import java.util.Arrays;

import org.joml.Vector3fc;
import org.joml.Vector4fc;

public class GraphicsContext2D {
	public static final int CONST_LINECAP_BUTT = 0;
	public static final int CONST_LINECAP_ROUND = 1;
	public static final int CONST_LINECAP_SQUARE = 2;
	
	public static final int CONST_LINEJOIN_BEVEL = 0;
	public static final int CONST_LINEJOIN_MITER = 1;
	public static final int CONST_LINEJOIN_ROUND = 2;
	
	public static final int CONST_CW = 0;
	public static final int CONST_CCW = 1;
	
	public static final int CONST_TEXT_ALIGN_LEFT = 0;
	public static final int CONST_TEXT_ALIGN_CENTER = 1;
	public static final int CONST_TEXT_ALIGN_RIGHT = 2;
	
	public static final int CONST_TEXT_BASELINE_TOP = 0;
	public static final int CONST_TEXT_BASELINE_ASCENT_MIDDLE = 1;
	public static final int CONST_TEXT_BASELINE_MIDDLE = 2;
	public static final int CONST_TEXT_BASELINE_BASELINE = 3;
	public static final int CONST_TEXT_BASELINE_BOTTOM = 4;
	
	public static final int CMD_NONE = -1;
	
	public static final int CMD_CLEAR = 0;
	
	public static final int CMD_STATE_SAVE = 1;
	public static final int CMD_STATE_RESTORE = 2;
	public static final int CMD_STATE_RESET = 3;
	
	public static final int CMD_RDR_SET_STROKE_RGBA = 4;
	public static final int CMD_RDR_SET_FILL_RGBA = 5;
	
	public static final int CMD_RDR_SET_MITER_LIMIT = 6;
	public static final int CMD_RDR_SET_STROKE_SIZE = 7;
	public static final int CMD_RDR_SET_LINE_CAP = 8;
	public static final int CMD_RDR_SET_LINE_JOIN = 9;
	public static final int CMD_RDR_SET_GLOBAL_ALPHA = 10;
	
	public static final int CMD_TRANS_RESET = 11;
	public static final int CMD_TRANS_APPLY = 12;
	public static final int CMD_TRANS_TRANSLATE = 13;
	public static final int CMD_TRANS_ROTATE = 14;
	public static final int CMD_TRANS_SKEW_X = 15;
	public static final int CMD_TRANS_SKEW_Y = 16;
	public static final int CMD_TRANS_SCALE = 17;
	
	public static final int CMD_IMG_DRAW = 18;
	
	public static final int CMD_CLIP_SET = 19;
	// here lies the 20th command... R.I.P.
	public static final int CMD_CLIP_RESET = 21;
	
	public static final int CMD_PATH_BEGIN = 22;
	public static final int CMD_PATH_MOVETO = 23;
	public static final int CMD_PATH_LINETO = 24;
	public static final int CMD_PATH_BEZIERTO = 25;
	public static final int CMD_PATH_QUADTO = 26;
	public static final int CMD_PATH_ARCTO = 27;
	public static final int CMD_PATH_CLOSE = 28;
	public static final int CMD_PATH_SET_WINDING = 29;
	public static final int CMD_PATH_ARC = 30;
	public static final int CMD_PATH_RECT = 31;
	public static final int CMD_PATH_ROUNDEDRECT = 32;
	public static final int CMD_PATH_ELLIPSE = 33;
	public static final int CMD_PATH_CIRCLE = 34;
	public static final int CMD_PATH_FILL = 35;
	public static final int CMD_PATH_STROKE = 36;
	
	public static final int CMD_TEXT_SET_FONT = 37;
	public static final int CMD_TEXT_SET_ALIGN = 38;
	public static final int CMD_TEXT_SET_BASELINE = 39;
	public static final int CMD_TEXT_FILL = 40;
	public static final int CMD_TEXT_STROKE = 41;
	
	public static final int CMD_STROKE_LINE = 42;
	public static final int CMD_STROKE_RECT = 43;
	public static final int CMD_STROKE_ROUNDED_RECT = 44;
	public static final int CMD_FILL_RECT = 45;
	public static final int CMD_FILL_ROUNDED_RECT = 46;
	
	private static final int INI_STACK_SIZE = 2048;
	
	private static final int INI_STRARG_SIZE = 16;
	
	private float[] commandStack = new float[INI_STACK_SIZE];
	private int commandStackIndex = 0;
	
	private Object[] args = new Object[INI_STRARG_SIZE];
	private int argStackIndex = 0;
	
	private float lastCommand = CMD_NONE;
	
	public GraphicsContext2D() {
		
	}
	
	private void growCommandStackSizeIfNeeded() {
		if(this.commandStackIndex >= this.commandStack.length) {
			this.commandStack = Arrays.copyOf(this.commandStack, this.commandStackIndex);
		}
	}
	
	private void growArgStackSizeIfNeeded() {
		if(this.argStackIndex >= this.args.length) {
			this.args = Arrays.copyOf(this.args, this.argStackIndex);
		}
	}
	
	private void pushCommands(float cmd, float b, float c, float d, float e, float f, float g, float h, float i) {
		this.commandStackIndex += 9;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 9] = cmd;
		this.commandStack[this.commandStackIndex - 8] = b;
		this.commandStack[this.commandStackIndex - 7] = c;
		this.commandStack[this.commandStackIndex - 6] = d;
		this.commandStack[this.commandStackIndex - 5] = e;
		this.commandStack[this.commandStackIndex - 4] = f;
		this.commandStack[this.commandStackIndex - 3] = g;
		this.commandStack[this.commandStackIndex - 2] = h;
		this.commandStack[this.commandStackIndex - 1] = i;
	}
	
	private void pushCommands(float cmd, float b, float c, float d, float e, float f, float g) {
		this.commandStackIndex += 7;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 7] = cmd;
		this.commandStack[this.commandStackIndex - 6] = b;
		this.commandStack[this.commandStackIndex - 5] = c;
		this.commandStack[this.commandStackIndex - 4] = d;
		this.commandStack[this.commandStackIndex - 3] = e;
		this.commandStack[this.commandStackIndex - 2] = f;
		this.commandStack[this.commandStackIndex - 1] = g;
	}
	
	private void pushCommands(float cmd, float c, float d, float e, float f, float g) {
		this.commandStackIndex += 6;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 6] = cmd;
		this.commandStack[this.commandStackIndex - 5] = c;
		this.commandStack[this.commandStackIndex - 4] = d;
		this.commandStack[this.commandStackIndex - 3] = e;
		this.commandStack[this.commandStackIndex - 2] = f;
		this.commandStack[this.commandStackIndex - 1] = g;
	}
	
	private void pushCommands(float cmd, float d, float e, float f, float g) {
		this.commandStackIndex += 5;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 5] = cmd;
		this.commandStack[this.commandStackIndex - 4] = d;
		this.commandStack[this.commandStackIndex - 3] = e;
		this.commandStack[this.commandStackIndex - 2] = f;
		this.commandStack[this.commandStackIndex - 1] = g;
	}
	
	private void pushCommands(float cmd, float e, float f, float g) {
		this.commandStackIndex += 4;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 4] = cmd;
		this.commandStack[this.commandStackIndex - 3] = e;
		this.commandStack[this.commandStackIndex - 2] = f;
		this.commandStack[this.commandStackIndex - 1] = g;
	}
	
	private void pushCommands(float cmd, float f, float g) {
		this.commandStackIndex += 3;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 3] = cmd;
		this.commandStack[this.commandStackIndex - 2] = f;
		this.commandStack[this.commandStackIndex - 1] = g;
	}
	
	private void pushCommands(float cmd, float g) {
		this.commandStackIndex += 2;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 2] = cmd;
		this.commandStack[this.commandStackIndex - 1] = g;
	}
	
	private void pushCommands(float cmd) {
		this.commandStackIndex++;
		this.lastCommand = cmd;
		
		this.growCommandStackSizeIfNeeded();
		
		this.commandStack[this.commandStackIndex - 1] = cmd;
	}
	
	private void pushArg(Object arg) {
		this.argStackIndex++;
		
		this.growArgStackSizeIfNeeded();
		
		this.args[this.argStackIndex - 1] = arg;
	}
	
	public boolean isCommandStackEmpty() {
		return this.commandStackIndex == 0;
	}
	
	public float[] commandStack() {
		return this.commandStack;
	}
	
	public Object[] argStack() {
		return this.args;
	}
	
	public int getCommandStackSize() {
		return this.commandStackIndex;
	}
	
	public void resetBuffers() {
		this.commandStack = new float[INI_STACK_SIZE];
		this.args = new Object[INI_STRARG_SIZE];
		this.lastCommand = CMD_NONE;
	}
	
	public void discardAll() {
		this.commandStackIndex = 0;
		this.argStackIndex = 0;
		this.lastCommand = CMD_NONE;
	}
	
	public void apply(GraphicsContext2D gtx) {
		// Push commands
		this.commandStackIndex += gtx.commandStackIndex;
		this.growCommandStackSizeIfNeeded();
		System.arraycopy(gtx.commandStack, 0, this.commandStack, this.commandStackIndex - gtx.commandStackIndex, gtx.commandStackIndex);
		
		// Push args
		this.argStackIndex += gtx.argStackIndex;
		this.growArgStackSizeIfNeeded();
		System.arraycopy(gtx.args, 0, this.args, this.argStackIndex - gtx.argStackIndex, gtx.argStackIndex);
		
		// Last command
		this.lastCommand = gtx.lastCommand;
	}
	
	public void clear() {
		this.pushCommands(CMD_CLEAR);
	}
	
	public void save() {
		this.pushCommands(CMD_STATE_SAVE);
	}
	
	public void restore() {
		/*
		// If the last command is "save", undo it to prevent useless calculations, since this will lead to the same results
		if(this.commandStackIndex > 0 && this.lastCommand == CMD_STATE_SAVE) this.commandStack[--this.commandStackIndex] = 0;
		else this.pushCommands(CMD_STATE_RESTORE);
		*/
		
		this.pushCommands(CMD_STATE_RESTORE);
	}
	
	public void reset() {
		this.pushCommands(CMD_STATE_RESET);
	}
	
	public void setFill(float r, float g, float b, float a) {
		this.pushCommands(CMD_RDR_SET_FILL_RGBA, r, g, b, a);
	}
	
	public void setFill(float r, float g, float b) {
		this.setFill(r, g, b, 1);
	}
	
	public void setFill(float gray, float alpha) {
		this.setFill(gray, gray, gray, alpha);
	}
	
	public void setFill(float gray) {
		this.setFill(gray, gray, gray);
	}
	
	public void setFill(Color c) {
		if(c == null) return;
		
		this.setFill(c.red(), c.green(), c.blue(), c.alpha());
	}
	
	public void setFill(Vector3fc c, float alpha) {
		this.setFill(c.x(), c.y(), c.z(), alpha);
	}
	
	public void setFill(Vector3fc c) {
		this.setFill(c, 1.0f);
	}
	
	public void setFill(Vector4fc c) {
		if(c == null) return;
		
		this.setFill(c.x(), c.y(), c.z(), c.w());
	}
	
	public void setStroke(float r, float g, float b, float a) {
		this.pushCommands(CMD_RDR_SET_STROKE_RGBA, r, g, b, a);
	}
	
	public void setStroke(float r, float g, float b) {
		this.setStroke(r, g, b, 1);
	}
	
	public void setStroke(float gray, float alpha) {
		this.setStroke(gray, gray, gray, alpha);
	}
	
	public void setStroke(float gray) {
		this.setStroke(gray, 1.0f);
	}
	
	public void setStroke(Color c) {
		if(c == null) return;
		
		this.setStroke(c.red(), c.green(), c.blue(), c.alpha());
	}
	
	public void setStroke(Vector3fc c, float alpha) {
		this.setStroke(c.x(), c.y(), c.z(), alpha);
	}
	
	public void setStroke(Vector3fc c) {
		this.setStroke(c, 1.0f);
	}
	
	public void setStroke(Vector4fc c) {
		if(c == null) return;
		
		this.setStroke(c.x(), c.y(), c.z(), c.w());
	}
	
	public void setMiterLimit(float limit) {
		this.pushCommands(CMD_RDR_SET_MITER_LIMIT, limit);
	}
	
	public void setStrokeSize(float size) {
		this.pushCommands(CMD_RDR_SET_STROKE_SIZE, size);
	}
	
	public void setLineCap(LineCap cap) {
		switch(cap) {
			case BUTT:
				this.pushCommands(CMD_RDR_SET_LINE_CAP, CONST_LINECAP_BUTT);
				break;
			case ROUND:
				this.pushCommands(CMD_RDR_SET_LINE_CAP, CONST_LINECAP_ROUND);
				break;
			case SQUARE:
				this.pushCommands(CMD_RDR_SET_LINE_CAP, CONST_LINECAP_SQUARE);
				break;
		}
	}
	
	public void setLineCap(LineJoin join) {
		switch(join) {
			case BEVEL:
				this.pushCommands(CMD_RDR_SET_LINE_JOIN, CONST_LINEJOIN_BEVEL);
				break;
			case MITER:
				this.pushCommands(CMD_RDR_SET_LINE_JOIN, CONST_LINEJOIN_MITER);
				break;
			case ROUND:
				this.pushCommands(CMD_RDR_SET_LINE_JOIN, CONST_LINEJOIN_ROUND);
				break;
		}
	}
	
	public void setGlobalAlpha(float alpha) {
		this.pushCommands(CMD_RDR_SET_GLOBAL_ALPHA, alpha);
	}
	
	public void resetTransform() {
		this.pushCommands(CMD_TRANS_RESET);
	}
	
	public void transform(float a, float b, float c, float d, float e, float f) {
		this.pushCommands(CMD_TRANS_APPLY, a, b, c, d, e, f);
	}
	
	public void translate(float x, float y) {
		this.pushCommands(CMD_TRANS_TRANSLATE, x, y);
	}
	
	public void rotate(float a) {
		this.pushCommands(CMD_TRANS_ROTATE, a);
	}
	
	public void skewX(float angle) {
		this.pushCommands(CMD_TRANS_SKEW_X, angle);
	}
	
	public void skewY(float angle) {
		this.pushCommands(CMD_TRANS_SKEW_Y, angle);
	}
	
	public void scale(float x, float y) {
		this.pushCommands(CMD_TRANS_SCALE, x, y);
	}
	
	public void drawImage(Texture2D tex, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh) {
		if(dw == 0 || dh == 0) return;
		
		this.pushCommands(CMD_IMG_DRAW, sx, sy, sw, sh, dx, dy, dw, dh);
		this.pushArg(tex);
	}
	
	public void drawImage(Texture2D tex, float x, float y, float w, float h) {
		this.drawImage(tex, 0, 0, tex.getWidth(), tex.getHeight(), x, y, w, h);
	}
	
	public void drawImage(Texture2D tex, float x, float y) {
		this.drawImage(tex, x, y, tex.getWidth(), tex.getHeight());
	}
	
	public void clip(float x, float y, float w, float h) {
		this.pushCommands(CMD_CLIP_SET, x, y, w, h);
	}
	
	public void clipReset() {
		this.pushCommands(CMD_CLIP_RESET);
	}
	
	public void beginPath() {
		this.pushCommands(CMD_PATH_BEGIN);
	}
	
	public void moveTo(float x, float y) {
		this.pushCommands(CMD_PATH_MOVETO, x, y);
	}
	
	public void lineTo(float x, float y) {
		this.pushCommands(CMD_PATH_LINETO, x, y);
	}
	
	public void bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
		this.pushCommands(CMD_PATH_BEZIERTO, c1x, c1y, c2x, c2y, x, y);
	}
	
	public void quadTo(float cx, float cy, float x, float y) {
		this.pushCommands(cx, cy, x, y);
	}
	
	public void arcTo(float x1, float y1, float x2, float y2, float radius) {
		this.pushCommands(CMD_PATH_ARCTO, x1, y1, x2, y2, radius);
	}
	
	public void closePath() {
		this.pushCommands(CMD_PATH_CLOSE);
	}
	
	public void setPathWinding(boolean clockwise) {
		this.pushCommands(CMD_PATH_SET_WINDING, clockwise ? CONST_CW : CONST_CCW);
	}
	
	public void arc(float cx, float cy, float r, float a0, float a1, boolean clockwise) {
		this.pushCommands(CMD_PATH_ARC, cx, cy, r, a0, a1, clockwise ? CONST_CW : CONST_CCW);
	}
	
	public void rect(float x, float y, float w, float h) {
		this.pushCommands(CMD_PATH_RECT, x, y, w, h);
	}
	
	public void roundedRect(float x, float y, float w, float h, float r) {
		this.pushCommands(CMD_PATH_ROUNDEDRECT, x, y, w, h, r);
	}
	
	public void ellipse(float cx, float cy, float rw, float ry) {
		this.pushCommands(CMD_PATH_ELLIPSE, cx, cy, rw, ry);
	}
	
	public void circle(float cx, float cy, float r) {
		this.pushCommands(CMD_PATH_CIRCLE, cx, cy, r);
	}
	
	public void fill() {
		this.pushCommands(CMD_PATH_FILL);
	}
	
	public void stroke() {
		this.pushCommands(CMD_PATH_STROKE);
	}
	
	public void setFont(Font fnt) {
		this.pushCommands(CMD_TEXT_SET_FONT);
		this.pushArg(fnt);
	}
	
	public void setTextAlign(TextAlignment align) {
		switch(align) {
			case CENTER:
				this.pushCommands(CMD_TEXT_SET_ALIGN, CONST_TEXT_ALIGN_CENTER);
				break;
			case LEFT:
				this.pushCommands(CMD_TEXT_SET_ALIGN, CONST_TEXT_ALIGN_LEFT);
				break;
			case RIGHT:
				this.pushCommands(CMD_TEXT_SET_ALIGN, CONST_TEXT_ALIGN_RIGHT);
				break;
		}
	}
	
	public void setTextBaseline(TextBaseline baseline) {
		switch(baseline) {
			case BASELINE:
				this.pushCommands(CMD_TEXT_SET_BASELINE, CONST_TEXT_BASELINE_BASELINE);
				break;
			case BOTTOM:
				this.pushCommands(CMD_TEXT_SET_BASELINE, CONST_TEXT_BASELINE_BOTTOM);
				break;
			case MIDDLE:
				this.pushCommands(CMD_TEXT_SET_BASELINE, CONST_TEXT_BASELINE_MIDDLE);
				break;
			case TOP:
				this.pushCommands(CMD_TEXT_SET_BASELINE, CONST_TEXT_BASELINE_TOP);
				break;
			case ASCENT_MIDDLE:
				this.pushCommands(CMD_TEXT_SET_BASELINE, CONST_TEXT_BASELINE_ASCENT_MIDDLE);
				break;
		}
	}
	
	public void fillText(String txt, float x, float y) {
		if(txt.length() <= 0) return;
		
		this.pushCommands(CMD_TEXT_FILL, x, y);
		this.pushArg(txt);
	}
	
	public void strokeText(CharSequence txt, float x, float y) {
		this.pushCommands(CMD_TEXT_STROKE, x, y);
		this.pushArg(txt);
	}
	
	public void strokeLine(float sx, float sy, float ex, float ey) {
		this.pushCommands(CMD_STROKE_LINE, sx, sy, ex, ey);
	}
	
	public void strokeRect(float x, float y, float w, float h) {
		this.pushCommands(CMD_STROKE_RECT, x, y, w, h);
	}
	
	public void fillRect(float x, float y, float w, float h) {
		this.pushCommands(CMD_FILL_RECT, x, y, w, h);
	}
	
	public void fillRoundedRect(float x, float y, float w, float h, float r) {
		this.pushCommands(CMD_FILL_ROUNDED_RECT, x, y, w, h, r);
	}
	
	public void strokeRoundedRect(float x, float y, float w, float h, float r) {
		this.pushCommands(CMD_STROKE_ROUNDED_RECT, x, y, w, h, r);
	}
}
