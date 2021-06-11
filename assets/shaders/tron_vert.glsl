#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec3 norm;


//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view;
uniform mat4 projection;

out struct VertexData
{
    vec3 position;
    vec3 norm;

} vertexData;

void main()
{

    vec4 pos = projection * view * model_matrix * vec4(position, 1.0f);
    gl_Position = pos;
    vertexData.position = pos.xyz;

    vec4 nor = view * model_matrix * vec4(norm,0.0f);
    vertexData.norm = nor.xyz;
}
