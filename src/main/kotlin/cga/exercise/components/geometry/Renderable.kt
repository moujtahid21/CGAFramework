package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

//meshes -> MutableList von Mesh wird übergegebn, damit diese dann über Renderable gerendert werden können

//**2.3**//
class Renderable(val meshes : MutableList<Mesh>): IRenderable, Transformable()
{
    override fun render(shaderProgram: ShaderProgram)
    {
        /**2.3.1**/
        shaderProgram.setUniform("model_matrix",getWorldModelMatrix(),false)
        meshes.forEach{ it.render() }
    }
}