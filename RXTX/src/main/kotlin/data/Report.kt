package data

import data.reportJT.ReportJT

class Report (
    val checkerNo: Int? = null,
    val softwareVersion: String = "-",
    val machineType: String = "-",
    val separateHarness: Int? = null,
    val measuredOhmJudge: Int? = null,
    val reportsJT: List<ReportJT>
){
    override fun toString(): String {
        return "Checker No: $checkerNo \n" +
                "SoftwareVersion: $softwareVersion\n" +
                "MachineType: $machineType\n" +
                "Separate Harness: $separateHarness\n" +
                "Measured Ohm Judge: $measuredOhmJudge\n" +
                "Report on each axis: ${reportsJT.joinToString("\n-----------------\n")}"

    }
}