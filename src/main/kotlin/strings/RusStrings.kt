package strings

class RusStrings : Strings {
    override val title = "Утилиты для KRobots"

    // Menu bar
    override val connect = "Подключение"
    override val disconnect = "Отключение"
    override val toRobot = "Загрузка с робота"
    override val toBreakChecker = "Загрузка с Break checker"
    override val file = "Файл"
    override val save = "Сохранить"
    override val load = "Загрузить"
    override val saveProject = "Сохранить проект (.krsd)"
    override val saveExcelTable = "Сохранить отчет (.xlsx)"
    override val saveBackup = "Сохранить бэкап (.as)"

    // Robot connection
    override val enterIp = "Введите Ip"
    override val enterPort = "Введите порт"

    // Break checker connect
    override val breakCheckerWindowName = "Подключение к Break Checker"
    override val listAcceptablePorts = "Список допустимых COM портов"
    override val getReport = "Загрузка отчета"
}
