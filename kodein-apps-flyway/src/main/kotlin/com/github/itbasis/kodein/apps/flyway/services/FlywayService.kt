package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.ex.AbstractService
import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.configuration.ConfigUtils

@Suppress("MemberVisibilityCanBePrivate")
open class FlywayService(private val dataSourceService: DataSourceConfig) : AbstractService() {

	override val finalizeAfterStart = true

	private val flyway: Flyway by lazy { Flyway() }

	override fun start() {
		flyway.dataSource = dataSourceService.dataSource
		flyway.configure(flywayConfigure())
		flyway.migrate()
	}

	protected open fun flywayConfigure(): Map<String, String> {
		return mapOf(
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_NAME" to dataSourceService.dbName,
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_USER" to dataSourceService.username,
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_PASSWORD" to dataSourceService.password
		)
	}
}
