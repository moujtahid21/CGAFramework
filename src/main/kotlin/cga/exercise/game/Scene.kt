package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.OBJLoader
import org.lwjgl.opengl.GL11.*
import org.joml.*
import org.lwjgl.glfw.GLFW


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    private val groundMesh : Mesh
    private val sphereMesh : Mesh
    private val ground : Renderable
    private val sphere : Renderable
    private val camera : TronCamera


    //scene setup
    init {

        //todo: Shaderprogramm erstellen
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")


        //initial opengl state
       //Background black
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glEnable(GL_CULL_FACE); GLError.checkThrow() //Face culling aktiviert
        glFrontFace(GL_CCW); GLError.checkThrow() //Clock-wise culling an
        glCullFace(GL_BACK); GLError.checkThrow() //Face Culling für Back-Faces an
        glEnable(GL_DEPTH_TEST); GLError.checkThrow() //Tiefen Test aktiviert
        glDepthFunc(GL_LESS); GLError.checkThrow() //legt fest wann ein Fragment den
        // Tiefentest im DEPTH_BUFFER besteht
        // @param GL_LESS : Neue Fragmente bestehen den Vergleich, wenn sie einen geringeren Tiefenwert haben.

        //Shaderprogramm
        staticShader.use()


        //sphere.obj laden
        val sphereOBJ  = OBJLoader.loadOBJ("assets/models/sphere.obj", false, false)
        val sphereMeshOBJ = sphereOBJ.objects.first().meshes.first()
        val sphereAttrib = arrayOf(
            VertexAttribute(3, GL_FLOAT,32,0),
            VertexAttribute(2, GL_FLOAT,32,12),
            VertexAttribute(3, GL_FLOAT,32,20))
        sphereMesh = Mesh(sphereMeshOBJ.vertexData,sphereMeshOBJ.indexData,sphereAttrib)
        sphere = Renderable(mutableListOf(sphereMesh))

        //ground.obj laden
        val groundOBJ = OBJLoader.loadOBJ("assets/models/ground.obj", false, false)
        val groundMeshOBJ  = groundOBJ.objects.first().meshes.first()
        val groundAttrib = arrayOf(
            VertexAttribute(3, GL_FLOAT,32,0),
            VertexAttribute(3, GL_FLOAT,32,12),
            VertexAttribute(3, GL_FLOAT,32,20))
        groundMesh = Mesh(groundMeshOBJ.vertexData,groundMeshOBJ.indexData,groundAttrib)
        ground = Renderable(mutableListOf(groundMesh))

        //Camera integration
        camera = TronCamera(cameraTarget = Vector3f(0f), parent = sphere)
        camera.rotateLocal(Math.toRadians(-20.0).toFloat(),0f,0f)
        camera.translateLocal(Vector3f(0f,0f,4.0f))


    }

    fun render(dt: Float, t: Float)
    {
        //glClear leert die im Parameter festgelegten Buffer, indem sie mit einem Leerwert gefüllt werden.
        //@param mask-> kann eine oder mehrere (mit dem bitweisen 'or' verknüpfte) Konstanten enthalten:
        //GL_COLOR_BUFFER_BIT, GL_DEPTH_BUFFER_BIT, GL_ACCUM_BUFFER_BIT und GL_STENCIL_BUFFER_BIT
        //Die Leerwerte der einzelnen Buffer werden mit glClearColor bzw. glClearIndex, glClearDepth, glClearStencil und
        //glClearAccum festgelegt. Und mithilfe des glDrawBuffer Befehls können mehrere ColorBuffer gleichzeitig geleert werden.
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.bind(staticShader)
        ground.render(staticShader)
        sphere.render(staticShader)


    }

    fun update(dt: Float, t: Float)
    {
        if(window.getKeyState(GLFW.GLFW_KEY_W))
        {
            sphere.translateLocal(Vector3f(0f,0f,-1.0f*dt))
        }
        if(window.getKeyState(GLFW.GLFW_KEY_S))
        {
            sphere.translateLocal(Vector3f(0f,0f,1.0f*dt))
        }
        if(window.getKeyState(GLFW.GLFW_KEY_A))
        {
            sphere.rotateLocal(0f,1.5f*dt,0f)
        }
        if(window.getKeyState(GLFW.GLFW_KEY_D))
        {
            sphere.rotateLocal(0f,-1.5f*dt,0f)
        }

    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}


    fun cleanup() {}
}
