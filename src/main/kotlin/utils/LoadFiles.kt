package utils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class Dialog {
    enum class Mode { LOAD, SAVE }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun DialogFile(
    mode: Dialog.Mode = Dialog.Mode.LOAD,
    title: String = "Choisissez un fichier",
    extensions: List<FileNameExtensionFilter> = listOf(),
    scope: FrameWindowScope,
    onResult: (files: List<File>) -> Unit
) {
    DisposableEffect(Unit) {
        val job = GlobalScope.launch(Dispatchers.Swing) {
            val fileChooser = JFileChooser()
            fileChooser.dialogTitle = title
            fileChooser.isMultiSelectionEnabled = mode == Dialog.Mode.LOAD
            fileChooser.isAcceptAllFileFilterUsed = extensions.isEmpty()
            extensions.forEach { fileChooser.addChoosableFileFilter(it) }

            val returned = if(mode == Dialog.Mode.LOAD) {
                fileChooser.showOpenDialog(scope.window)
            } else {
                fileChooser.showSaveDialog(scope.window)
            }

            onResult(when(returned) {
                JFileChooser.APPROVE_OPTION -> {
                    if(mode == Dialog.Mode.LOAD) {
                        val files = fileChooser.selectedFiles.filter { it.canRead() }
                        if(files.isNotEmpty()) {
                            files
                        }
                        else {
                            listOf()
                        }
                    } else {
                        if(!fileChooser.fileFilter.accept(fileChooser.selectedFile)) {
                            val ext = (fileChooser.fileFilter as FileNameExtensionFilter).extensions[0]
                            fileChooser.selectedFile = File(fileChooser.selectedFile.absolutePath + ".$ext")
                        }
                        listOf(fileChooser.selectedFile)
                    }
                }
                else -> listOf()
            })
        }

        onDispose {
            job.cancel()
        }
    }
}