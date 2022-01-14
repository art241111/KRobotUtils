package data.reportJT

import kotlinx.serialization.Serializable

@Serializable
data class BrakeResistance(
    val measureData: Double,
    val voltStandard: Double,
    val judgeResult: String = ""
) {
    override fun toString(): String {
        return "Measured data  Mean data  Volt standard    Judge result \n" +
                "$measureData   $voltStandard   $judgeResult \n"
    }
}