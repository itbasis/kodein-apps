package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.ex.AbstractService
import com.github.itbasis.kodein.ex.Service
import kotlin.reflect.KClass

abstract class AbstractDatabaseService(protected open val dataSourceService: DataSourceService) :
	AbstractService() {

	override fun shouldRunAfter(): List<KClass<out Service>>? = listOf(FlywayService::class)
}
