const val kotlinVersion = "1.3.61"

object AppInfo {
    const val identifier = "com.pactsafe.pactsafeandroidsdk"
}

object Android {
    const val compileSdkVersion = 29
    const val minSdk = 22
    const val targetSdk = 29
}

object BuildPlugins {
    private object Versions {
        const val buildToolsVersion = "3.4.2"
        const val ktlint = "8.2.0"
        const val googleServiceVersion = "4.3.2"
    }

    const val googleServices = "com.google.gms:google-services:${Versions.googleServiceVersion}"
    const val googleServicesPlugin = "com.google.gms.google-services"

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val ktlintPlugin = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val kotlinAndroid = "android"
    const val kotlinAndroidExtensions = "android.extensions"
    const val serialization = "org.jetbrains.kotlin.plugin.serialization"
}

object Dependencies {
    private object Versions {
        const val appCompat = "1.1.0"
        const val constraintLayout = "1.1.3"
        const val coroutines = "1.3.2"
        const val fragment = "1.2.0-alpha02"
        const val gson = "2.8.5"
        const val koin = "2.0.1"
        const val ktx = "1.1.0"
        const val legacySupport = "1.0.0"
        const val lifecycle = "2.1.0"
        const val material = "1.1.0-alpha10"
        const val materialDialog = "3.1.1"
        const val okHttp = "4.2.1"
        const val reflect = "1.3.41"
        const val retrofit = "2.6.0"
        const val retrofitAdapter = ""
        const val rxJava = "2.2.13"
        const val rxKotlin = "2.4.0"
        const val rxAndroid = "2.1.1"
        const val serializationVersion = "0.13.0"
    }

    const val androidCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val materialDialogCore = "com.afollestad.material-dialogs:core:${Versions.materialDialog}"
    const val materialDialogFiles = "com.afollestad.material-dialogs:files:${Versions.materialDialog}"
    const val materialDialogInput = "com.afollestad.material-dialogs:input:${Versions.materialDialog}"
    const val materialDialogDateTime = "com.afollestad.material-dialogs:datetime:${Versions.materialDialog}"
    const val materialDialogLifecycle = "com.afollestad.material-dialogs:lifecycle:${Versions.materialDialog}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitCallAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val serializationRuntime = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serializationVersion}"
    const val viewModelScope = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
}

object TestDependencies {
    private object Versions {
        const val androidxTestRunner = "1.1.0"
        const val espressoCore = "3.2.0"
        const val junit = "5.4.2"
    }

    const val androidxTestRunner = "androidx.test:runner:${Versions.androidxTestRunner}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val junit = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJunitRunner"
}