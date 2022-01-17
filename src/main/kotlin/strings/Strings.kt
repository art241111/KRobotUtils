package strings

interface Strings {
   val title: String
   val krsdFile: String
   val krsdFileExpansion: String
   val asFile: String
   val asFileExpansion: String
   val xlsxFile: String
   val xlsxFileExpansion: String

   // Menu bar
   val connect: String
   val disconnect: String
   val toRobot: String
   val toBreakChecker: String
   val file: String
   val save: String
   val load: String
   val saveProject: String
   val savingProject: String
   val loadProject: String
   val loadingProject: String
   val saveExcelTable: String
   val savingExcelTable: String
   val saveBackup: String
   val savingBackup: String
   val loadBackup: String

   // Robot connection
   val robotConnection: String
   val enterIp: String
   val enterPort: String

   // Break checker connect
   val breakCheckerWindowName: String
   val listAcceptablePorts: String
   val getReport: String
}
