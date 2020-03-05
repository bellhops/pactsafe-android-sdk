

plugins {
    id(BuildPlugins.androidApplication)
    kotlin(BuildPlugins.kotlinAndroid)
    kotlin(BuildPlugins.kotlinAndroidExtensions)
}

android {
    compileSdkVersion(Android.compileSdkVersion)
    defaultConfig {
        applicationId = AppInfo.identifier
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = TestDependencies.testInstrumentationRunner

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                manifestPlaceholders = mapOf("enableCrashReporting" to "false")

            }
            getByName("debug") {
                isMinifyEnabled = false
                isDebuggable = true
                manifestPlaceholders = mapOf("enableCrashReporting" to "true")

            }
        }
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
}