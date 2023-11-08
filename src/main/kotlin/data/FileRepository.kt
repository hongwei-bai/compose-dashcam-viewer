package data

import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class FileRepository {
    private val _fileStateFlow = MutableStateFlow<File?>(null)
    val fileStateFlow = _fileStateFlow

    fun readFiles(path: String) {
        _fileStateFlow.value = File(path)
    }
}