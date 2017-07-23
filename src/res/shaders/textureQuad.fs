uniform sampler2D color;
uniform float globalAlpha;

#ifdef CLIPPING
	struct Clip {
		bool enabled;
		
		mat3 xform;
		vec2 extent;
	};

	uniform Clip clip;

	in vec2 pass_quad_clip_pos;
#endif

in vec2 pass_quad_uv;

out vec4 out_color;

#ifdef CLIPPING
	bool clipMask(vec2 p) {
		return abs(p.x) < clip.extent.x && abs(p.y) < clip.extent.y;
	}
#endif

void main() {
#ifdef CLIPPING
	if(clip.enabled && !clipMask(pass_quad_clip_pos)) discard;
#endif
	
	out_color = texture(color, pass_quad_uv) * vec4(1, 1, 1, globalAlpha);
}
