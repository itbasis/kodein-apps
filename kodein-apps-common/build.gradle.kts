import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformCommonPlugin

apply {
	plugin<KotlinPlatformCommonPlugin>()
}

val kotlinExVersion: String by project

dependencies {
	"compile"(
		group = "com.github.itbasis.kodein-ex",
		name = "kodein-ex-common",
		version = kotlinExVersion
	)
}
