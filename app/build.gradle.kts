
plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "1.8.0"
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true  // Habilita Jetpack Compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"  // Asegúrate de usar la versión correcta
    }
    splits{
        abi{
            isEnable = false
        }
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.activity:activity-compose:1.8.0")  // Permite usar Compose en actividades
    implementation("androidx.compose.ui:ui:1.5.0")  // Librerías esenciales de Compose
    implementation("androidx.compose.material3:material3:1.2.0")  // Material Design en Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")  // Previews de UI en Compose
    implementation("androidx.navigation:navigation-compose:2.8.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")  // Herramientas de depuración
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("androidx.compose.runtime:runtime-livedata:<version>")
    implementation("com.google.firebase:firebase-auth:<latest_version>")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.gms:play-services-auth:20.1.0")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-gif:2.2.2")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.31.1-alpha")

}