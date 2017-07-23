struct Clip {
	bool enabled;
	
	mat3 xform;
	vec2 extent;
};

uniform sampler2D fontTexture;
uniform Clip clip;

in vec2 pass_uv;
in vec2 pass_clipPos;
in vec4 pass_letterDiffuseColorAlpha;

out vec4 out_color;

bool clipMask(vec2 p) {
	return abs(p.x) < clip.extent.x && abs(p.y) < clip.extent.y;
}

void main() {
	if(clip.enabled && !clipMask(pass_clipPos)) discard;
	
	out_color = vec4(pass_letterDiffuseColorAlpha.rgb, pass_letterDiffuseColorAlpha.a * texture2D(fontTexture, pass_uv).r);
}
