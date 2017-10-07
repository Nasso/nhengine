// Primitives
layout (location = 0) in vec2 position;

// Per instance
layout (location = 1) in vec4 tileSetLocationMapPosition; // vec2(tile image position in tile-set) + vec2(tile position on map)

uniform ivec2 tilesetSize; // ~~ col and row count
uniform vec3 tileWidthHeightDepth; // ~~ size of a tile in pixels
uniform ivec2 tileSetWidthHeight;
uniform float opacity;
uniform mat4 model;
uniform bool isometric;

uniform mat4 projView;

out vec4 pass_diffuseColorAlpha;
out vec2 pass_uv;

void main() {
	pass_diffuseColorAlpha = vec4(1.0, 1.0, 1.0, opacity);
	
	pass_uv = (position + tileSetLocationMapPosition.xy) / tilesetSize.xy;
	
	vec2 pos;
	
	if(isometric) {
		pos = position * tileWidthHeightDepth.xy + vec2(
			(tileSetLocationMapPosition.z - tileSetLocationMapPosition.w) * tileSetWidthHeight.x * 0.5 - tileSetLocationMapPosition.z,
			(tileSetLocationMapPosition.z + tileSetLocationMapPosition.w) * tileSetWidthHeight.y * 0.5 - tileSetLocationMapPosition.w
		);
	} else pos = tileWidthHeightDepth.xy * (position + tileSetLocationMapPosition.zw);
	
	gl_Position = projView * model * vec4(pos, tileWidthHeightDepth.z, 1.0);
}
