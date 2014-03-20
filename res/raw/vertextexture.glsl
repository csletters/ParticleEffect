attribute vec4 aPosition;
attribute vec4 aColor;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform float time;
      
varying vec4 starColor;
void main() {

	vec4 newVertex = aPosition;
	
	newVertex.z += time;
	newVertex.z = fract(newVertex.z);
	
	float size = (20.0 * newVertex.z* newVertex.z);
	starColor = smoothstep(1.0, 7.0, size)* aColor;
	
	newVertex.z = (999.9 * newVertex.z) - 1000.0;
	gl_Position = projection*view*model*newVertex;
	gl_PointSize = size;
}