// Primitives
layout (location = 0) in vec2 position;

uniform vec3 widthHeightDepth;
uniform vec4 spriteSubRegion;
uniform vec4 diffuseColorAlpha;
uniform mat4 model;

uniform mat4 projView;

out vec4 pass_diffuseColorAlpha;
out vec2 pass_uv;

void main() {
	pass_diffuseColorAlpha = diffuseColorAlpha;
	
	pass_uv = position * spriteSubRegion.zw + spriteSubRegion.xy;
	
	gl_Position = projView * model * vec4(position * widthHeightDepth.xy, widthHeightDepth.z, 1.0);
}
