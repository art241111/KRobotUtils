package com.github.poluka.kControlLibrary.points

import com.github.poluka.kControlLibrary.Command
import com.github.poluka.kControlLibrary.program.Program

/**
 * Assigns the current pose to the pose variable with the specified variable name. The pose may be
 * expressed in transformation values, joint displacement values or compound transformation values.
 *
 * @param pointName - Variable value can be specified in transformation values, joint displacement values, or compound
 * transformation values
 */
class Here(private val pointName: String) : Command {
    constructor(point: Point) : this (point.name)

    override fun generateCommand(): String {
        return "HERE $pointName"
    }
}

fun Program.HERE(pointName: String) = add(Here(pointName))
fun Program.HERE(point: Point) = add(Here(point))
