package com.github.itbasis.kodein.apps.flyway.services

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.apps.flyway.config.DataSourceFullAccessConfig
import com.github.itbasis.kodein.ex.ServiceManager
import com.mysql.cj.jdbc.MysqlDataSource
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal class DataSourceServiceTest {
	private class MySqlDataSourceService(dataSourceConfig: DataSourceConfig) :
		DataSourceService(dataSourceConfig) {

		override fun start() {
			val ds = MysqlDataSource()
			ds.user = dataSourceConfig.username
			dataSource = ds
		}
	}

	private class MySqlDataSourceFullAccessService(dataSourceConfig: DataSourceFullAccessConfig) :
		DataSourceFullAccessService(dataSourceConfig) {

		override fun start() {
			val ds = MysqlDataSource()
			ds.user = dataSourceConfig.username
			dataSource = ds
		}
	}

	@Before
	fun setUp() {
		System.clearProperty("DB_USERNAME")
		System.clearProperty("DB_FA_USERNAME")
	}

	@Test
	fun testMySqlDataSource() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { MySqlDataSourceService(instance()) }
		}
		val dsService: DataSourceService by kodein.instance()
		shouldThrow<UninitializedPropertyAccessException> {
			(dsService.dataSource as MysqlDataSource).user shouldBe "username"
		}
		ServiceManager(kodein).run()
		(dsService.dataSource as MysqlDataSource).user shouldBe "username"
	}

	@Test
	fun testMySqlDataSourceFullAccess() {
		System.setProperty("DB_FA_USERNAME", "username_fa")
		val kodein = Kodein {
			bind<DataSourceConfig>() with singleton { DataSourceConfig() }
			bind<DataSourceFullAccessConfig>() with singleton { DataSourceFullAccessConfig() }
			bind<DataSourceService>() with singleton { MySqlDataSourceService(instance()) }
			bind<DataSourceFullAccessService>() with singleton { MySqlDataSourceFullAccessService(instance()) }
		}
		val dsService: DataSourceService by kodein.instance()
		val dsFaService: DataSourceFullAccessService by kodein.instance()
		ServiceManager(kodein).run()
		(dsService.dataSource as MysqlDataSource).user shouldBe "username"
		(dsFaService.dataSource as MysqlDataSource).user shouldBe "username_fa"
	}
}
