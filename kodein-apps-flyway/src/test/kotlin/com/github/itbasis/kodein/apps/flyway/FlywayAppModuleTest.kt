package com.github.itbasis.kodein.apps.flyway

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.apps.flyway.config.DataSourceFullAccessConfig
import com.github.itbasis.kodein.apps.flyway.services.AbstractDatabaseService
import com.github.itbasis.kodein.apps.flyway.services.DataSourceFullAccessService
import com.github.itbasis.kodein.apps.flyway.services.DataSourceService
import com.github.itbasis.kodein.ex.ServiceManager
import com.mysql.cj.jdbc.MysqlDataSource
import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.EmbeddedMysql.Builder
import com.wix.mysql.config.DownloadConfig
import com.wix.mysql.config.MysqldConfig
import com.wix.mysql.config.SchemaConfig
import com.wix.mysql.distribution.Version.v5_7_latest
import io.kotlintest.shouldBe
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.sql.Connection

internal class FlywayAppModuleTest {
	private open class MySqlDataSourceService(dataSourceConfig: DataSourceConfig) :
		DataSourceService(dataSourceConfig) {

		override fun start() {
			with(MysqlDataSource()) {
				setURL("jdbc:mysql://${dataSourceConfig.host}:${dataSourceConfig.port}/${dataSourceConfig.name}")
				user = dataSourceConfig.username
				setPassword(dataSourceConfig.password)
				dataSource = this
			}
		}
	}

	private class MySqlDataSourceFullAccessService(dataSourceConfig: DataSourceFullAccessConfig) :
		DataSourceFullAccessService(dataSourceConfig) {

		override fun start() {
			with(MysqlDataSource()) {
				setURL("jdbc:mysql://${dataSourceConfig.host}:${dataSourceConfig.port}/${dataSourceConfig.name}")
				user = dataSourceConfig.username
				setPassword(dataSourceConfig.password)
				dataSource = this
			}

		}
	}

	private class MySqlDatabaseService(override val dataSourceService: DataSourceService) :
		AbstractDatabaseService(dataSourceService) {

		public lateinit var connection: Connection

		override fun start() {
			val ds: MysqlDataSource = dataSourceService.dataSource as MysqlDataSource
			connection = ds.connection
		}
	}

	var embeddedDb: EmbeddedMysql

	init {
		val dbSchema = SchemaConfig.aSchemaConfig("test").build()
		val mysqlCfg = MysqldConfig.aMysqldConfig(v5_7_latest).withFreePort().build()

		embeddedDb =
			Builder(mysqlCfg, DownloadConfig.aDownloadConfig().build()).addSchema(dbSchema).start()
		System.setProperty("DB_NAME", dbSchema.name)
		System.setProperty("DB_USERNAME", mysqlCfg.username)
		System.setProperty("DB_PASSWORD", mysqlCfg.password)
		System.setProperty("DB_PORT", mysqlCfg.port.toString())
//
		System.setProperty("DB_FA_NAME", dbSchema.name)
		System.setProperty("DB_FA_USERNAME", mysqlCfg.username)
		System.setProperty("DB_FA_PASSWORD", mysqlCfg.password)
		System.setProperty("DB_FA_PORT", mysqlCfg.port.toString())
	}

	@Test
	fun testFlywayAppModule() {
		val kodein = Kodein {
			bind<DataSourceConfig>() with singleton { DataSourceConfig() }
			bind<DataSourceFullAccessConfig>() with singleton { DataSourceFullAccessConfig() }
			bind<DataSourceService>() with singleton { MySqlDataSourceService(instance()) }
			bind<DataSourceFullAccessService>() with singleton { MySqlDataSourceFullAccessService(instance()) }
			bind<MySqlDatabaseService>() with singleton { MySqlDatabaseService(instance()) }

			import(flywayAppModule)
		}
		ServiceManager(kodein).run()

		val databaseService by kodein.instance<MySqlDatabaseService>()
		val stmt = databaseService.connection.createStatement()
		val rs = stmt.executeQuery("SELECT count(*) FROM test_tbl")
		rs.next()
		rs.getInt(1) shouldBe 2
	}
}
