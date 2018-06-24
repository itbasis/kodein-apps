import org.gradle.plugins.ide.idea.model.IdeaModel

val kotlinVersion: String by extra
val commonsLang3Version: String by extra
val commonsCompressVersion: String by extra

repositories {
	mavenLocal()
	jcenter()
	mavenCentral()
	maven(url = "https://dl.bintray.com/kotlin/exposed")
	maven(url = "https://jitpack.io")
}

configurations.all {
	resolutionStrategy {
		failOnVersionConflict()

		eachDependency {
			when (requested.group) {
				"org.apache.commons" -> when (requested.name) {
					"commons-lang3" -> useVersion(commonsLang3Version)
					"commons-compress" -> useVersion(commonsCompressVersion)
				}

				"org.jetbrains.kotlin" -> useVersion(kotlinVersion)
				"org.slf4j" -> useVersion("1.7.25")
				"junit" -> useVersion("4.12")
				"io.kotlintest" -> useVersion("3.1.6")
//        "org.kodein.di"             -> useVersion("5.1.0")
//        "com.github.lewik.klogging" -> useVersion("1.2.41")
//        "com.github.lewik"          -> useTarget("com.github.lewik.klogging:${requested.name}:${requested.version}")

//	      "com.wix"                        -> useVersion("4.1.2")
//	      "org.flywaydb"                 -> useVersion("5.1.1")
//	      "org.jetbrains.exposed"        -> useVersion("0.10.2")
//	      "org.mindrot"                  -> useVersion("0.4")

//	      "com.github.itbasis.kodein-ex" -> useVersion("v20180623_0656")
			}
		}
	}
}

apply {
	plugin<IdeaPlugin>()
}

configure<IdeaModel> {
	module {
		isDownloadJavadoc = false
		isDownloadSources = false
	}
}
