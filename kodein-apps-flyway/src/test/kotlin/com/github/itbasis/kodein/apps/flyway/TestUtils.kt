package com.github.itbasis.kodein.apps.flyway

internal fun clearSystemProperties() {
	System.getProperties().filter { entry ->
		(entry.key as String).startsWith("DB_")
	}.keys.forEach { key ->
		System.clearProperty(key as String)
	}
}
