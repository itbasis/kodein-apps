package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.clearSystemProperties
import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.mysql.cj.jdbc.MysqlDataSource
import io.kotlintest.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal class DataSourceServiceTest {
	private class MySqlDataSourceConfig : DataSourceConfig() {
		init {
			val ds = MysqlDataSource()
			ds.serverName = host
			ds.databaseName = dbName
			ds.port = port
			ds.user = username
			dataSource = ds
		}
	}

	@After
	fun tearDown() {
		clearSystemProperties()
	}

	@Before
	fun setUp() {
		clearSystemProperties()
	}

	@Test
	fun testMySqlDataSource() {
		val kodein = Kodein {
			bind() from singleton { MySqlDataSourceConfig() }
		}
		val dataSourceConfig: DataSourceConfig by kodein.instance()
		val dataSource = dataSourceConfig.dataSource as MysqlDataSource
		dataSource.user shouldBe "username"
		dataSource.getUrl() shouldBe "jdbc:mysql://localhost:0/db"
	}

	@Test
	fun testMySqlDataSourceChangeDbName() {
		System.setProperty("DB_NAME", "db1")

		val kodein = Kodein {
			bind() from singleton { MySqlDataSourceConfig() }
		}

		val dataSourceConfig: DataSourceConfig by kodein.instance()
		val dataSource = dataSourceConfig.dataSource as MysqlDataSource
		dataSource.getUrl() shouldBe "jdbc:mysql://localhost:0/db1"
	}
}
