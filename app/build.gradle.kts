plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

//androidExtensions {
//    experimental = true
//}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.iaruchkin.deepbreath"
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 29
        versionName = "1.4.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables.useSupportLibrary = true

//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments = ["room.schemaLocation": "$projectDir/schemas".toString()]
//            }
//        }

//        apply(from = "../apikey.properties.kts")
//        val WEATHER_API_KEY: String by extra
//
//        //Api keys stored in gradle.properties
//        buildConfigField("String", "WEATHER_API_KEY", WEATHER_API_KEY)
//        buildConfigField("String", "OPEN_WEATHER_API_KEY", OPEN_WEATHER_API_KEY)
//        buildConfigField("String", "AQI_API_KEY", AQI_API_KEY)
//        buildConfigField("String", "AQIAV_API_KEY", AQIAV_API_KEY)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
//    compileOptions {
//        sourceCompatibility = '1.8'
//        targetCompatibility = '1.8'
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.7.20")

    //groupie
    implementation("com.xwray:groupie:2.2.0")
    implementation("com.xwray:groupie-kotlin-android-extensions:2.2.0")

    //test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //other
    implementation("io.reactivex.rxjava2:rxjava:2.2.9")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")

    //appCompat
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    //glide
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")

    //room
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-rxjava2:2.4.3")
    annotationProcessor("androidx.room:room-compiler:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")

    //circle indicator
    implementation("me.relex:circleindicator:2.1.0")

    //worker service
    implementation("androidx.work:work-runtime:2.8.0-rc01")

    //moxy
    implementation("com.github.moxy-community:moxy:2.2.2")
    annotationProcessor("com.github.moxy-community:moxy-compiler:2.2.2")
    kapt("com.github.moxy-community:moxy-compiler:2.2.2")
    implementation("com.github.moxy-community:moxy-ktx:2.2.2")
    implementation("com.github.moxy-community:moxy-androidx:2.2.2")


    //location
    implementation("com.google.android.gms:play-services-location:18.0.0")


    // When using a MDC theme
    implementation("com.google.android.material:compose-theme-adapter:1.2.1")

    // When using a AppCompat theme
    implementation("com.google.accompanist:accompanist-appcompat-theme:0.25.1")
}