plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.activity:activity-compose:1.8.0")  // Permite usar Compose en actividades
    implementation("androidx.compose.ui:ui:1.5.0")  // Librerías esenciales de Compose
    implementation("androidx.compose.material3:material3:1.2.0")  // Material Design en Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")  // Previews de UI en Compose
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")  // Herramientas de depuración
    implementation("io.coil-kt:coil-compose:2.0.0")
}