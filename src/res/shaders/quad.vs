uniform mat4 projView;
uniform mat4 model;
uniform bool flipY;

in vec2 position_quad;

out vec2 pass_quad_uv;

void main(){
	pass_quad_uv = position_quad;
	
	if(flipY) pass_quad_uv.y = 1 - pass_quad_uv.y;
	
	gl_Position = projView * model * vec4(position_quad, 0.0, 1.0);
}
