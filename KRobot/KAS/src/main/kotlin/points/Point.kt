package com.github.poluka.kControlLibrary.points

import com.github.poluka.kControlLibrary.Command
import com.github.poluka.kControlLibrary.program.Program
import com.github.poluka.kControlLibrary.utils.GenerateRandomString

data class Point(
    var name: String = "",
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var o: Double = 0.0,
    var a: Double = 0.0,
    var t: Double = 0.0,
) : Command {
    override fun generateCommand(): String {
        return "POINT $name = TRANS($x,$y,$z,$o,$a,$t)"
    }

    override fun toString(): String {
        return "$x,$y,$z,$o,$a,$t"
    }

    companion object {
        fun createPoint(
            name: String = "",
            x: Double = 0.0,
            y: Double = 0.0,
            z: Double = 0.0,
            o: Double = 0.0,
            a: Double = 0.0,
            t: Double = 0.0,
        ): Point {
            var defName = name
            if (name == "") defName = GenerateRandomString.generateString(14)

            return Point(defName, x, y, z, o, a, t)
        }
    }

    fun copy(
        name: String? = null,
        x: Double? = null,
        y: Double? = null,
        z: Double? = null,
        o: Double? = null,
        a: Double? = null,
        t: Double? = null,
    ): Point {
        return Point(
            name = name ?: this.name,
            x = x ?: this.x,
            y = y ?: this.y,
            z = z ?: this.z,
            o = o ?: this.o,
            a = a ?: this.a,
            t = t ?: this.t,
        )
    }

    /**
     * Получаем доступ через координаты.
     * @param coordinate - координата, по которой нужно олучить значения.
     */
    operator fun get(index: Int): Double {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            3 -> o
            4 -> a
            5 -> t
            else -> x
        }
    }

    /**
     * Изменяем значения через координаты.
     * @param coordinate - координата, по которой нужно изменить значения,
     * @param value - значение, которое нужно установить.
     */
    operator fun set(index: Int, value: Double) {
        when (index) {
            0 -> x = value
            1 -> y = value
            2 -> z = value
            3 -> o = value
            4 -> a = value
            5 -> t = value
            else -> x = value
        }
    }
}

fun Program.POINT(
    name: String = "",
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 0.0,
    o: Double = 0.0,
    a: Double = 0.0,
    t: Double = 0.0,
): Point {
    val point = Point.createPoint(name, x, y, z, o, a, t)
    this.add(point)

    return point
}
