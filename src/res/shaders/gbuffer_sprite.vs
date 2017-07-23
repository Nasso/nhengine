// Primitives
layout (location = 0) in vec2 position;

// Per instance
layout (location = 1) in vec3 quickTransformWHDepth;
layout (location = 2) in vec4 spriteSubRegion;
layout (location = 3) in vec4 diffuseColorAlpha;
layout (location = 4) in mat4 model;
// layout loc 4, 5, 6, 7 for the matrix
layout (location = 8) in vec2 gridXYPositionOffset;

uniform bool isTileMap;

uniform mat4 projView;
uniform mat4 tileMapModel;

out vec4 pass_diffuseColorAlpha;
out vec2 pass_uv;

void main() {
	pass_diffuseColorAlpha = diffuseColorAlpha;
	
	pass_uv = position * spriteSubRegion.zw + spriteSubRegion.xy;
	
	if(isTileMap) {
		gl_Position = projView * tileMapModel * model * vec4(position * quickTransformWHDepth.xy + gridXYPositionOffset.xy, quickTransformWHDepth.z, 1.0);
	} else {
		gl_Position = projView * model * vec4(position * quickTransformWHDepth.xy + gridXYPositionOffset.xy, quickTransformWHDepth.z, 1.0);
	}
}
