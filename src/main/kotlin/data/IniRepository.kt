package data

import kotlinx.coroutines.flow.MutableStateFlow
import org.ini4j.Ini
import org.ini4j.Wini
import java.io.File
import java.io.FileReader


class IniRepository {
    private val _iniStateFlow = MutableStateFlow<IniModel?>(null)
    val iniStateFlow = _iniStateFlow

    private val iniFile = File(System.getProperty("user.dir"), "dashcam_viewer.ini")

    fun read() {
        val ini = Ini(FileReader(iniFile))
        val section = ini[SECTION_DEFAULT]
        section?.run {
            iniStateFlow.value = IniModel(
                frontPath = get(KEY_FRONT) ?: "",
                rearPath = get(KEY_REAR) ?: "",
                outputPath = get(KEY_OUTPUT) ?: "",
            )
        }
    }

    fun save(front: String, rear: String, output: String) {
        val wini = Wini(iniFile)
        wini.put(SECTION_DEFAULT, KEY_FRONT, front)
        wini.put(SECTION_DEFAULT, KEY_REAR, rear)
        wini.put(SECTION_DEFAULT, KEY_OUTPUT, output)
        wini.store()
    }

    companion object {
        private const val SECTION_DEFAULT = "default"
        private const val KEY_FRONT = "front"
        private const val KEY_REAR = "rear"
        private const val KEY_OUTPUT = "output"
    }

}