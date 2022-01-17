package strings

class EnStrings : Strings {
    override val title = "Utils for KRobots"
    override val krsdFile = "KRSD Files"
    override val krsdFileExpansion = "krsd"
    override val asFile = "AS Files"
    override val asFileExpansion = "as"
    override val xlsxFile = "Excel Files"
    override val xlsxFileExpansion = "xlsx"

    // Menu bar
    override val connect = "Connect"
    override val disconnect = "Disconnect"
    override val toRobot = "Load from robot"
    override val toBreakChecker = "Load from break checker"
    override val file = "File"
    override val save = "Save"
    override val load = "Load"
    override val saveProject = "Save project (.krsd)"
    override val savingProject = "Saving project (.krsd)"
    override val loadProject = "Load project (.krsd)"
    override val loadingProject = "Loading project (.krsd)"
    override val saveExcelTable = "Save report (.xlsx)"
    override val savingExcelTable = "Saving report (.xlsx)"
    override val saveBackup = "Save backup (.as)"
    override val savingBackup = "Saving backup (.as)"
    override val loadBackup = "Load from backup (.as)"

    // Robot connection
    override val robotConnection = "Connect to the robot"
    override val enterIp = "Enter Ip"
    override val enterPort = "Enter port"

    // Break checker connect
    override val breakCheckerWindowName = "Connect to break check"
    override val listAcceptablePorts = "List of acceptable COM ports"
    override val getReport = "Get report"
}
