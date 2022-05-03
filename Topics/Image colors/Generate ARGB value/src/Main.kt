
fun printARGB() {
    try {
        val (a, r, g, b) = readln().trim().split(" ").map { it.toUByte().toInt() }
        val color = Color(r, g, b, a)
        println(color.rgb.toUInt())
    } catch (e: Exception) { println("Invalid input") }     
}
