package data

import constant.OsConstant
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.swing.filechooser.FileSystemView

class FileRepository {
    private val _driverFlow = MutableStateFlow<List<File>>(emptyList())
    val driverFlow = _driverFlow

    private val _footageFlow = MutableStateFlow<List<File>>(emptyList())
    val footageFlow = _footageFlow

    private val _informationFlow = MutableStateFlow<String?>(null)
    val informationFlow = _informationFlow

    fun identifyRemovableDrivers() {
        val fsv = FileSystemView.getFileSystemView()
        val removableDrivers = File.listRoots().filter { path ->
            val description = fsv.getSystemTypeDescription(path)
            description?.contains(OsConstant.REMOVABLE_DISK_KEY_WORD) ?: false
        }
        driverFlow.value = removableDrivers
        if (removableDrivers.isEmpty()) {
            _informationFlow.value = "No removable drivers found!"
        }
    }

    fun locateFootage(file: File) {
        val frontDir = findDirByName("front", file)
        var rearDir = findDirByName("rear", file)
        if (rearDir == null) {
            rearDir = findDirByName("back", file)
        }

        if (frontDir == null) {
            _informationFlow.value = "No footage found!"
            return
        }

        if (rearDir != null) {
            _footageFlow.value = listOf(
                frontDir, rearDir
            )
        } else {
            _footageFlow.value = listOf(frontDir)
        }
    }

    fun readFiles(path: String) {
//        if (!File(path).exists() || !File(path).isDirectory) {
//            _errorStateFlow.value = "No dash cam footage found."
//        }
//
//        _readResultStateFlow.value = File(path)
    }

    private fun findDirByName(name: String, file: File): File? {
        file.listFiles()?.forEach {
            if (it.isDirectory) {
                if (it.name.equals(name)) {
                    val filesInside = it.listFiles()
                    if (filesInside != null) {
                        if (filesInside.isNotEmpty() && filesInside.any { it.name.endsWith(".mp4") }) {
                            return it
                        }
                    }
                } else {
                    val r = findDirByName(name, it)
                    r?.let { return r }
                }
            }
        }
        return null
    }

    private fun containsDashCamCharacteristic(file: File): Boolean {
        file.listFiles()?.forEach {
            if (it.isDirectory) {
                if (it.name.equals("front")) {
                    val files = it.listFiles()
                    if (files != null) {
                        return files.any { it.isFile && it.name.endsWith(".mp4") }
                    }
                }
                if (containsDashCamCharacteristic(it)) {
                    return true
                }
            }
        }
        return false
    }
}