package watermark
import org.w3c.dom.ranges.RangeException
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

enum class PositionMethod { SINGLE, GRID }
class WrongImageException(m: String): Exception(m)
class WrongTransparencyException (m: String): Exception(m)
class WrongColorInputException (m: String): Exception(m)
class WrongWatermarkCoordinatedInputException (m: String): Exception(m)
class PositionTypeException (m: String): Exception(m)

class Watermark {
    private var image = BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)
    private var watermark = BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)
    private var useAlphaChannel = false
    private var colorToUseAsTransparent: Color?  = null
    private var transparencyPercent: Int = 100
    private var watermarkPositionX = 0
    private var watermarkPositionY = 0
    private var positionMethod: PositionMethod = PositionMethod.SINGLE
    private var outputImageFile: File? = null
    private var outputImage: BufferedImage = BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)
    private var outputFileName: String = ""
    var successfullInit = false

    init {
        try {
            initImage()
            initWatermark()
            initTransparencyModeAndColor()
            initTransparencyPercent()
            initPositionMethod()
            initWatermarkPosition()
            initOutputImage()
            successfullInit = true
        } catch (e: WrongImageException) {
            println(e.message)
        } catch (e: WrongTransparencyException) {
            println(e.message)
        } catch (e: WrongColorInputException) {
            println(e.message)
        } catch (e: WrongWatermarkCoordinatedInputException) {
            println(e.message)
        } catch (e: PositionTypeException) {
            println(e.message)
        }
    }

    private fun initImage() {
        println("Input the image filename:")
        val  filePath = readln()
        image = getImage(filePath)
    }

    private fun initWatermark() {
        println("Input the watermark image filename:")
        val filePath = readln()
        watermark = getImage(filePath, "watermark")

        if(watermark.width > image.width || watermark.height > image.height) {
            throw WrongImageException("The watermark's dimensions are larger.")
        }

    }

    private fun initPositionMethod() {
        println("Choose the position method (single, grid):")
        val strPos = readln()
        positionMethod = if(strPos == "single") {
            PositionMethod.SINGLE
        } else if (strPos == "grid") {
            PositionMethod.GRID
        } else {
            throw PositionTypeException("The position method input is invalid.")
        }
    }
    private fun initTransparencyModeAndColor() {
        if(watermark.transparency == 3) {
            println("Do you want to use the watermark's Alpha channel?")
            useAlphaChannel = readln() == "yes"
        } else {
            println("Do you want to set a transparency color?")
            if(readln() == "yes") {
                println("Input a transparency color ([Red] [Green] [Blue]):")
                try {
                    val color = readln().split(" ").map { it.toInt() }
                    if(color.size != 3)  throw WrongColorInputException("The transparency color input is invalid.")
                    colorToUseAsTransparent = Color(color[0], color[1], color[2])
                } catch (e: Exception){
                    throw WrongColorInputException("The transparency color input is invalid.")
                }
            }
        }
    }

    private fun initTransparencyPercent() {
        println("Input the watermark transparency percentage (Integer 0-100):")
        try {
            transparencyPercent = readln().toInt()
        } catch (e: Exception) {
            throw WrongTransparencyException("The transparency percentage isn't an integer number.")
        }
        if(transparencyPercent !in (0..100)) {
            throw WrongTransparencyException("The transparency percentage is out of range.")
        }
    }

    private fun getImage (filePath: String, imageName:String = "image"):BufferedImage {
        val file = File(filePath)
        if(!file.exists()) {
            throw WrongImageException("The file $filePath doesn't exist.")
        }
        val image: BufferedImage = ImageIO.read(file)
        if(image.colorModel.numColorComponents != 3) {
            throw WrongImageException("The number of $imageName color components isn't 3.")

        }
        if(image.colorModel.pixelSize !in listOf(24,32)) {
            throw WrongImageException ("The $imageName isn't 24 or 32-bit.")
        }
        return image

    }

    private fun initWatermarkPosition() {
        if(positionMethod == PositionMethod.SINGLE) {
            val maxWatermarkpositionX = image.width - watermark.width
            val maxWatermarkpositionY = image.height - watermark.height

            println("Input the watermark position ([x 0-$maxWatermarkpositionX] [y 0-$maxWatermarkpositionY]):")
            try {
                val listXY = readln().split(" ").map { it.toInt() }
                check( listXY.size == 2)
                watermarkPositionX = listXY[0]
                watermarkPositionY = listXY[1]
                if(watermarkPositionX !in 0..maxWatermarkpositionX || watermarkPositionY !in 0..maxWatermarkpositionY) throw RangeException (1, "The position input is out of range.")

            } catch(e: RangeException) {
                throw WrongWatermarkCoordinatedInputException (e.message ?: "")
            } catch(e: Exception) {
                throw WrongWatermarkCoordinatedInputException ("The position input is invalid.")
            }
        }

    }

    private fun initOutputImage() {
        println("Input the output image filename (jpg or png extension):")
        outputFileName = readln()
        if (
            !(outputFileName.substringAfterLast('.') == "png" ||
                    outputFileName.substringAfterLast('.') == "jpg")
        ) {
            throw WrongImageException("The output file extension isn't \"jpg\" or \"png\".")
        }
        outputImageFile = File(this.outputFileName)
        outputImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)

        val g = outputImage.graphics
        g.drawImage(image, 0, 0, null)
        g.dispose()



    }

    private fun putWatermarkOnPosition (x: Int, y: Int) {
        for (i in 0 until watermark.width) {
            for (j in 0 until watermark.height) {
                if(x + i >= outputImage.width || y + j >= outputImage.height) continue
                val watermarkColor = Color(watermark.getRGB(i, j), useAlphaChannel)
                val imageColor = Color(image.getRGB(x + i, y + j))

                if (
                    useAlphaChannel && watermarkColor.alpha == 255
                    || !useAlphaChannel && colorToUseAsTransparent == null
                    ||  colorToUseAsTransparent != null && watermarkColor != colorToUseAsTransparent
                ) {
                    val color = Color(
                        (transparencyPercent * watermarkColor.red + (100 - transparencyPercent) * imageColor.red) / 100,
                        (transparencyPercent * watermarkColor.green + (100 - transparencyPercent) * imageColor.green) / 100,
                        (transparencyPercent * watermarkColor.blue + (100 - transparencyPercent) * imageColor.blue) / 100
                    )
                    outputImage.setRGB(x + i, y + j, color.rgb)
                }

            }
        }
    }

    fun putWatermark() {

        if(positionMethod == PositionMethod.SINGLE) {
            putWatermarkOnPosition(watermarkPositionX, watermarkPositionY)
        } else if (positionMethod == PositionMethod.GRID) {
            for (i in 0..image.width / watermark.width) {
                for (j in 0.. image.height / watermark.height) {
                    putWatermarkOnPosition(i * watermark.width, j * watermark.height )
                }
            }
        }
    }

    fun saveOutputImage() {
        ImageIO.write(outputImage, "png", outputImageFile)
        println("The watermarked image $outputFileName has been created.")
    }

}

fun main() {

    val watermark = Watermark()
    if(watermark.successfullInit) {
        watermark.putWatermark()
        watermark.saveOutputImage()
    }

}