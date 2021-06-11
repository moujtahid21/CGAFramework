package cga.exercise.components.shader

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Fabian on 16.09.2017.
 */
class ShaderProgram(vertexShaderPath: String, fragmentShaderPath: String) {
    private var programID: Int = 0
    //Matrix buffers for setting matrix uniforms. Prevents allocation for each uniform
    private val m4x4buf: FloatBuffer = BufferUtils.createFloatBuffer(16)
    /**
     * Sets the active shader program of the OpenGL render pipeline to this shader
     * if this isn't already the currently active shader
     */
    fun use() {
        val curprog = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
        if (curprog != programID) GL20.glUseProgram(programID) //Programmobjekt aktuell
    }

    /**
     * Frees the allocated OpenGL objects
     */
    fun cleanup() {
        GL20.glDeleteProgram(programID)
    }

    //setUniform() functions are added later during the course
    // float vector uniforms
    /**
     * Sets a single float uniform
     * @param name  Name of the uniform variable in the shader
     * @param value Value
     * @return returns false if the uniform was not found in the shader
     */
    fun setUniform(name: String, value: Matrix4f, transpose : Boolean): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1)
        {
            //glUniform modifiziert den Wert einer Uniform-Variable oder eines Uniform-Arrays.
                // Der Ablageort zu verändernden Uniform-Variable  wird mit location angegeben,
                    // das einen von getUniformLocation zurückgelieferten Wert bekommt. Dabei arbeitet
                        // diese Methode noch mit dem Programmobjekt, dass durch den Aufruf von glUseProgram aktuell ist.
            GL20.glUniformMatrix4fv(loc,transpose,value.get(m4x4buf))
            //glUniformMatrix4fv besitzt bei uns 3 Parameter:
            // @param loc -> Gibt den Speicherort der Uniform-Variable an, die verändert werden soll
            // @param transpose -> Gibt an, ob die Matrix transponiert ist werden soll, wenn die Werte in die Uniform-Variable geladen werden.
            // @param value -> Gibt einen Pointer auf eine 4x4 Matrix, die verwendet wird, um die gegebene Uniform-Variable zu aktualisieren.
            return true
        }
        return false
    }


    /**
     * Creates a shader object from vertex and fragment shader paths
     * @param vertexShaderPath      vertex shader path
     * @param fragmentShaderPath    fragment shader path
     * @throws Exception if shader compilation failed, an exception is thrown
     */
    init {
        val vPath = Paths.get(vertexShaderPath)
        val fPath = Paths.get(fragmentShaderPath)
        val vSource = String(Files.readAllBytes(vPath))
        val fSource = String(Files.readAllBytes(fPath))
        val vShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        if (vShader == 0) throw Exception("Vertex shader object couldn't be created.")
        val fShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        if (fShader == 0) {
            GL20.glDeleteShader(vShader)
            throw Exception("Fragment shader object couldn't be created.")
        }
        GL20.glShaderSource(vShader, vSource)
        GL20.glShaderSource(fShader, fSource)
        GL20.glCompileShader(vShader)
        if (GL20.glGetShaderi(vShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetShaderInfoLog(vShader)
            GL20.glDeleteShader(fShader)
            GL20.glDeleteShader(vShader)
            throw Exception("Vertex shader compilation failed:\n$log")
        }
        GL20.glCompileShader(fShader)
        if (GL20.glGetShaderi(fShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetShaderInfoLog(fShader)
            GL20.glDeleteShader(fShader)
            GL20.glDeleteShader(vShader)
            throw Exception("Fragment shader compilation failed:\n$log")
        }
        programID = GL20.glCreateProgram()
        if (programID == 0) {
            GL20.glDeleteShader(vShader)
            GL20.glDeleteShader(fShader)
            throw Exception("Program object creation failed.")
        }
        GL20.glAttachShader(programID, vShader)
        GL20.glAttachShader(programID, fShader)
        GL20.glLinkProgram(programID)
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetProgramInfoLog(programID)
            GL20.glDetachShader(programID, vShader)
            GL20.glDetachShader(programID, fShader)
            GL20.glDeleteShader(vShader)
            GL20.glDeleteShader(fShader)
            throw Exception("Program linking failed:\n$log")
        }
        GL20.glDetachShader(programID, vShader)
        GL20.glDetachShader(programID, fShader)
        GL20.glDeleteShader(vShader)
        GL20.glDeleteShader(fShader)
    }
}


