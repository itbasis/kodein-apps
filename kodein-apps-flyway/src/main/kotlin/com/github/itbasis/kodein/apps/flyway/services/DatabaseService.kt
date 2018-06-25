package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.ex.AbstractService
import com.github.itbasis.kodein.ex.Service
import kotlin.reflect.KClass

open class DatabaseService(protected val dataSourceConfig: DataSourceConfig) : AbstractService() {
	override fun start() {
		throw UnsupportedOperationException()
	}

	override fun shouldRunAfter(): List<KClass<out Service>>? = listOf(FlywayService::class)
}
