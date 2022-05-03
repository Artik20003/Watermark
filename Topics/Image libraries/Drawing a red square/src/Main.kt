import java.awt.Color
import java.awt.image.BufferedImage  

fun drawSquare(): BufferedImage {
  val bi = BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
  val gr = bi.createGraphics()
  gr.color = Color.RED
  gr.drawRect(100,100,300,300)
  return bi

}