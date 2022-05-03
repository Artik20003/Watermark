import java.awt.Color
import java.awt.image.BufferedImage  
const val  WIDTH = 200
const val  HEIGHT = 200

fun drawLines(): BufferedImage {
    val bi = BufferedImage (200, 200, BufferedImage.TYPE_INT_RGB)
    val gr = bi.createGraphics()
    //gr.color = Color.BLACK
    //gr.fillRect(0,0, WIDTH, HEIGHT)
    gr.color = Color.RED
    gr.drawLine(0,0, WIDTH, HEIGHT)
    gr.color = Color.GREEN
    gr.drawLine(HEIGHT,0, 0,WIDTH)
    return bi
}