package data

class AttractingVolts(
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

class ReleasingVolts(
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

class BrakeResistance(
    val measureData: Double,
    val voltStandard: Double,
    val judgeResult: String = ""
) {
    override fun toString(): String {
        return "Measured data  Mean data  Volt standard    Judge result \n" +
                "$measureData   $voltStandard   $judgeResult \n"
    }
}

class ResistanceLine(
    val measureData: Double?,
    val resistanceStandard: Double?,
    val judgeResult: String = ""
)

class ReportJT(
    var resistance: List<ResistanceLine>,
    val brakeVoltMeasuringTime: Int?,
    val attractingVolts: AttractingVolts,
    val releasingVolts: ReleasingVolts,
    val brakeResistance: BrakeResistance,
) {
    override fun toString(): String {
        var ret = ""
        resistance.forEach {
            ret += "${it.measureData} ${it.resistanceStandard} ${it.judgeResult} \n"
        }
        return "Measured data    Resistance standards[Ohm]    Judge result \n" +
                "$ret \n" +
                "\n" +
                "Brake volt measuring time: $brakeVoltMeasuringTime \n" +
                "Attracting volts: \n" +
                attractingVolts.toString() +
                "Releasing volts: \n" +
                releasingVolts.toString() +
                "Brake resistance: \n" +
                brakeResistance.toString()
    }
}