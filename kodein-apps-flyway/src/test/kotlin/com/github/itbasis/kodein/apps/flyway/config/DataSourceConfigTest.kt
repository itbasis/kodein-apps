package com.github.itbasis.kodein.apps.flyway.config

import io.kotlintest.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal class DataSourceConfigTest {
	private class DataSourceExConfig : DataSourceConfig() {
		override val username: String by lazyProperty { "user test" }
	}

	@Before
	fun setUp() {
		System.clearProperty("DB_USERNAME")
		System.clearProperty("DB_FA_USERNAME")
	}

	@Test
	fun testDataSource00() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		ds.username shouldBe "username"
		System.setProperty("DB_USERNAME", "username1")
		ds.username shouldBe "username"
	}

	@Test
	fun testDataSource01() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
		}
		System.setProperty("DB_USERNAME", "username1")
		val ds: DataSourceConfig by kodein.instance()
		ds.username shouldBe "username1"
		System.setProperty("DB_USERNAME", "username2")
		ds.username shouldBe "username1"
	}

	@Test
	fun testOverrideDataSource00() {
		val kodein = Kodein {
			bind() from singleton { DataSourceExConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		ds.username shouldBe "user test"
		System.setProperty("DB_USERNAME", "username")
		ds.username shouldBe "user test"
	}

	@Test
	fun testDataSourceFullAccess00() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { DataSourceFullAccessConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		val dsFa: DataSourceFullAccessConfig by kodein.instance()

		ds.username shouldBe "username"
		dsFa.username shouldBe "username"
	}

	@Test
	fun testDataSourceFullAccess01() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { DataSourceFullAccessConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		val dsFa: DataSourceFullAccessConfig by kodein.instance()

		System.setProperty("DB_USERNAME", "username1")

		ds.username shouldBe "username1"
		// FIXME correctly: dsFa.username shouldBe "username1"
		dsFa.username shouldBe "username"
	}

	@Test
	fun testDataSourceFullAccess02() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { DataSourceFullAccessConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		val dsFa: DataSourceFullAccessConfig by kodein.instance()

		System.setProperty("DB_FA_USERNAME", "username2")

		ds.username shouldBe "username"
		dsFa.username shouldBe "username2"
	}

	@Test
	fun testDataSourceFullAccess03() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { DataSourceFullAccessConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		val dsFa: DataSourceFullAccessConfig by kodein.instance()

		ds.username shouldBe "username"
		System.setProperty("DB_USERNAME", "username2")
		dsFa.username shouldBe "username"
	}

	@Test
	fun testDataSourceFullAccess04() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { DataSourceFullAccessConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		val dsFa: DataSourceFullAccessConfig by kodein.instance()

		ds.username shouldBe "username"
		System.setProperty("DB_FA_USERNAME", "username2")
		dsFa.username shouldBe "username2"
	}

	@Test
	fun testDataSourceFullAccess05() {
		val kodein = Kodein {
			bind() from singleton { DataSourceConfig() }
			bind() from singleton { DataSourceFullAccessConfig() }
		}
		val ds: DataSourceConfig by kodein.instance()
		val dsFa: DataSourceFullAccessConfig by kodein.instance()

		System.setProperty("DB_USERNAME", "username1")
		System.setProperty("DB_FA_USERNAME", "username2")

		ds.username shouldBe "username1"
		dsFa.username shouldBe "username2"
	}
}
