#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec3 norm;

} vertexData;

//fragment shader output
out vec4 color;


void main()
{

    vec3 normalColor = abs(normalize(vertexData.norm));

    color = vec4(normalColor.xyz,1.0f);

}