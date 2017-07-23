uniform sampler2D color;
uniform float gamma;

in vec2 pass_quad_uv;

out vec4 out_color;

void main(){
	vec4 trgba = texture(color, pass_quad_uv).xyzw;
	out_color = vec4(pow(trgba.rgb, vec3(1.0 / gamma)), trgba.a);
}
