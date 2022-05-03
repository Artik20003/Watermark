import java.awt.Color
import java.awt.image.BufferedImage
const val HEIGHT = 200
const val WIDTH = 200
const val PADDING = 50
fun drawStrings(): BufferedImage {
    val bi = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
    val gr = bi.createGraphics()
    val colors = mutableListOf<Color>(Color.RED, Color.GREEN, Color.BLUE)
    for (i in colors.indices) {
        gr.color = colors[i]
        gr.drawString("Hello, images!", PADDING + i, PADDING + i)
    }
    return bi

}