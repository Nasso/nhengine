struct Clip {
	bool enabled;
	
	mat3 xform;
	vec2 extent;
};

// Primitives
layout (location = 0) in vec2 position;

// Per instance
layout (location = 1) in vec4 positionStartXYEndXY;
layout (location = 2) in vec4 textureStartSTEndST;
layout (location = 3) in vec4 letterDiffuseColorAlpha;

uniform mat4 projViewModel;
uniform mat3 projViewModel3;
uniform vec2 scaleXY;
uniform bool cvsMode;
uniform Clip clip;

out vec2 pass_uv;
out vec2 pass_clipPos;
out vec4 pass_letterDiffuseColorAlpha;

void main() {
	pass_uv = position * (textureStartSTEndST.zw - textureStartSTEndST.xy) + textureStartSTEndST.xy;
	pass_letterDiffuseColorAlpha = letterDiffuseColorAlpha;
	
	vec2 transformedPos = position;
	transformedPos *= positionStartXYEndXY.zw - positionStartXYEndXY.xy;
	transformedPos += positionStartXYEndXY.xy;
	
	if(cvsMode) {
		vec3 transformedPos3 = projViewModel3 * vec3(transformedPos, 1.0);
		
		// Clip transform
		if(clip.enabled) pass_clipPos = (clip.xform * vec3(transformedPos3.xy, 1.0)).xy;
		
		// Canvas Scaling
		transformedPos3 -= vec3(scaleXY / 2f, 1.0);
		transformedPos3 *= vec3(2.0 / scaleXY.x, -2.0 / scaleXY.y, 1.0);
		
		gl_Position = vec4(transformedPos3, 1.0);
	} else {
		gl_Position = projViewModel * vec4(transformedPos, 0.0, 1.0);
	}
}
