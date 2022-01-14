package data.reportJT

import kotlinx.serialization.Serializable

@Serializable
data class AttractingVolts(
    val measureData: List<Double?>,
    val meanData: Double?,
    val voltStandard: Double?,
    val judgeResult: String = ""
) {
    override fun toString(): String {
        var retString = ""
        measureData.forEachIndexed { index, s ->
            if (index != 0) {
                retString += s.toString() + "\n"
            }
        }
        return "Measured data  Mean data  Volt standard    Judge result \n" +
                "${measureData[0]}  $meanData  $voltStandard   $judgeResult \n" +
                retString
    }
}