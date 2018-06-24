package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.config.DataSourceFullAccessConfig

abstract class DataSourceFullAccessService(dataSourceFullAccessConfig: DataSourceFullAccessConfig) :
	DataSourceService(dataSourceConfig = dataSourceFullAccessConfig)
