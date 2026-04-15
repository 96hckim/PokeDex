// Gradle 플러그인 관리 설정 (빌드 초기 단계에서 처리됨)
pluginManagement {
    // build-logic 디렉토리를 복합 빌드로 포함
    // 이를 통해 build-logic에서 정의한 Convention 플러그인을 프로젝트에서 사용 가능
    includeBuild("build-logic")

    // 플러그인을 찾을 저장소 설정
    repositories {
        // Google 저장소 - 특정 그룹만 포함하도록 제한
        google {
            content {
                includeGroupByRegex("com\\.android.*")  // Android 플러그인
                includeGroupByRegex("com\\.google.*")   // Google 플러그인
                includeGroupByRegex("androidx.*")       // Androidx 플러그인
            }
        }
        mavenCentral() // Maven Central 저장소
        gradlePluginPortal() // Gradle 플러그인 포털
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// 의존성 해결 방법 관리 설정
dependencyResolutionManagement {
    // 프로젝트별 저장소 설정을 금지하고 이 설정만 사용하도록 강제
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // 의존성을 찾을 저장소 설정
    repositories {
        google()        // Android/Google 라이브러리용
        mavenCentral()  // 대부분의 오픈소스 라이브러리용
    }
}

// 루트 프로젝트 이름 설정
rootProject.name = "PokeDex"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// 프로젝트에 포함할 모듈 정의
include(":app")

// 코어 모듈 (여러 모듈에서 공유되는 기능)
include(":core:model")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:domain")
include(":core:data")
include(":core:ui")

// 기능별 모듈
include(":feature:list")
include(":feature:detail")
include(":feature:favorites")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
    """
    Now in Android requires JDK 21+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
