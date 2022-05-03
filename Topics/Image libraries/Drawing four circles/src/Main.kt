import java.awt.Color
import java.awt.image.BufferedImage  

fun drawCircles(): BufferedImage {
    val bi = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
    val gr = bi.createGraphics()
    val colors = mutableListOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)
    var index = 0
    for (x in mutableListOf<Int>(50, 75)){
        for (y in mutableListOf<Int>(50, 75)) {
            gr.color = colors[index++]
            gr.drawOval(x, y, 100, 100)
        }
    }
    return bi
}
