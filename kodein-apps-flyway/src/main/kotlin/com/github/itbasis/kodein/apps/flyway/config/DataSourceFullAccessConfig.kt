package com.github.itbasis.kodein.apps.flyway.config

open class DataSourceFullAccessConfig(prefix: String = "DB_FA") : DataSourceConfig(prefix) {
	override val username: String by lazyProperty { super.username }
	override val password: String by lazyProperty(security = true) { super.password }
}
