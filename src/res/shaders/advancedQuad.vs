uniform mat3 transform;
uniform vec4 sourceXYWH;
uniform vec4 destXYWH;

#ifdef CLIPPING
	struct Clip {
		bool enabled;
		
		mat3 xform;
		vec2 extent;
	};
	
	uniform Clip clip;
#endif

uniform vec2 scaleXY;

in vec2 position_quad;

out vec2 pass_quad_uv;

#ifdef CLIPPING
	out vec2 pass_quad_clip_pos;
#endif

void main() {
	vec2 quadPos = position_quad;
	vec2 quadUV = quadPos;
	
	quadUV.xy *= sourceXYWH.zw;
	quadUV.xy += sourceXYWH.xy;
	
	pass_quad_uv = quadUV;
	
	quadPos.xy *= destXYWH.zw;
	quadPos.xy += destXYWH.xy;
	
	vec3 quadPos3 = transform * vec3(quadPos, 1.0);
	
#ifdef CLIPPING
	// Clip transform
	if(clip.enabled) {
		pass_quad_clip_pos = (clip.xform * vec3(quadPos3.xy, 1.0)).xy;
	}
#endif
	
	// Canvas Scaling
	quadPos3 -= vec3(scaleXY / 2.0, 1.0);
	quadPos3 *= vec3(2.0 / scaleXY.x, -2.0 / scaleXY.y, 1.0);
	
	gl_Position = vec4(quadPos3, 1.0);
}
