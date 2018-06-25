import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformCommonPlugin

apply {
	plugin<KotlinPlatformCommonPlugin>()
}

val kotlinUtilsVersion: String by project
val kotlinExVersion: String by project

dependencies {
	"compile"("com.github.itbasis.kodein-ex:kodein-ex-common:$kotlinExVersion")

	"compile"("com.github.itbasis.kotlin-utils:kotlin-utils-common:$kotlinUtilsVersion")
}
