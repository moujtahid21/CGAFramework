package cga.exercise.components.geometry

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30.*


/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {

        indexcount = indexdata.size

        // generate ID
        vao = glGenVertexArrays()
        vbo = glGenBuffers()
        ibo = glGenBuffers()

        // binding & upload
        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW)

        for((i,attribute) in attributes.withIndex())
        {
            glVertexAttribPointer(i,attribute.n,attribute.type,false, attribute.stride,attribute.offset.toLong())
            glEnableVertexAttribArray(i)
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ibo)
        GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexdata, GL_STATIC_DRAW)
    }

    /**
     * renders the mesh
     */
    fun render()
    {

        glBindVertexArray(vao)
        glDrawElements(GL15.GL_TRIANGLES,indexcount,GL15.GL_UNSIGNED_INT,0)
        glBindVertexArray(0)

    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) glDeleteBuffers(ibo)
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }
}

