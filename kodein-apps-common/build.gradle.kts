import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformCommonPlugin

apply {
	plugin<KotlinPlatformCommonPlugin>()
}

val kotlinUtilsVersion: String by project
val kotlinExVersion: String by project

dependencies {
	"compile"(
		group = "com.github.itbasis.kodein-ex", name = "kodein-ex-common", version = kotlinExVersion
	)

	"compile"(
		group = "com.github.itbasis.kotlin-utils",
		name = "kotlin-utils-jvm",
		version = kotlinUtilsVersion
	)

}
