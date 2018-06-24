package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.apps.flyway.config.DataSourceFullAccessConfig
import com.github.itbasis.kodein.ex.AbstractService
import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.configuration.ConfigUtils

@Suppress("MemberVisibilityCanBePrivate")
open class FlywayService(
	private val dataSourceFullAccessService: DataSourceFullAccessService,
	private val dataSourceFullAccessConfig: DataSourceFullAccessConfig,
	private val dataSourceConfig: DataSourceConfig
) : AbstractService() {

	override val finalizeAfterStart = true

	private val flyway: Flyway by lazy { Flyway() }

	override fun start() {
		flyway.dataSource = dataSourceFullAccessService.dataSource
		flyway.configure(flywayConfigure())
		flyway.migrate()
	}

	protected open fun flywayConfigure(): Map<String, String> {
		return mapOf(
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_FA_NAME" to dataSourceFullAccessConfig.name,
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_FA_USER" to dataSourceFullAccessConfig.username,
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_FA_PASSWORD" to dataSourceFullAccessConfig.password,
//
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_NAME" to dataSourceConfig.name,
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_USER_RW" to dataSourceConfig.username,
			ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX + "DB_PASSWORD_RW" to dataSourceConfig.password
		)
	}
}
