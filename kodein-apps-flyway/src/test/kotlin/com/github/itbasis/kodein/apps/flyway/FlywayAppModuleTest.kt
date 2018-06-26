package com.github.itbasis.kodein.apps.flyway

import com.github.itbasis.kodein.apps.flyway.config.DataSourceConfig
import com.github.itbasis.kodein.apps.flyway.services.DatabaseService
import com.github.itbasis.kodein.ex.ServiceManager
import com.mysql.cj.jdbc.MysqlDataSource
import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.EmbeddedMysql.Builder
import com.wix.mysql.config.DownloadConfig
import com.wix.mysql.config.MysqldConfig
import com.wix.mysql.config.SchemaConfig
import com.wix.mysql.distribution.Version.v5_7_latest
import io.kotlintest.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters.JVM
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.sql.Connection

@FixMethodOrder(JVM)
internal class FlywayAppModuleTest {

	private class MySqlDataSourceConfig : DataSourceConfig<MysqlDataSource>() {
		init {
			val ds = MysqlDataSource()
			ds.serverName = host
			ds.databaseName = dbName
			ds.user = username
			ds.setPassword(password)
			ds.port = port
			dataSource = ds
		}
	}

	private class MySqlFullAccessDataSourceConfig : DataSourceConfig<MysqlDataSource>("DB_FA") {
		init {
			val ds = MysqlDataSource()
			ds.serverName = host
			ds.databaseName = dbName
			ds.user = username
			ds.setPassword(password)
			ds.port = port
			dataSource = ds
		}
	}

	private class MySqlDatabaseService(dataSourceConfig: DataSourceConfig<MysqlDataSource>) :
		DatabaseService<MysqlDataSource>(dataSourceConfig) {

		lateinit var connection: Connection

		override fun start() {
			connection = dataSourceConfig.dataSource.connection
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

	private var embeddedDb: EmbeddedMysql

	init {
		val dbSchema = SchemaConfig.aSchemaConfig("db").withCommands(
			"CREATE USER IF NOT EXISTS 'user_rw'@'localhost' IDENTIFIED BY 'user_rw_pwd';",
			"GRANT ALL ON db.* TO 'user_rw'@'localhost';"
		).build()
		val mysqlCfg = MysqldConfig.aMysqldConfig(v5_7_latest).withFreePort().build()

		embeddedDb =
			Builder(mysqlCfg, DownloadConfig.aDownloadConfig().build()).addSchema(dbSchema).start()
	}

	@Test
	fun testDataSourceSingle() {
		System.setProperty("DB_USERNAME", embeddedDb.config.username)
		System.setProperty("DB_PASSWORD", embeddedDb.config.password)
		System.setProperty("DB_PORT", embeddedDb.config.port.toString())

		val kodein = Kodein {
			bind<DataSourceConfig<MysqlDataSource>>(KODEIN_TAG_DATA_SOURCE_CONFIG) with singleton { MySqlDataSourceConfig() }
			bind<MySqlDatabaseService>() with singleton {
				MySqlDatabaseService(
					instance(
						KODEIN_TAG_DATA_SOURCE_CONFIG
					)
				)
			}

			import(flywayAppModule)
		}
		ServiceManager(kodein).run()

		val databaseService: MySqlDatabaseService by kodein.instance()

		val stmt = databaseService.connection.createStatement()
		val rs = stmt.executeQuery("SELECT count(*), CURRENT_USER() FROM test_tbl")
		rs.next()
		rs.getInt(1) shouldBe 2
		rs.getString(2) shouldBe "auser@%"
	}

	@Test
	fun testDataSourceSingle1() {
		System.setProperty("DB_USERNAME", embeddedDb.config.username)
		System.setProperty("DB_PASSWORD", embeddedDb.config.password)
		System.setProperty("DB_PORT", embeddedDb.config.port.toString())

		val kodein = Kodein {
			bind<DataSourceConfig<MysqlDataSource>>(KODEIN_TAG_DATA_SOURCE_CONFIG) with singleton { MySqlDataSourceConfig() }
			bind<MySqlDatabaseService>() with singleton {
				MySqlDatabaseService(
					instance(
						KODEIN_TAG_DATA_SOURCE_CONFIG
					)
				)
			}

			import(flywayAppModule)
		}
		ServiceManager(kodein).run()

		val databaseService: MySqlDatabaseService by kodein.instance()

		val stmt = databaseService.connection.createStatement()
		val rs = stmt.executeQuery("SELECT count(*), CURRENT_USER() FROM test_tbl")
		rs.next()
		rs.getInt(1) shouldBe 2
		rs.getString(2) shouldBe "auser@%"
	}

	@Test
	fun testDataSourceMulti() {
		System.setProperty("DB_FA_USERNAME", embeddedDb.config.username)
		System.setProperty("DB_FA_PASSWORD", embeddedDb.config.password)
		System.setProperty("DB_FA_PORT", embeddedDb.config.port.toString())

		System.setProperty("DB_USERNAME", embeddedDb.config.username)
		System.setProperty("DB_PASSWORD", embeddedDb.config.password)
		System.setProperty("DB_PORT", embeddedDb.config.port.toString())

		val kodein = Kodein {
			bind<DataSourceConfig<MysqlDataSource>>(KODEIN_TAG_DATA_SOURCE_CONFIG) with singleton { MySqlFullAccessDataSourceConfig() }
			bind<DataSourceConfig<MysqlDataSource>>("dsRW") with singleton { MySqlDataSourceConfig() }
			bind<MySqlDatabaseService>() with singleton { MySqlDatabaseService(instance("dsRW")) }

			import(flywayAppModule)
		}
		ServiceManager(kodein).run()

		val databaseService: MySqlDatabaseService by kodein.instance()

		val stmt = databaseService.connection.createStatement()
		val rs = stmt.executeQuery("SELECT count(*), CURRENT_USER() FROM test_tbl")
		rs.next()
		rs.getInt(1) shouldBe 2
		rs.getString(2) shouldBe "auser@%"
	}

	@Test
	fun testDataSourceMultiS() {
		System.setProperty("DB_FA_USERNAME", embeddedDb.config.username)
		System.setProperty("DB_FA_PASSWORD", embeddedDb.config.password)
		System.setProperty("DB_FA_PORT", embeddedDb.config.port.toString())

		System.setProperty("DB_USERNAME", "user_rw")
		System.setProperty("DB_PASSWORD", "user_rw_pwd")
		System.setProperty("DB_PORT", embeddedDb.config.port.toString())

		val kodein = Kodein {
			bind<DataSourceConfig<MysqlDataSource>>(KODEIN_TAG_DATA_SOURCE_CONFIG) with singleton { MySqlFullAccessDataSourceConfig() }
			bind<DataSourceConfig<MysqlDataSource>>("dsRW") with singleton { MySqlDataSourceConfig() }
			bind<MySqlDatabaseService>() with singleton { MySqlDatabaseService(instance("dsRW")) }

			import(flywayAppModule)
		}
		ServiceManager(kodein).run()

		val databaseService: MySqlDatabaseService by kodein.instance()

		val stmt = databaseService.connection.createStatement()
		val rs = stmt.executeQuery("SELECT count(*), CURRENT_USER() FROM test_tbl")
		rs.next()
		rs.getInt(1) shouldBe 2
		rs.getString(2) shouldBe "user_rw@localhost"
	}
}
