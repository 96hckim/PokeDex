import com.hckim.pokedex.PokeDexBuildType

plugins {
    alias(libs.plugins.pokedex.android.application)
    alias(libs.plugins.pokedex.android.application.compose)
    alias(libs.plugins.pokedex.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        applicationId = "com.hckim.pokedex"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            applicationIdSuffix = PokeDexBuildType.DEBUG.applicationIdSuffix
        }
        release {
            // 1. 코드 최적화 활성화 (기본값 true, 필요시 gradle.properties에서 끄기 가능)
            isMinifyEnabled = providers.gradleProperty("minifyWithR8")
                .map(String::toBooleanStrict).getOrElse(true)
            applicationIdSuffix = PokeDexBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // 2. 개발 중 릴리즈 빌드 테스트를 위해 디버그 키로 서명 허용
            signingConfig = signingConfigs.named("debug").get()
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true
    namespace = "com.hckim.pokedex"
}

dependencies {
    implementation(projects.feature.list)
    implementation(projects.feature.detail)
    implementation(projects.feature.favorites)

    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.model)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    kspTest(libs.hilt.compiler)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
