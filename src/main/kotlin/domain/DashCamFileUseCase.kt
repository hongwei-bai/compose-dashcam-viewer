package domain

import data.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.File


class DashCamFileUseCase(
    private val repository: FileRepository = FileRepository()
) {
    val driverStateFlow = repository.driverFlow.map {
        val driverNames = it.map { driver ->
            driver.canonicalPath
        }
        takeFirstDriverAsDefault(it)
        driverNames
    }

    val footageDirStateFlow = repository.footageFlow.map {
        parseFootage(it.first())
        it
    }

    val statusBarStateFlow = repository.informationFlow

    private val _selectedDriverStateFlow = MutableStateFlow<File?>(null)
    val selectedDriverStateFlow = _selectedDriverStateFlow

    private val _footageStateFlow = MutableStateFlow<File?>(null)
    val footageStateFlow = _footageStateFlow

    init {
        repository.identifyRemovableDrivers()
    }

    fun load() {
        repository.identifyRemovableDrivers()
    }

    fun selectDriver(driver: String) {
        repository.locateFootage(File(driver))
    }

    private fun takeFirstDriverAsDefault(drivers: List<File>) {
        if (_selectedDriverStateFlow.value == null && drivers.isNotEmpty()) {
            _selectedDriverStateFlow.value = drivers.first()
            repository.locateFootage(drivers.first())
        }
    }

    private fun parseFootage(front: File) {
        front.listFiles()


    }
}