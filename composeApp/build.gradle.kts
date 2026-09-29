
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.tasks.Copy

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)

    kotlin("plugin.serialization") version libs.versions.kotlin
    id("com.codingfeline.buildkonfig") version libs.versions.buildkonfig
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                mode = KotlinWebpackConfig.Mode.PRODUCTION

                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.compottie.lite)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)
            implementation(libs.navigation.compose)
            implementation(libs.material3.window.size)
            implementation(libs.koin.compose.view.model)
            implementation(libs.settings.no.arg)
            implementation(libs.kotlinx.datetime)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}

val localProperties = Properties().apply {
    FileInputStream(rootProject.file("local.properties")).use {
        load(it)
    }
}
tasks.named<Copy>("wasmJsProcessResources") {
    filesMatching("preloader.js") {
        filter { line ->
            line
                .replace(
                    "__HTTPS_HOST_NAME__",
                    localProperties.getProperty("httpsHostName", "")
                )
                .replace(
                    "__AD_TRACKING_BEARER_TOKEN__",
                    localProperties.getProperty("adTrackingBearerToken", "")
                )
                .replace(
                    "__APPLE_PROVIDER_TOKEN__",
                    localProperties.getProperty("appleProviderToken", "")
                )
        }
    }
}

buildkonfig {
    packageName = "augmy.interactive.com"

    defaultConfigs {
        buildConfigField(
            STRING,
            "MatrixMediaToken",
            localProperties.getProperty("matrixMediaToken", "")
        )

        buildConfigField(
            STRING,
            "HttpsHostName",
            localProperties.getProperty("httpsHostName", "")
        )

        buildConfigField(
            STRING,
            "AdTrackingBearerToken",
            localProperties.getProperty("adTrackingBearerToken", "")
        )

        buildConfigField(
            STRING,
            "AppleProviderToken",
            localProperties.getProperty("appleProviderToken", "")
        )
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    languageSettings.optIn("kotlin.time.ExperimentalTime")
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.WARNING // NONE | FAIL
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false // true
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = false // true
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}
