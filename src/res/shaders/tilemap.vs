// Primitives
layout (location = 0) in vec2 position;

// Per instance
layout (location = 1) in vec4 tileSetLocationMapPosition; // vec2(tile image position in tile-set) + vec2(tile position on map)

uniform ivec2 tilesetSize; // ~~ col and row count
uniform vec3 tileWidthHeightDepth; // ~~ size of a tile in pixels
uniform float opacity;
uniform mat4 model;
uniform bool isometric;

uniform mat4 projView;

out vec4 pass_diffuseColorAlpha;
out vec2 pass_uv;

void main() {
	pass_diffuseColorAlpha = vec4(1, 1, 1, opacity);
	
	pass_uv = (1.0 / tilesetSize.xy) * (position + tileSetLocationMapPosition.xy);
	
	vec2 pos;
	
	if(isometric) {
		pos = position * tileWidthHeightDepth.xy + vec2(
			(tileSetLocationMapPosition.z - tileSetLocationMapPosition.w) * tileWidthHeightDepth.x * 0.5 - tileSetLocationMapPosition.z,
			(tileSetLocationMapPosition.z + tileSetLocationMapPosition.w) * tileWidthHeightDepth.y * 0.5 - tileSetLocationMapPosition.w
		);
	} else pos = tileWidthHeightDepth.xy * (position + tileSetLocationMapPosition.zw);
	
	gl_Position = projView * model * vec4(pos, tileWidthHeightDepth.z, 1.0);
}
