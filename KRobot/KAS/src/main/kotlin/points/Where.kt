package com.github.poluka.kControlLibrary.points

import com.github.poluka.kControlLibrary.Command
import com.github.poluka.kControlLibrary.program.Program

/**
 * Displays the current robot pose.
 *
 * @param Selects the mode in which the data is displayed. There are 16 modes as shown below (modes 7
 * to 16 are options). If the mode is not specified, transformation values of the TCP in the base
 * coordinates and the joint angles (JT1, JT2, …, JT3) are displayed. The display mode does not
 * change until "Enter" is pressed again.
 */
class Where(private val displayMode: Int? = null) : Command {
    override fun generateCommand(): String {
        return "WHERE ${displayMode ?: ""}"
    }
}

fun Program.WHERE(displayMode: Int? = null) = add(Where(displayMode))
