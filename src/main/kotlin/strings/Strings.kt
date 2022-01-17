package strings

interface Strings {
   val title: String

   // Menu bar
   val connect: String
   val disconnect: String
   val toRobot: String
   val toBreakChecker: String
   val file: String
   val save: String
   val load: String
   val saveProject: String
   val saveExcelTable: String
   val saveBackup: String

   // Robot connection
   val robotConnection: String
   val enterIp: String
   val enterPort: String

   // Break checker connect
   val breakCheckerWindowName: String
   val listAcceptablePorts: String
   val getReport: String
}
