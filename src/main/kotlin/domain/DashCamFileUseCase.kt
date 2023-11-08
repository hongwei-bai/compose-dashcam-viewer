package domain

import data.FileRepository
import kotlinx.coroutines.flow.map

class DashCamFileUseCase(
    repository: FileRepository = FileRepository()
) {
    init {
        repository.readFiles("D:\\Profiles\\Hongwei\\Desktop\\hai usb")
    }

    val stateFlow = repository.fileStateFlow.map {
        it?.listFiles()
    }
}