uniform sampler2D color;
uniform bool horizontal;
uniform int textureLength;
uniform float size;
uniform float kernel[11] = float[] (0.022657, 0.046108, 0.080127, 0.118904, 0.150677, 0.163053, 0.150677, 0.118904, 0.080127, 0.046108, 0.022657);

in vec2 pass_quad_uv;

out vec4 out_color;

void main(){
	vec2 pixelSize = vec2((1.0 / textureLength) * size);
	vec4 blurred = vec4(0.0);
	
	if(horizontal){
		pixelSize.y = 0;
	}else{
		pixelSize.x = 0;
	}
	
	for(int i = -5; i <= 5; i++){
		blurred += texture(color, pass_quad_uv + pixelSize * i).xyzw * kernel[i+5];
	}
	
	out_color = blurred.xyzw;
}
