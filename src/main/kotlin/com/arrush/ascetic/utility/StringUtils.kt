package com.arrush.ascetic.utility

fun String.substringIf(start: Int, end: Int = this.length, condition: (String) -> Boolean): String = if (condition(this)) this.substring(start, end) else ("$this ").substring(start, this.length)
fun String.getPossibleInt(): Int? = try { this.toInt() } catch (ex: NumberFormatException) { null }
fun String.orderedReplace(oldValue: String, vararg newString: String): String {
    println(this.count(oldValue))
    println(newString.size)

    var replaced = this
    if (this.count(oldValue) == newString.size)
        for (lol in newString) replaced = replaced.replaceFirst(oldValue, lol)
    return replaced
}
fun String.count(value: String): Int {
    val char1 = value.toCharArray().distinct()
    val char2 = this.toCharArray()
    var count = 0
    for ( c : Char in char1) {
        count += char2.filter { s -> s == c }.count()
    }
    return count
}



operator fun String.times(quantity: Long): String {
    var string = ""
    for (i in 1..quantity) {
        string += this
    }
    return string
}
