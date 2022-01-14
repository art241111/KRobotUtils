package data.reportJT

import kotlinx.serialization.Serializable

@Serializable
data class ResistanceLine(
    val measureData: Double?,
    val resistanceStandard: Double?,
    val judgeResult: String = ""
)