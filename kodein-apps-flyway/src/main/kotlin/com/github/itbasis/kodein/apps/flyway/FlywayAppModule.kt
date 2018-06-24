package com.github.itbasis.kodein.apps.flyway

import com.github.itbasis.kodein.apps.flyway.services.FlywayService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.weakReference

val flywayAppModule = Kodein.Module(name = "FlywayAppModule") {
	bind<FlywayService>() with singleton(ref = weakReference) {
		FlywayService(
			instance(), instance(), instance()
		)
	}
}
