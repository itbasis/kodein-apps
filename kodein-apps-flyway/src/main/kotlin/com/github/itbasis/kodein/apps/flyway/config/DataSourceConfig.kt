package com.github.itbasis.kodein.apps.flyway.config

import ru.itbasis.kotlin.utils.lazyProperty
import javax.sql.DataSource

open class DataSourceConfig(prefix: String = "DB") {

	open val dbName: String by lazyProperty { "db" }
	open val host: String by lazyProperty(prefix = prefix) { "localhost" }
	open val port: Int by lazyProperty(prefix = prefix) { 0 }

	open val username: String by lazyProperty(prefix = prefix) { "username" }
	open val password: String by lazyProperty(prefix = prefix, security = true) { "password" }

	open lateinit var dataSource: DataSource

}
