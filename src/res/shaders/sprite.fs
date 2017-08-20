uniform sampler2D diffuseTexture;

in vec4 pass_diffuseColorAlpha;
in vec2 pass_uv;

out vec4 out_color;

void main() {
	out_color = vec4(texture2D(diffuseTexture, pass_uv) * pass_diffuseColorAlpha);
}
