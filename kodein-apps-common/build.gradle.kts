import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformCommonPlugin

apply {
	plugin<KotlinPlatformCommonPlugin>()
}

val kotlinUtilsVersion: String by project
val kodeinExVersion: String by project

dependencies {
	"compile"("com.github.itbasis.kodein-ex:kodein-ex-common:$kodeinExVersion")

	"compile"("com.github.itbasis.kotlin-utils:kotlin-utils-common:$kotlinUtilsVersion")
}
