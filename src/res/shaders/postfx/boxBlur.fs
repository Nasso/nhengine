uniform sampler2D color;
uniform ivec2 textureDim;
uniform int size;

in vec2 pass_quad_uv;

out vec4 out_color;

void main(){
	vec4 blurred = vec4(0.0);
	
	float totalSamples = pow(size * 2 + 1, 2);
	
	for(int i = -size; i <= size; i++){
		for(int j = -size; j <= size; j++){
			blurred += textureOffset(color,
				pass_quad_uv,
				ivec2(i, j)).xyzw / totalSamples;
		}
	}
	
	out_color = blurred.xyzw;
}
