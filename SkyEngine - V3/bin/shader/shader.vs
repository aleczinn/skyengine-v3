#version 420

layout(location=0) in vec2 vertices;
layout (location=1) in vec4 color;

out vec4 outColor;
uniform mat4 projectionMatrix;

void main() {
	gl_Position = projectionMatrix * vec4(vertices, 0.0, 1.0);
	outColor = color;
}