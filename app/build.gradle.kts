import java.io.FileInputStream
import java.util.*

plugins {
    id(BuildPlugins.androidLibrary)
    kotlin(BuildPlugins.kotlinAndroid)
    kotlin(BuildPlugins.kotlinAndroidExtensions)
    `maven-publish`
}

apply(from ="versioning.gradle.kts")

val versionFile = File(project.rootDir, "version.properties")
var versionProps = Properties()

FileInputStream(versionFile).use { stream -> versionProps.load(stream) }
val versionNumber = versionProps["version"].toString()

val envBuildNumber = (System.getenv("GITHUB_RUN_ID") ?: "0").toInt()


android {
    compileSdkVersion(Android.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.targetSdk)
        versionCode = envBuildNumber
        versionName = versionNumber
        testInstrumentationRunner = TestDependencies.testInstrumentationRunner

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                manifestPlaceholders = mapOf("enableCrashReporting" to "false")
                buildConfigField("String", "PS_BASE_URL", "\"https://pactsafe.io\"")

            }
            getByName("debug") {
                isMinifyEnabled = false
                isDebuggable = true
                manifestPlaceholders = mapOf("enableCrashReporting" to "true")
                buildConfigField("String", "PS_BASE_URL", "\"https://dev.pactsafe.io\"")
            }
        }

        afterEvaluate {
            publishing {
                publications {
                    register("gprRelease", MavenPublication::class){
                            groupId = "com.pactsafe"
                            artifactId = "androidsdk"
                            version = versionNumber
                            artifact("$buildDir/outputs/aar/app-release.aar")
                    }
                }

                repositories {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/pactsafe/pactsafe-android-sdk")
                        credentials {
                            username = System.getenv("BOT_USERNAME")
                            password = System.getenv("BOT_TOKEN")
                        }
                    }
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility= JavaVersion.VERSION_1_8
        targetCompatibility =JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.ktxCore)
    implementation(Dependencies.constraintLayout)
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.androidxTestRunner)
    androidTestImplementation(TestDependencies.espressoCore)

    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitCallAdapter)
    implementation(Dependencies.retrofitConverter)
    implementation(Dependencies.androidCoroutines)
    implementation(Dependencies.ktxCore)
    implementation(Dependencies.rxJava)
    implementation(Dependencies.rxKotlin)
    implementation(Dependencies.rxAndroid)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.androidCoroutines)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.gson)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLogging)
    implementation(Dependencies.serializationRuntime)
    implementation(Dependencies.material)
}
