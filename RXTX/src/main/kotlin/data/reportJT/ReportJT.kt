package data.reportJT

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