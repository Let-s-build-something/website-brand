
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs.addAll("-Xopt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
        }

        moduleName = "composeApp"
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
            implementation(libs.compottie.network)
            implementation(libs.navigation.compose)
            implementation(libs.material3.window.size)
            implementation(libs.koin.compose.view.model)
            implementation(libs.settings.no.arg)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
    }
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.WARNING // NONE | FAIL
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false // true
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = false // true
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}

tasks.register<Exec>("optimizeWasm") {
    group = "build"
    description = "Optimize the generated WASM file using Binaryen"

    // Ensure this task runs after the WASM compilation task
    dependsOn("wasmJsBrowserProductionWebpack")

    // Define the paths for the original and optimized WASM files
    val wasmFile = file("$buildDir/dist/wasmJs/productionExecutable/composeApp.wasm")
    val optimizedWasmFile = file("$buildDir/dist/wasmJs/productionExecutable/composeApp.optimized.wasm")

    // Specify the command and arguments for Binaryen's wasm-opt
    commandLine("wasm-opt", wasmFile.absolutePath, "-o", optimizedWasmFile.absolutePath, "--O3")

    doLast {
        // Optionally delete the original WASM file if needed
        // wasmFile.delete()
        println("Optimized WASM file created at: ${optimizedWasmFile.absolutePath}")
    }
}

// Make sure to run the optimization task after building
tasks.getByName("wasmJsBrowserDevelopmentRun").finalizedBy("optimizeWasm")