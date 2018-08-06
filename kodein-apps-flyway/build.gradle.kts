import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

apply {
	plugin<KotlinPlatformJvmPlugin>()
}

val kodeinExVersion: String by project
val kotlinUtilsVersion: String by project
val commonsLang3Version: String by project
val commonsCompressVersion: String by project

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	"compile"(project(":kodein-apps-common"))

	"compile"(group = "com.github.itbasis.kodein-ex", name = "kodein-ex-jvm", version = kodeinExVersion)
	"compile"(group = "com.github.itbasis.kotlin-utils", name = "kotlin-utils-jvm", version = kotlinUtilsVersion)


	"compile"(group = "org.flywaydb", name = "flyway-core", version = "5.1.1")

	"testCompile"(group = "mysql", name = "mysql-connector-java", version = "8.0.11")
	"testCompile"("com.wix:wix-embedded-mysql:4.1.2")
	"testCompile"(group = "org.apache.commons", name = "commons-compress", version = commonsCompressVersion)
	"testCompile"(group = "org.apache.commons", name = "commons-lang3", version = commonsLang3Version)
}
