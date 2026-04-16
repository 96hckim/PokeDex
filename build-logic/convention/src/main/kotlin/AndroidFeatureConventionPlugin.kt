import com.android.build.api.dsl.LibraryExtension
import com.hckim.pokedex.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "pokedex.android.library")
            apply(plugin = "pokedex.hilt")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:common"))
                "implementation"(project(":core:domain"))

                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
//                "implementation"(libs.findLibrary("androidx.hilt.lifecycle.viewModelCompose").get())

                "androidTestImplementation"(
                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get(),
                )
            }
        }
    }
}
