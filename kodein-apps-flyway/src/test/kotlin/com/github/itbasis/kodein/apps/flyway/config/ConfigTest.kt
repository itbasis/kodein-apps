package com.github.itbasis.kodein.apps.flyway.config

import io.kotlintest.Description
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class ConfigTest : FunSpec() {
	private object TestObjectConfig : AbstractConfig(prefix = "TEST_OBJECT") {
		val field1 by lazyProperty { "default_test" }
		val field2 by lazyProperty { "default_test" }
	}

	private class TestClassConfig : AbstractConfig(prefix = "testClass") {
		val field1 by lazyProperty { "default_test" }
	}

	override fun beforeTest(description: Description) {
		arrayOf("TEST_OBJECT_FIELD1", "TEST_OBJECT_FIELD2", "TEST_CLASS_FIELD1").forEach {
			System.clearProperty(it)
		}
	}

	init {
		test("testConfig") {
			System.setProperty("TEST_OBJECT_FIELD1", "field1")

			TestObjectConfig.field1 shouldBe "field1"
			TestObjectConfig.field2 shouldBe "default_test"

			System.setProperty("TEST_OBJECT_FIELD2", "field2")
			TestObjectConfig.field2 shouldBe "default_test"
		}

		test("testClass") {
			TestClassConfig().field1 shouldBe "default_test"

			System.setProperty("TEST_CLASS_FIELD1", "field1")
			TestClassConfig().field1 shouldBe "field1"

			val testClassConfig = TestClassConfig()
			testClassConfig.field1 shouldBe "field1"
			System.setProperty("TEST_CLASS_FIELD1", "field2")
			testClassConfig.field1 shouldBe "field1"
		}
	}
}
