package augmy.interactive.com.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.LaptopMac
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.ui.graphics.vector.ImageVector

enum class PlatformDistribution(val label: String, val url: String, val imageVector: ImageVector) {
    ANDROID(
        label = "Android",
        url = "https://play.google.com/store/apps/details?id=augmy.interactive.com",
        imageVector = Icons.Outlined.Android
    ),
    IOS(
        label = "iOS",
        url = "",
        imageVector = Icons.Outlined.PhoneIphone
    ),
    WINDOWS(
        label = "Windows",
        url = "https://augmy.org/downloads/Augmy.msi",
        imageVector = Icons.Outlined.DesktopWindows
    ),
    MACOS(
        label = "macOS",
        url = "",
        imageVector = Icons.Outlined.LaptopMac
    ),
    LINUX(
        label = "Linux",
        url = "https://augmy.org/downloads/Augmy.deb",
        imageVector = Icons.Outlined.Laptop
    )
}