uniform sampler2D color;
uniform float reduceMin;
uniform float reduceMul;
uniform float spanMax;
uniform ivec2 resolution;

in vec2 pass_quad_uv;

out vec4 out_color;

void main(){
	vec2 inverse_resolution = 1.0 / resolution;
	
	// Sample the texture on the corners
	// # #
	//  X
	// # #
	
	// Calculate their intensity based on some weights
	vec3 luma = vec3(0.299, 0.587, 0.114);
	float lumaM  = dot(texture(color,  pass_quad_uv).xyz,  luma);
	float lumaNW = dot(textureOffset(color, pass_quad_uv, ivec2(-1, -1)).xyz, luma);
	float lumaNE = dot(textureOffset(color, pass_quad_uv, ivec2(1, -1)).xyz, luma);
	float lumaSW = dot(textureOffset(color, pass_quad_uv, ivec2(-1, 1)).xyz, luma);
	float lumaSE = dot(textureOffset(color, pass_quad_uv, ivec2(1, 1)).xyz, luma);
	
	// Gets the direction of the intensity
	// If it's more intense on the up than on the down
	// And if it's more intense on the left or on the right
	// The values are the direction based on their difference
	vec2 dir = vec2(-((lumaNW + lumaNE) - (lumaSW + lumaSE)), ((lumaNW + lumaSW) - (lumaNE + lumaSE)));
	
	// Reduce amount (> reduceMin)
	dir = min(vec2(spanMax, spanMax), max(vec2(-spanMax, -spanMax), dir * 1.0 / (min(abs(dir.x), abs(dir.y)) + max((lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * reduceMul), reduceMin)))) * inverse_resolution;
	
	vec4 rgbaA = 0.5 * (texture(color, pass_quad_uv + dir * (1.0/3.0 - 0.5)).xyzw + texture(color, pass_quad_uv + dir * (2.0/3.0 - 0.5)).xyzw);
	vec4 rgbaB = rgbaA * 0.5 + 0.25 * (texture(color, pass_quad_uv + dir * -0.5).xyzw + texture(color, pass_quad_uv + dir * 0.5).xyzw);
	float lumaB = dot(rgbaB.xyz, luma);
	
	if((lumaB < min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)))) || (lumaB > max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE))))) {
		out_color = rgbaA;
	} else {
		out_color = rgbaB;
	}
}
