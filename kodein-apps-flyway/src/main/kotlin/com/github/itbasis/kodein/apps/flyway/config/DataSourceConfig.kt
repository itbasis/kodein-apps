package com.github.itbasis.kodein.apps.flyway.config

open class DataSourceConfig(prefix: String = "DB") : AbstractConfig(prefix) {
	open val name: String by lazyProperty { "db" }
	open val host: String by lazyProperty { "localhost" }
	open val port: Int by lazyProperty { 0 }

	open val username: String by lazyProperty { "username" }
	open val password: String by lazyProperty(security = true) { "password" }
}
