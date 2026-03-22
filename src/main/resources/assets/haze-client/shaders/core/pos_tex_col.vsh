#version 330 core

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;

out vec2 texCoord;
out vec2 fragCoord;
out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texCoord = gl_Position.xy * 0.5 + 0.5;
    fragCoord = UV0 * 2.0 - 1.0;
    vertexColor = Color;
}