object Resource

fun inputAsLines(name: String): List<String> = inputAsText(name).lines()

fun inputAsText(name: String): String = Resource::class.java.getResource("$name.txt")?.readText()
    ?: error("File '$name.txt' not found in resources")