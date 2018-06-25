package com.github.itbasis.kodein.apps.flyway

import com.github.itbasis.kodein.apps.flyway.services.FlywayService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.weakReference

const val KODEIN_TAG_DATA_SOURCE_CONFIG = "dataSourceConfig"

val flywayAppModule = Kodein.Module(name = "FlywayAppModule") {
	//	bind<DataSourceConfig>(KODEIN_TAG_DATA_SOURCE_CONFIG) with singleton { DataSourceConfig() }
	bind<FlywayService>() with singleton(ref = weakReference) {
		FlywayService(
			instance(
				KODEIN_TAG_DATA_SOURCE_CONFIG
			)
		)
	}
//	bind<DatabaseService>() with singleton { DatabaseService(instance(KODEIN_TAG_DATA_SOURCE_CONFIG)) }
}
