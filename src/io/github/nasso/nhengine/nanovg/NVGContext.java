package io.github.nasso.nhengine.nanovg;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.joml.Vector2i;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGGlyphPosition;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NVGTextRow;
import org.lwjgl.nanovg.NanoVGGL3;

import io.github.nasso.nhengine.opengl.OGLManager;

public class NVGContext {
	private static final boolean DEBUG = OGLManager.DEBUG;
	private static NVGContext singlectx;
	
	private long ctx = 0;
	
	// Arrays used in some methods
	private int[] itemp_a = new int[1];
	private int[] itemp_b = new int[1];
	private float[] ftemp_a = new float[1];
	private float[] ftemp_b = new float[1];
	private float[] ftemp_c = new float[1];
	
	private NVGContext(int flags) {
		this.ctx = NanoVGGL3.nvgCreate(flags);
	}
	
	// Begin drawing a new frame
	// Calls to nanovg drawing API should be wrapped in nvgBeginFrame() &
	// nvgEndFrame()
	// nvgBeginFrame() defines the size of the window to render to in relation
	// currently
	// set viewport (i.e. glViewport on GL backends). Device pixel ration allows
	// to
	// control the rendering on Hi-DPI devices.
	// For example, GLFW returns two dimension for an opened window: window size
	// and
	// frame buffer size. In that case you would set windowWidth/Height to the
	// window size
	// devicePixelRatio to: frameBufferWidth / windowWidth.
	
	/**
	 * Begins drawing a new frame.
	 * 
	 * <p>
	 * Calls to nanovg drawing API should be wrapped in {@link #nvgBeginFrame BeginFrame} &amp; {@link #nvgEndFrame EndFrame}. {@link #nvgBeginFrame BeginFrame} defines the size of the window to render to in relation currently set viewport (i.e. {@code glViewport} on GL backends). Device pixel ration allows to control the rendering on Hi-DPI devices. For example, GLFW returns two dimension for an opened window: window size and frame buffer size. In that case you would set {@code windowWidth/Height} to the window size {@code devicePixelRatio} to: {@code frameBufferWidth / windowWidth}.
	 * </p>
	 *
	 * @param windowWidth
	 *            the window width
	 * @param windowHeight
	 *            the window height
	 * @param devicePixelRatio
	 *            the device pixel ratio
	 */
	public void beginFrame(int windowWidth, int windowHeight, float devicePixelRatio) {
		nvgBeginFrame(this.ctx, windowWidth, windowHeight, devicePixelRatio);
	}
	
	/**
	 * Cancels drawing the current frame.
	 */
	public void cancelFrame() {
		nvgCancelFrame(this.ctx);
	}
	
	/**
	 * Ends drawing flushing remaining render state.
	 */
	public void endFrame() {
		nvgEndFrame(this.ctx);
	}
	
	//
	// State Handling
	//
	// NanoVG contains state which represents how paths will be rendered.
	// The state contains transform, fill and stroke styles, text and font
	// styles,
	// and scissor clipping.
	
	/**
	 * Pushes and saves the current render state into a state stack. A matching {@link #nvgRestore Restore} must be used to restore the state.
	 */
	public void save() {
		nvgSave(this.ctx);
	}
	
	/**
	 * Pops and restores current render state.
	 */
	public void restore() {
		nvgRestore(this.ctx);
	}
	
	/**
	 * Resets current render state to default values. Does not affect the render state stack.
	 */
	public void reset() {
		nvgReset(this.ctx);
	}
	
	//
	// Render styles
	//
	// Fill and stroke render style can be either a solid color or a paint which
	// is a gradient or a pattern.
	// Solid color is simply defined as a color value, different kinds of paints
	// can be created
	// using nvgLinearGradient(), nvgBoxGradient(), nvgRadialGradient() and
	// nvgImagePattern().
	//
	// Current render style can be saved and restored using nvgSave() and
	// nvgRestore().
	
	/**
	 * Sets current stroke style to a solid color.
	 * 
	 * @param color
	 *            the color to set
	 */
	public void strokeColor(NVGColor color) {
		nvgStrokeColor(this.ctx, color);
	}
	
	/**
	 * Sets current stroke style to a paint, which can be a one of the gradients or a pattern.
	 *
	 * @param paint
	 *            the paint to set
	 */
	public void strokePaint(NVGPaint paint) {
		nvgStrokePaint(this.ctx, paint);
	}
	
	/**
	 * Sets current fill style to a solid color.
	 *
	 * @param color
	 *            the color to set
	 */
	public void fillColor(NVGColor color) {
		nvgFillColor(this.ctx, color);
	}
	
	/**
	 * Sets current fill style to a paint, which can be a one of the gradients or a pattern.
	 *
	 * @param paint
	 *            the paint to set
	 */
	public void fillPaint(NVGPaint paint) {
		nvgFillPaint(this.ctx, paint);
	}
	
	/**
	 * Sets the miter limit of the stroke style. Miter limit controls when a sharp corner is beveled.
	 *
	 * @param limit
	 *            the miter limit to set
	 */
	public void miterLimit(float limit) {
		nvgMiterLimit(this.ctx, limit);
	}
	
	/**
	 * Sets the stroke width of the stroke style.
	 *
	 * @param size
	 *            the stroke width to set
	 */
	public void strokeWidth(float size) {
		nvgStrokeWidth(this.ctx, size);
	}
	
	/**
	 * Sets how the end of the line (cap) is drawn.
	 * 
	 * <p>
	 * The default line cap is {@link #NVG_BUTT BUTT}.
	 * </p>
	 *
	 * @param cap
	 *            the line cap to set. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_BUTT BUTT}</td>
	 *            <td>{@link #NVG_ROUND ROUND}</td>
	 *            <td>{@link #NVG_SQUARE SQUARE}</td>
	 *            </tr>
	 *            </table>
	 */
	public void lineCap(int cap) {
		nvgLineCap(this.ctx, cap);
	}
	
	/**
	 * Sets how sharp path corners are drawn.
	 * 
	 * <p>
	 * The default line join is {@link #NVG_MITER MITER}.
	 * </p>
	 *
	 * @param join
	 *            the line join to set. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_MITER MITER}</td>
	 *            <td>{@link #NVG_ROUND ROUND}</td>
	 *            <td>{@link #NVG_BEVEL BEVEL}</td>
	 *            </tr>
	 *            </table>
	 */
	public void lineJoin(int join) {
		nvgLineJoin(this.ctx, join);
	}
	
	/**
	 * Sets the transparency applied to all rendered shapes.
	 * 
	 * <p>
	 * Already transparent paths will get proportionally more transparent as well.
	 * </p>
	 *
	 * @param alpha
	 *            the alpha value to set
	 */
	public void globalAlpha(float alpha) {
		nvgGlobalAlpha(this.ctx, alpha);
	}
	
	//
	// Transforms
	//
	// The paths, gradients, patterns and scissor region are transformed by an
	// transformation
	// matrix at the time when they are passed to the API.
	// The current transformation matrix is a affine matrix:
	// [sx kx tx]
	// [ky sy ty]
	// [ 0 0 1]
	// Where: sx,sy define scaling, kx,ky skewing, and tx,ty translation.
	// The last row is assumed to be 0,0,1 and is not stored.
	//
	// Apart from nvgResetTransform(), each transformation function first
	// creates
	// specific transformation matrix and pre-multiplies the current
	// transformation by it.
	//
	// Current coordinate system (transformation) can be saved and restored
	// using nvgSave() and nvgRestore().
	
	/**
	 * Resets current transform to an identity matrix.
	 */
	public void resetTransform() {
		nvgResetTransform(this.ctx);
	}
	
	/**
	 * Premultiplies current coordinate system by specified matrix. The parameters are interpreted as matrix as follows:
	 * 
	 * <pre>
	 * <code>[a c e]
	 * [b d f]
	 * [0 0 1]</code>
	 * </pre>
	 *
	 * @param a
	 *            the a value
	 * @param b
	 *            the b value
	 * @param c
	 *            the c value
	 * @param d
	 *            the d value
	 * @param e
	 *            the e value
	 * @param f
	 *            the f value
	 */
	public void transform(float a, float b, float c, float d, float e, float f) {
		nvgTransform(this.ctx, a, b, c, d, e, f);
	}
	
	/**
	 * Translates current coordinate system.
	 *
	 * @param x
	 *            the X axis translation amount
	 * @param y
	 *            the Y axis translation amount
	 */
	public void translate(float x, float y) {
		nvgTranslate(this.ctx, x, y);
	}
	
	/**
	 * Rotates current coordinate system.
	 *
	 * @param angle
	 *            the rotation angle, in radians
	 */
	public void rotate(float angle) {
		nvgRotate(this.ctx, angle);
	}
	
	/**
	 * Skews the current coordinate system along X axis.
	 *
	 * @param angle
	 *            the skew angle, in radians
	 */
	public void skewX(float angle) {
		nvgSkewX(this.ctx, angle);
	}
	
	/**
	 * Skews the current coordinate system along Y axis.
	 *
	 * @param angle
	 *            the skew angle, in radians
	 */
	public void skewY(float angle) {
		nvgSkewY(this.ctx, angle);
	}
	
	/**
	 * Scales the current coordinate system.
	 *
	 * @param x
	 *            the X axis scale factor
	 * @param y
	 *            the Y axis scale factor
	 */
	public void scale(float x, float y) {
		nvgScale(this.ctx, x, y);
	}
	
	/**
	 * Stores the top part (a-f) of the current transformation matrix in to the specified buffer.
	 * 
	 * <pre>
	 * <code>
	 * [a c e]
	 * [b d f]
	 * [0 0 1]
	 * </code>
	 * </pre>
	 * 
	 * <p>
	 * There should be space for 6 floats in the return buffer for the values {@code a-f}.
	 * </p>
	 *
	 * @param xform
	 *            the destination buffer
	 */
	public void currentTransform(float[] xform) {
		nvgCurrentTransform(this.ctx, xform);
	}
	
	/**
	 * Stores the top part (a-f) of the current transformation matrix in to the specified buffer.
	 * 
	 * <pre>
	 * <code>
	 * [a c e]
	 * [b d f]
	 * [0 0 1]
	 * </code>
	 * </pre>
	 * 
	 * <p>
	 * There should be space for 6 floats in the return buffer for the values {@code a-f}.
	 * </p>
	 *
	 * @param xform
	 *            the destination buffer
	 */
	public void currentTransform(FloatBuffer xform) {
		nvgCurrentTransform(this.ctx, xform);
	}
	
	/**
	 * Stores the top part (a-f) of the current transformation matrix in to the specified buffer.
	 * 
	 * <pre>
	 * <code>
	 * [a c e]
	 * [b d f]
	 * [0 0 1]
	 * </code>
	 * </pre>
	 * 
	 * <p>
	 * There should be space for 6 floats in the return buffer for the values {@code a-f}.
	 * </p>
	 *
	 * @param xform
	 *            the destination buffer
	 */
	public void currentTransform(Matrix3f xform) {
		xform.identity();
		float[] data = new float[9];
		nvgCurrentTransform(this.ctx, data);
		xform.set(data);
	}
	
	//
	// Images
	//
	// NanoVG allows you to load jpg, png, psd, tga, pic and gif files to be
	// used for rendering.
	// In addition you can upload your own image. The image loading is provided
	// by stb_image.
	// The parameter imageFlags is combination of flags defined in
	// NVGimageFlags.
	
	/**
	 * Creates image by loading it from the disk from specified file name.
	 *
	 * @param filename
	 *            the image file name
	 * @param imageFlags
	 *            the image flags. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_IMAGE_GENERATE_MIPMAPS IMAGE_GENERATE_MIPMAPS}</td>
	 *            <td>{@link #NVG_IMAGE_REPEATX IMAGE_REPEATX}</td>
	 *            <td>{@link #NVG_IMAGE_REPEATY IMAGE_REPEATY}</td>
	 *            <td>{@link #NVG_IMAGE_FLIPY IMAGE_FLIPY}</td>
	 *            <td>{@link #NVG_IMAGE_PREMULTIPLIED IMAGE_PREMULTIPLIED}</td>
	 *            </tr>
	 *            </table>
	 *
	 * @return a handle to the image
	 */
	public int createImage(String filename, int imageFlags) {
		return nvgCreateImage(this.ctx, filename, imageFlags);
	}
	
	/**
	 * Creates image by loading it from the specified chunk of memory.
	 *
	 * @param imageFlags
	 *            the image flags. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_IMAGE_GENERATE_MIPMAPS IMAGE_GENERATE_MIPMAPS}</td>
	 *            <td>{@link #NVG_IMAGE_REPEATX IMAGE_REPEATX}</td>
	 *            <td>{@link #NVG_IMAGE_REPEATY IMAGE_REPEATY}</td>
	 *            <td>{@link #NVG_IMAGE_FLIPY IMAGE_FLIPY}</td>
	 *            <td>{@link #NVG_IMAGE_PREMULTIPLIED IMAGE_PREMULTIPLIED}</td>
	 *            </tr>
	 *            </table>
	 * @param data
	 *            the image data
	 *
	 * @return a handle to the image
	 */
	public int createImageMem(int imageFlags, ByteBuffer data) {
		return nvgCreateImageMem(this.ctx, imageFlags, data);
	}
	
	/**
	 * Creates image from specified image data.
	 *
	 * @param w
	 *            the image width
	 * @param h
	 *            the image height
	 * @param imageFlags
	 *            the image flags. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_IMAGE_GENERATE_MIPMAPS IMAGE_GENERATE_MIPMAPS}</td>
	 *            <td>{@link #NVG_IMAGE_REPEATX IMAGE_REPEATX}</td>
	 *            <td>{@link #NVG_IMAGE_REPEATY IMAGE_REPEATY}</td>
	 *            <td>{@link #NVG_IMAGE_FLIPY IMAGE_FLIPY}</td>
	 *            <td>{@link #NVG_IMAGE_PREMULTIPLIED IMAGE_PREMULTIPLIED}</td>
	 *            </tr>
	 *            </table>
	 * @param data
	 *            the image data
	 *
	 * @return a handle to the image
	 */
	public int createImageRGBA(int w, int h, int imageFlags, ByteBuffer data) {
		return nvgCreateImageRGBA(this.ctx, w, h, imageFlags, data);
	}
	
	/**
	 * Updates image data specified by image handle.
	 *
	 * @param image
	 *            the image handle
	 * @param data
	 *            the image data
	 */
	public void updateImage(int image, ByteBuffer data) {
		nvgUpdateImage(this.ctx, image, data);
	}
	
	/**
	 * Returns the dimensions of a created image.
	 *
	 * @param image
	 *            the image handle
	 * 
	 * @return the image size
	 */
	public Vector2i imageSize(int image) {
		return this.imageSize(image, null);
	}
	
	/**
	 * Returns the dimensions of a created image.
	 *
	 * @param image
	 *            the image handle
	 * @param target
	 *            returns the image size
	 * 
	 * @return the image size
	 */
	public Vector2i imageSize(int image, Vector2i target) {
		if(target == null) target = new Vector2i();
		
		nvgImageSize(this.ctx, image, this.itemp_a, this.itemp_b);
		
		target.x = this.itemp_a[0];
		target.y = this.itemp_b[0];
		
		return target;
	}
	
	/**
	 * Deletes created image.
	 *
	 * @param image
	 *            the image handle to delete
	 */
	public void deleteImage(int image) {
		nvgDeleteImage(this.ctx, image);
	}
	
	//
	// Scissoring
	//
	// Scissoring allows you to clip the rendering into a rectangle. This is
	// useful for various
	// user interface cases like rendering a text edit or a timeline.
	
	/**
	 * Sets the current scissor rectangle.
	 * 
	 * <p>
	 * The scissor rectangle is transformed by the current transform.
	 * </p>
	 *
	 * @param x
	 *            the rectangle X axis coordinate
	 * @param y
	 *            the rectangle Y axis coordinate
	 * @param w
	 *            the rectangle width
	 * @param h
	 *            the rectangle height
	 */
	public void scissor(float x, float y, float w, float h) {
		nvgScissor(this.ctx, x, y, w, h);
	}
	
	/**
	 * Intersects current scissor rectangle with the specified rectangle.
	 * 
	 * <p>
	 * The scissor rectangle is transformed by the current transform.
	 * </p>
	 * 
	 * <p>
	 * Note: in case the rotation of previous scissor rect differs from the current one, the intersection will be done between the specified rectangle and the previous scissor rectangle transformed in the current transform space. The resulting shape is always rectangle.
	 * </p>
	 *
	 * @param x
	 *            the rectangle X axis coordinate
	 * @param y
	 *            the rectangle Y axis coordinate
	 * @param w
	 *            the rectangle width
	 * @param h
	 *            the rectangle height
	 */
	public void intersectScissor(float x, float y, float w, float h) {
		nvgIntersectScissor(this.ctx, x, y, w, h);
	}
	
	/**
	 * Resets and disables scissoring.
	 */
	public void resetScissor() {
		nvgResetScissor(this.ctx);
	}
	
	//
	// Paths
	//
	// Drawing a new shape starts with nvgBeginPath(), it clears all the
	// currently defined paths.
	// Then you define one or more paths and sub-paths which describe the shape.
	// The are functions
	// to draw common shapes like rectangles and circles, and lower level
	// step-by-step functions,
	// which allow to define a path curve by curve.
	//
	// NanoVG uses even-odd fill rule to draw the shapes. Solid shapes should
	// have counter clockwise
	// winding and holes should have counter clockwise order. To specify winding
	// of a path you can
	// call nvgPathWinding(). This is useful especially for the common shapes,
	// which are drawn CCW.
	//
	// Finally you can fill the path using current fill style by calling
	// nvgFill(), and stroke it
	// with current stroke style by calling nvgStroke().
	//
	// The curve segments and sub-paths are transformed by the current
	// transform.
	
	/**
	 * Clears the current path and sub-paths.
	 */
	public void beginPath() {
		nvgBeginPath(this.ctx);
	}
	
	/**
	 * Starts new sub-path with specified point as first point.
	 *
	 * @param x
	 *            the point X axis coordinate
	 * @param y
	 *            the point Y axis coordinate
	 */
	public void moveTo(float x, float y) {
		nvgMoveTo(this.ctx, x, y);
	}
	
	/**
	 * Adds line segment from the last point in the path to the specified point.
	 *
	 * @param x
	 *            the point X axis coordinate
	 * @param y
	 *            the point Y axis coordinate
	 */
	public void lineTo(float x, float y) {
		nvgLineTo(this.ctx, x, y);
	}
	
	/**
	 * Adds cubic bezier segment from last point in the path via two control points to the specified point.
	 *
	 * @param c1x
	 *            the first control point X axis coordinate
	 * @param c1y
	 *            the first control point Y axis coordinate
	 * @param c2x
	 *            the second control point X axis coordinate
	 * @param c2y
	 *            the second control point Y axis coordinate
	 * @param x
	 *            the point X axis coordinate
	 * @param y
	 *            the point Y axis coordinate
	 */
	public void bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
		nvgBezierTo(this.ctx, c1x, c1y, c2x, c2y, x, y);
	}
	
	/**
	 * Adds quadratic bezier segment from last point in the path via a control point to the specified point.
	 *
	 * @param cx
	 *            the control point X axis coordinate
	 * @param cy
	 *            the control point Y axis coordinate
	 * @param x
	 *            the point X axis coordinate
	 * @param y
	 *            the point Y axis coordinate
	 */
	public void quadTo(float cx, float cy, float x, float y) {
		nvgQuadTo(this.ctx, cx, cy, x, y);
	}
	
	/**
	 * Adds an arc segment at the corner defined by the last path point, and two specified points.
	 *
	 * @param x1
	 *            the first point X axis coordinate
	 * @param y1
	 *            the first point Y axis coordinate
	 * @param x2
	 *            the second point X axis coordinate
	 * @param y2
	 *            the second point Y axis coordinate
	 * @param radius
	 *            the arc radius, in radians
	 */
	public void arcTo(float x1, float y1, float x2, float y2, float radius) {
		nvgArcTo(this.ctx, x1, y1, x2, y2, radius);
	}
	
	/**
	 * Closes current sub-path with a line segment.
	 */
	public void closePath() {
		nvgClosePath(this.ctx);
	}
	
	/**
	 * Sets the current sub-path winding.
	 *
	 * @param dir
	 *            the sub-path winding. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_CCW CCW}</td>
	 *            <td>{@link #NVG_CW CW}</td>
	 *            </tr>
	 *            </table>
	 */
	public void pathWinding(int dir) {
		nvgPathWinding(this.ctx, dir);
	}
	
	/**
	 * Creates new circle arc shaped sub-path.
	 *
	 * @param cx
	 *            the arc center X axis coordinate
	 * @param cy
	 *            the arc center Y axis coordinate
	 * @param r
	 *            the arc radius
	 * @param a0
	 *            the arc starting angle, in radians
	 * @param a1
	 *            the arc ending angle, in radians
	 * @param dir
	 *            the arc direction. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_CCW CCW}</td>
	 *            <td>{@link #NVG_CW CW}</td>
	 *            </tr>
	 *            </table>
	 */
	public void arc(float cx, float cy, float r, float a0, float a1, int dir) {
		nvgArc(this.ctx, cx, cy, r, a0, a1, dir);
	}
	
	/**
	 * Creates new rectangle shaped sub-path.
	 *
	 * @param x
	 *            the rectangle X axis coordinate
	 * @param y
	 *            the rectangle Y axis coordinate
	 * @param w
	 *            the rectangle width
	 * @param h
	 *            the rectangle height
	 */
	public void rect(float x, float y, float w, float h) {
		nvgRect(this.ctx, x, y, w, h);
	}
	
	/**
	 * Creates new rounded rectangle shaped sub-path.
	 *
	 * @param x
	 *            the rectangle X axis coordinate
	 * @param y
	 *            the rectangle Y axis coordinate
	 * @param w
	 *            the rectangle width
	 * @param h
	 *            the rectangle height
	 * @param r
	 *            the corner radius
	 */
	public void roundedRect(float x, float y, float w, float h, float r) {
		nvgRoundedRect(this.ctx, x, y, w, h, r);
	}
	
	/**
	 * Creates new ellipse shaped sub-path.
	 *
	 * @param cx
	 *            the ellipse center X axis coordinate
	 * @param cy
	 *            the ellipse center Y axis coordinate
	 * @param rx
	 *            the ellipse X axis radius
	 * @param ry
	 *            the ellipse Y axis radius
	 */
	public void ellipse(float cx, float cy, float rx, float ry) {
		nvgEllipse(this.ctx, cx, cy, rx, ry);
	}
	
	/**
	 * Creates new circle shaped sub-path.
	 *
	 * @param cx
	 *            the circle center X axis coordinate
	 * @param cy
	 *            the circle center Y axis coordinate
	 * @param r
	 *            the circle radius
	 */
	public void circle(float cx, float cy, float r) {
		nvgCircle(this.ctx, cx, cy, r);
	}
	
	/**
	 * Fills the current path with current fill style.
	 */
	public void fill() {
		nvgFill(this.ctx);
	}
	
	/**
	 * Fills the current path with current stroke style.
	 */
	public void stroke() {
		nvgStroke(this.ctx);
	}
	
	//
	// Paints
	//
	// NanoVG supports four types of paints: linear gradient, box gradient,
	// radial gradient and image pattern.
	// These can be used as paints for strokes and fills.
	
	/**
	 * Creates and returns a linear gradient.
	 * 
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to {@link #nvgFillPaint FillPaint} or {@link #nvgStrokePaint StrokePaint}.
	 * </p>
	 *
	 * @param sx
	 *            the X axis start coordinate
	 * @param sy
	 *            the Y axis start coordinate
	 * @param ex
	 *            the X axis end coordinate
	 * @param ey
	 *            the Y axis end coordinate
	 * @param icol
	 *            the start color
	 * @param ocol
	 *            the end color
	 */
	public NVGPaint linearGradient(float sx, float sy, float ex, float ey, NVGColor icol, NVGColor ocol, NVGPaint target) {
		return nvgLinearGradient(this.ctx, sx, sy, ex, ey, icol, ocol, target);
	}
	
	/**
	 * Creates and returns a box gradient. Box gradient is a feathered rounded rectangle, it is useful for rendering drop shadows or highlights for boxes.
	 * 
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to {@link #nvgFillPaint FillPaint} or {@link #nvgStrokePaint StrokePaint}.
	 * </p>
	 *
	 * @param x
	 *            the rectangle left coordinate
	 * @param y
	 *            the rectangle top coordinate
	 * @param w
	 *            the rectangle width
	 * @param h
	 *            the rectangle height
	 * @param r
	 *            the corner radius
	 * @param f
	 *            the feather value. Feather defines how blurry the border of the rectangle is.
	 * @param icol
	 *            the inner color
	 * @param ocol
	 *            the outer color
	 */
	public NVGPaint boxGradient(float x, float y, float w, float h, float r, float f, NVGColor icol, NVGColor ocol, NVGPaint target) {
		return nvgBoxGradient(this.ctx, x, y, w, h, r, f, icol, ocol, target);
	}
	
	/**
	 * Creates and returns a radial gradient.
	 * 
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to {@link #nvgFillPaint FillPaint} or {@link #nvgStrokePaint StrokePaint}.
	 * </p>
	 *
	 * @param cx
	 *            the X axis center coordinate
	 * @param cy
	 *            the Y axis center coordinate
	 * @param inr
	 *            the inner radius
	 * @param outr
	 *            the outer radius
	 * @param icol
	 *            the start color
	 * @param ocol
	 *            the end color
	 */
	public NVGPaint radialGradient(float cx, float cy, float inr, float outr, NVGColor icol, NVGColor ocol, NVGPaint target) {
		return nvgRadialGradient(this.ctx, cx, cy, inr, outr, icol, ocol, target);
	}
	
	/**
	 * Creates and returns an image patter.
	 * 
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to {@link #nvgFillPaint FillPaint} or {@link #nvgStrokePaint StrokePaint}.
	 * </p>
	 *
	 * @param ox
	 *            the image pattern left coordinate
	 * @param oy
	 *            the image pattern top coordinate
	 * @param ex
	 *            the image width
	 * @param ey
	 *            the image height
	 * @param angle
	 *            the rotation angle around the top-left corner
	 * @param image
	 *            the image to render
	 * @param alpha
	 *            the alpha value
	 */
	public NVGPaint imagePattern(float ox, float oy, float ex, float ey, float angle, int image, float alpha, NVGPaint target) {
		return nvgImagePattern(this.ctx, ox, oy, ex, ey, angle, image, alpha, target);
	}
	
	//
	// Text
	//
	// NanoVG allows you to load .ttf files and use the font to render text.
	//
	// The appearance of the text can be defined by setting the current text
	// style
	// and by specifying the fill color. Common text and font settings such as
	// font size, letter spacing and text align are supported. Font blur allows
	// you
	// to create simple text effects such as drop shadows.
	//
	// At render time the font face can be set based on the font handles or
	// name.
	//
	// Font measure functions return values in local space, the calculations are
	// carried in the same resolution as the final rendering. This is done
	// because
	// the text glyph positions are snapped to the nearest pixels sharp
	// rendering.
	//
	// The local space means that values are not rotated or scale as per the
	// current
	// transformation. For example if you set font size to 12, which would mean
	// that
	// line height is 16, then regardless of the current scaling and rotation,
	// the
	// returned line height is always 16. Some measures may vary because of the
	// scaling
	// since aforementioned pixel snapping.
	//
	// While this may sound a little odd, the setup allows you to always render
	// the
	// same way regardless of scaling. I.e. following works regardless of
	// scaling:
	//
	// const char* txt = "Text me up.";
	// nvgTextBounds(vg, x,y, txt, NULL, bounds);
	// nvgBeginPath(vg);
	// nvgRoundedRect(vg, bounds[0],bounds[1], bounds[2]-bounds[0],
	// bounds[3]-bounds[1]);
	// nvgFill(vg);
	//
	// Note: currently only solid color fill is supported for text.
	
	/**
	 * Creates font by loading it from the disk from specified file name.
	 *
	 * @param name
	 *            the font name
	 * @param filename
	 *            the font file name
	 *
	 * @return a handle to the font
	 */
	public int createFont(String name, String filename) {
		return nvgCreateFont(this.ctx, name, filename);
	}
	
	/**
	 * Creates font by loading it from the specified memory chunk.
	 * 
	 * <p>
	 * The memory chunk must remain valid for as long as the font is used by NanoVG.
	 * </p>
	 *
	 * @param name
	 *            the font name
	 * @param data
	 *            the font data
	 * @param freeData
	 *            true if the font data should be freed automatically, false otherwise
	 *
	 * @return a handle to the font
	 */
	public int createFontMem(String name, ByteBuffer data, boolean freeData) {
		return nvgCreateFontMem(this.ctx, name, data, freeData ? 1 : 0);
	}
	
	/**
	 * Finds a loaded font of specified name, and returns handle to it, or -1 if the font is not found.
	 *
	 * @param name
	 *            the font name
	 */
	public int findFont(String name) {
		return nvgFindFont(this.ctx, name);
	}
	
	/**
	 * Sets the font size of current text style.
	 *
	 * @param size
	 *            the font size to set
	 */
	public void fontSize(float size) {
		nvgFontSize(this.ctx, size);
	}
	
	/**
	 * Sets the blur of current text style.
	 *
	 * @param blur
	 *            the blur amount to set
	 */
	public void fontBlur(float blur) {
		nvgFontBlur(this.ctx, blur);
	}
	
	/**
	 * Sets the letter spacing of current text style.
	 *
	 * @param spacing
	 *            the letter spacing amount to set
	 */
	public void textLetterSpacing(float spacing) {
		nvgTextLetterSpacing(this.ctx, spacing);
	}
	
	/**
	 * Sets the proportional line height of current text style. The line height is specified as multiple of font size.
	 *
	 * @param lineHeight
	 *            the line height to set
	 */
	public void textLineHeight(float lineHeight) {
		nvgTextLineHeight(this.ctx, lineHeight);
	}
	
	/**
	 * Sets the text align of current text style.
	 *
	 * @param align
	 *            the text align to set. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #NVG_ALIGN_LEFT ALIGN_LEFT}</td>
	 *            <td>{@link #NVG_ALIGN_CENTER ALIGN_CENTER}</td>
	 *            <td>{@link #NVG_ALIGN_RIGHT ALIGN_RIGHT}</td>
	 *            <td>{@link #NVG_ALIGN_TOP ALIGN_TOP}</td>
	 *            <td>{@link #NVG_ALIGN_MIDDLE ALIGN_MIDDLE}</td>
	 *            <td>{@link #NVG_ALIGN_BOTTOM ALIGN_BOTTOM}</td>
	 *            <td>{@link #NVG_ALIGN_BASELINE ALIGN_BASELINE}</td>
	 *            </tr>
	 *            </table>
	 */
	public void textAlign(int align) {
		nvgTextAlign(this.ctx, align);
	}
	
	/**
	 * Sets the font face based on specified id of current text style.
	 *
	 * @param font
	 *            the font id
	 */
	public void fontFaceId(int font) {
		nvgFontFaceId(this.ctx, font);
	}
	
	/**
	 * Sets the font face based on specified name of current text style.
	 *
	 * @param font
	 *            the font name
	 */
	public void fontFace(String font) {
		nvgFontFace(this.ctx, font);
	}
	
	/**
	 * Draws text string at specified location. If {@code end} is specified only the sub-string up to the {@code end} is drawn.
	 *
	 * @param x
	 *            the text X axis coordinate
	 * @param y
	 *            the text Y axis coordinate
	 * @param string
	 *            the text string to draw
	 */
	public float text(float x, float y, String string) {
		return nvgText(this.ctx, x, y, string);
	}
	
	/**
	 * Draws multi-line text string at specified location wrapped at the specified width. If {@code end} is specified only the sub-string up to the {@code end} is drawn.
	 * 
	 * <p>
	 * White space is stripped at the beginning of the rows, the text is split at word boundaries or when new-line characters are encountered. Words longer than the max width are slit at nearest character (i.e. no hyphenation).
	 * </p>
	 *
	 * @param x
	 *            the text box X axis coordinate
	 * @param y
	 *            the text box Y axis coordinate
	 * @param breakRowWidth
	 *            the maximum row width
	 * @param string
	 *            the text string to draw
	 */
	public void textBox(float x, float y, float breakRowWidth, String string) {
		nvgTextBox(this.ctx, x, y, breakRowWidth, string);
	}
	
	/**
	 * Measures the specified text string.
	 * 
	 * <p>
	 * Parameter {@code bounds} should be a pointer to {@code float[4]}, if the bounding box of the text should be returned. The bounds value are {@code [xmin,ymin, xmax,ymax]}.
	 * </p>
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param x
	 *            the text X axis coordinate
	 * @param y
	 *            the text Y axis coordinate
	 * @param string
	 *            the text string to measure
	 * @param bounds
	 *            returns the bounding box of the text
	 *
	 * @return the horizontal advance of the measured text (i.e. where the next character should drawn)
	 */
	public float textBounds(float x, float y, String string, float[] bounds) {
		return nvgTextBounds(this.ctx, x, y, string, bounds);
	}
	
	/**
	 * Measures the specified multi-text string.
	 * 
	 * <p>
	 * Parameter {@code bounds} should be a pointer to {@code float[4]}, if the bounding box of the text should be returned. The bounds value are {@code [xmin,ymin, xmax,ymax]}.
	 * </p>
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param x
	 *            the text box X axis coordinate
	 * @param y
	 *            the text box Y axis coordinate
	 * @param breakRowWidth
	 *            the maximum row width
	 * @param string
	 *            the text string to measure
	 * @param bounds
	 *            returns the bounding box of the text box
	 */
	public void textBoxBounds(float x, float y, float breakRowWidth, String string, float[] bounds) {
		nvgTextBoxBounds(this.ctx, x, y, breakRowWidth, string, bounds);
	}
	
	public float[] textBoxBounds(float x, float y, float breakRowWidth, String string) {
		float[] bounds = new float[4];
		nvgTextBoxBounds(this.ctx, x, y, breakRowWidth, string, bounds);
		return bounds;
	}
	
	/**
	 * Calculates the glyph x positions of the specified text. If {@code end} is specified only the sub-string will be used.
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param x
	 *            the text X axis coordinate
	 * @param y
	 *            the text Y axis coordinate
	 * @param string
	 *            the text string to measure
	 * @param positions
	 *            returns the glyph x positions
	 */
	public int textGlyphPosition(float x, float y, String string, NVGGlyphPosition.Buffer positions) {
		return nvgTextGlyphPositions(this.ctx, x, y, string, positions);
	}
	
	/**
	 * Returns the vertical metrics based on the current text style.
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param ascender
	 *            the line ascend
	 * @param descender
	 *            the line descend
	 * @param lineh
	 *            the line height
	 */
	public void textMetrics(float[] ascender, float[] descender, float[] lineh) {
		nvgTextMetrics(this.ctx, ascender, descender, lineh);
	}
	
	/**
	 * Returns the vertical metrics based on the current text style.
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 */
	public NVGTextMetrics textMetrics() {
		nvgTextMetrics(this.ctx, this.ftemp_a, this.ftemp_b, this.ftemp_c);
		return new NVGTextMetrics(this.ftemp_a[0], this.ftemp_b[0], this.ftemp_c[0]);
	}
	
	/**
	 * Breaks the specified text into lines. If {@code end} is specified only the sub-string will be used.
	 * 
	 * <p>
	 * White space is stripped at the beginning of the rows, the text is split at word boundaries or when new-line characters are encountered. Words longer than the max width are slit at nearest character (i.e. no hyphenation).
	 * </p>
	 *
	 * @param ctx
	 *            the NanoVG context
	 * @param string
	 *            the text string to measure
	 * @param breakRowWidth
	 *            the maximum row width
	 * @param rows
	 *            returns the text rows
	 */
	public int textBreakLines(String string, float breakRowWidth, NVGTextRow.Buffer rows, int maxRows) {
		return nvgTextBreakLines(this.ctx, string, breakRowWidth, rows);
	}
	
	// Global customs
	public void strokeLine(float sx, float sy, float ex, float ey) {
		this.save();
		this.beginPath();
		this.moveTo(sx, sy);
		this.lineTo(ex, ey);
		this.stroke();
		this.restore();
	}
	
	public void fillRect(float x, float y, float w, float h) {
		this.save();
		this.beginPath();
		this.rect(x, y, w, h);
		this.fill();
		this.restore();
	}
	
	public void strokeRect(float x, float y, float w, float h) {
		this.save();
		this.beginPath();
		this.rect(x, y, w, h);
		this.stroke();
		this.restore();
	}
	
	public void fillRoundedRect(float x, float y, float w, float h, float r) {
		this.save();
		this.beginPath();
		this.roundedRect(x, y, w, h, r);
		this.fill();
		this.restore();
	}
	
	public void strokeRoundedRect(float x, float y, float w, float h, float r) {
		this.save();
		this.beginPath();
		this.roundedRect(x, y, w, h, r);
		this.stroke();
		this.restore();
	}
	
	// Dispose
	
	/**
	 * Deletes the NanoVG context.
	 */
	public void dispose() {
		NanoVGGL3.nvgDelete(this.ctx);
	}
	
	/**
	 * Gets the singleton instance
	 * 
	 * @return The singleton context
	 */
	public static NVGContext get() {
		if(singlectx == null) create(true);
		
		return singlectx;
	}
	
	/**
	 * Creates the NanoVG context singleton
	 * 
	 * @param antialias
	 *            If antialiasing should be used
	 * @return The created context
	 */
	public static NVGContext create(boolean antialias) {
		int flags = 0;
		
		if(DEBUG) flags |= NVG_DEBUG;
		
		if(antialias) flags |= NVG_ANTIALIAS;
		
		flags |= NVG_STENCIL_STROKES;
		
		return singlectx = new NVGContext(flags);
	}
	
	/**
	 * Deletes the singleton context.
	 */
	public static void delete() {
		if(singlectx != null) {
			singlectx.dispose();
			singlectx = null;
		}
	}
	
	//
	// Color utils
	//
	// Colors in NanoVG are stored as unsigned ints in ABGR format.
	
	/**
	 * Returns a color value from red, green, blue values. Alpha will be set to 255 (1.0f).
	 *
	 * @param r
	 *            the red value
	 * @param g
	 *            the green value
	 * @param b
	 *            the blue value
	 */
	public static NVGColor rgb(byte r, byte g, byte b) {
		return nvgRGB(r, g, b, NVGColor.create());
	}
	
	/**
	 * Returns a color value from red, green, blue values. Alpha will be set to 1.0f.
	 *
	 * @param r
	 *            the red value
	 * @param g
	 *            the green value
	 * @param b
	 *            the blue value
	 */
	public static NVGColor rgbf(float r, float g, float b) {
		return nvgRGBf(r, g, b, NVGColor.create());
	}
	
	/**
	 * Returns a color value from red, green, blue and alpha values.
	 *
	 * @param r
	 *            the red value
	 * @param g
	 *            the green value
	 * @param b
	 *            the blue value
	 * @param a
	 *            the alpha value
	 */
	public static NVGColor rgba(byte r, byte g, byte b, byte a) {
		return nvgRGBA(r, g, b, a, NVGColor.create());
	}
	
	/**
	 * Returns a color value from red, green, blue and alpha values.
	 *
	 * @param r
	 *            the red value
	 * @param g
	 *            the green value
	 * @param b
	 *            the blue value
	 * @param a
	 *            the alpha value
	 */
	public static NVGColor rgbaf(float r, float g, float b, float a) {
		return nvgRGBAf(r, g, b, a, NVGColor.create());
	}
	
	/**
	 * Linearly interpolates from color {@code c0} to {@code c1}, and returns resulting color value.
	 *
	 * @param c0
	 *            the first color
	 * @param c1
	 *            the second color
	 * @param u
	 *            the interpolation factor
	 */
	public static NVGColor lerpRGBA(NVGColor c0, NVGColor c1, float u) {
		return nvgLerpRGBA(c0, c1, u, NVGColor.create());
	}
	
	/**
	 * Sets transparency of a color value.
	 *
	 * @param c0
	 *            the color
	 * @param a
	 *            the alpha value
	 */
	public static NVGColor transRGBA(NVGColor c0, byte a) {
		return nvgTransRGBA(c0, a, NVGColor.create());
	}
	
	/**
	 * Sets transparency of a color value.
	 *
	 * @param c0
	 *            the color
	 * @param a
	 *            the alpha value
	 */
	public static NVGColor transRGBAf(NVGColor c0, float a) {
		return nvgTransRGBAf(c0, a, NVGColor.create());
	}
	
	/**
	 * Returns color value specified by hue, saturation and lightness.
	 * 
	 * <p>
	 * HSL values are all in range {@code [0..1]}, alpha will be set to 255.
	 * </p>
	 *
	 * @param h
	 *            the hue value
	 * @param s
	 *            the saturation value
	 * @param l
	 *            the lightness value
	 */
	public static NVGColor hsl(float h, float s, float l) {
		return nvgHSL(h, s, l, NVGColor.create());
	}
	
	/**
	 * Returns color value specified by hue, saturation and lightness and alpha.
	 * 
	 * <p>
	 * HSL values are all in range {@code [0..1]}, alpha in range {@code [0..255]}
	 * </p>
	 *
	 * @param h
	 *            the hue value
	 * @param s
	 *            the saturation value
	 * @param l
	 *            the lightness value
	 * @param a
	 *            the alpha value
	 */
	public static NVGColor hsla(float h, float s, float l, byte a) {
		return nvgHSLA(h, s, l, a, NVGColor.create());
	}
}
