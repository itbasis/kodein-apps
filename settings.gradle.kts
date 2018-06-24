rootProject.name = "kodein-apps"

arrayOf("common", "flyway").forEach {
	include("${rootProject.name}-$it")
}
