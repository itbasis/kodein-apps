package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.ex.AbstractService
import javax.sql.DataSource

abstract class DataSourceService(protected val dataSourceConfig: DataSourceConfig) :
	AbstractService() {

	lateinit var dataSource: DataSource
}
