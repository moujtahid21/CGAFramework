package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import java.lang.Math.PI


class TronCamera(
        val fov : Float = Math.toRadians(90.0).toFloat(), //field of view
        val aspectRatio : Float = 16.0f/9.0f, //aspect ration
        val nearPlane : Float = 0.1f, // near Plane
        val farPlane : Float = 100.0f, // far Plane
        val cameraTarget: Vector3f,
        parent: Transformable
) : ICamera, Transformable(parent = parent)
{


    override fun bind(shader: ShaderProgram)
    {
        shader.setUniform("view",getCalculateViewMatrix(),false)
        shader.setUniform("projection",getCalculateProjectionMatrix(), false)
    }

    override fun getCalculateProjectionMatrix(): Matrix4f
    {
        val projectionMatrix = Matrix4f()

        projectionMatrix.perspective(fov,aspectRatio,nearPlane,farPlane)

       return projectionMatrix
    }

    override fun getCalculateViewMatrix(): Matrix4f
    {
        val viewMatrix = Matrix4f()

        viewMatrix.lookAt(getWorldPosition(), cameraTarget, getWorldYAxis())

        return viewMatrix
    }
}