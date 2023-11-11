package constant

sealed class DashCamConfig(
    val name: String,
    val frontKey: String,
    val rearKey: String?,
    val videoExt: String
)

object DDPai : DashCamConfig("DDPai", "front", "rear", "mp4")
object D70mai : DashCamConfig("70mai", "front", "back", "mp4")

val dashCamList = listOf(
    DDPai, D70mai
)