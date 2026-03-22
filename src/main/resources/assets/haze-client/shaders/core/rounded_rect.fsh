#version 330

in vec2 texCoord;
in vec2 fragCoord;
in vec4 vertexColor;

layout(std140) uniform Rect {
    vec2 uRect;
    vec4 uRadius;
    float uBorder;
    vec4 uBorderColor;
};

out vec4 fragColor;

float sdRoundedBox(in vec2 p, in vec2 b, in vec4 r) {
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x  = (p.y > 0.0) ? r.x  : r.y;
    vec2 q = abs(p) - b + r.x;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r.x;
}

void main() {
    vec2 p = fragCoord * uRect;
    float dist = sdRoundedBox(p, uRect, uRadius);

    float smoothAlpha = 1.0 - smoothstep(-1.0, 1.0, dist);

    vec4 alphadBorderColor = mix(vertexColor, uBorderColor, uBorderColor.a);

    float t = smoothstep(-uBorder - 1., -uBorder, dist);
    vec4 borderMixColor = mix(vertexColor, alphadBorderColor, t);

    fragColor = vec4(borderMixColor.rgb, smoothAlpha);
}