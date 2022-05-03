import .File
fun main() {
    val mainDirectory = File("/Projects/Programming")
    mainDirectory.mkdir()
    val labDirectory = mainDirectory.resolve("Project1")
    if(!labDirectory.exists())
        labDirectory.mkdir()

    val reportFile = mainDirectory.resolve("Report.pdf")
    if(!reportFile.exists())
        reportFile.createNewFile()


    // do not touch the lines below
    files.forEach {
        if (it.path == "/Projects/Programming" && it.isDirectory) {
            print("true")
        } else if (it.path == "/Projects/Programming/Project1" && it.isDirectory) {
            print("true")
        } else if (it.path == "/Projects/Programming/Report.pdf" && it.isFile) {
            print("true")
        } else {
            print("false")
        }
    }
}