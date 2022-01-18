package utils.excelUtils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Paths

fun workbook(filename: String, init: Workbook.() -> Unit): Workbook {
    val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
    val resourcesPath = Paths.get(projectDirAbsolutePath, "/src/main/resources")
    val file: InputStream = FileInputStream(File("$resourcesPath/$filename"))
    val workbook: Workbook = XSSFWorkbook(file)

    return workbook.apply(init)
}

fun Sheet.setValue(colIndex: Int, rowIndex: Int, value: String) {
    val row = getRow(rowIndex - 1)
    var cell: Cell? = row.getCell(colIndex  - 1)
    if (cell == null) cell = row.createCell(colIndex  - 1)
//        cell.setCellType(CellType.STRING)
    cell?.setCellValue(value)
}


fun Workbook.write(filename: String) {
    FileOutputStream(filename).use { out ->
        write(out)
    }
}