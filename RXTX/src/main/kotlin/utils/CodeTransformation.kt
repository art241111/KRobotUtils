package utils

import kotlin.math.pow

fun String.getString() = substring(6)

fun String.codeToStr(): String {
    val letters = mutableListOf<Char>()

    for (i in 1 until this.length - 1 step 2) {
        val wk = this[i]
        if (wk != '0' && wk != '1') {
            "${this[i]}${this[i + 1]}".toIntOrNull(16)?.let { letters.add(it.toChar()) }
        }
    }
    return letters.joinToString("")
}

fun String.codeToInt(): Int = substring(5, 9).codeToNum().toInt()

fun String.codeToIntWithCheck(): Int? {
    val text = this.substring(5, 9)
    return if (text > "8000" && text < "FFFF") {
        null
    } else {
        this.codeToInt()
    }
}

fun String.codeToLong(): Long = substring(5, 13).codeToNum()
fun String.codeToLongWithCheck(): Long? {
    val res = substring(5, 13).codeToNum()
    return if (res < 0) null
    else res
}

private fun String.codeToNum(): Long {
    val arr16 = mutableListOf<Long>()
    this.forEachIndexed() { index, char ->
        arr16.add(index = index, element = char.toString().toLong(16))
    }

    if (arr16[0] > 7) {
        arr16[0] = arr16[0] - 16
    }

    var returnValue = 0L
    arr16.forEachIndexed { index, value ->
        returnValue += (value * (16.0).pow((length - 1) - index)).toLong()
    }

    return returnValue
}

fun String.codeToJudgeResult(): String =
    when (this.substring(5, 9)) {
        "0001" -> "O"
        "FFFF" -> "X"
        else -> "-"
    }