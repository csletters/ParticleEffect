precision mediump float;
uniform sampler2D uTexture;
varying vec4 vStarColor;
void main() {

	gl_FragColor =  texture2D(uTexture, gl_PointCoord) * vStarColor;
}