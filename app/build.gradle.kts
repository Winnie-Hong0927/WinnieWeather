plugins {
//    alias(libs.plugins.androidApplication)
    id("com.android.application")
}

android {
    namespace = "com.hongwenli.winnieweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hongwenli.winnieweather"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {                                                      
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            // 配置jniLibs目录
            jniLibs.srcDirs("libs") // 加载so文件的目录
        }
    }
    buildFeatures {
        viewBinding = true //开启ViewBinding
        buildConfig = true //开启buildConfig，可以获取版本名和版本号
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("libs\\BaiduLBS_Android.jar"))
    implementation(files("..\\gradle\\wrapper\\gradle-wrapper.jar"))
    implementation("org.jetbrains:annotations:15.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.24")
    implementation(project(":library"))
    //Room数据库
    implementation ("androidx.room:room-runtime:2.4.2")
    annotationProcessor ("androidx.room:room-compiler:2.4.2")
    //Room 支持RxJava2
    implementation ("androidx.room:room-rxjava2:2.4.2")
    //腾讯MMKV
    implementation ("com.tencent:mmkv:1.2.11")
    //Gson
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

}

