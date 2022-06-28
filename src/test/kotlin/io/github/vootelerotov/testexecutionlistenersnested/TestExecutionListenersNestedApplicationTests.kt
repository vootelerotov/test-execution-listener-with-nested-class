package io.github.vootelerotov.testexecutionlistenersnested

import io.github.vootelerotov.testexecutionlistenersnested.TestExecutionListenersNestedApplicationTests.MyTestComponent
import io.github.vootelerotov.testexecutionlistenersnested.TestExecutionListenersNestedApplicationTests.MyTestExecutionListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestComponent
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
import org.springframework.test.context.TestPropertySource

@SpringBootTest(classes = [MyTestComponent::class])
@TestExecutionListeners(value = [MyTestExecutionListener::class], mergeMode = MERGE_WITH_DEFAULTS)
@TestConstructor(autowireMode = ALL)
class TestExecutionListenersNestedApplicationTests(private val testComponent: MyTestComponent) {

  @Test
  fun worksWithoutNestedClassWithNewContext() {
    assertThat(testComponent.enabled).isTrue
  }

  @Nested
  inner class ClassWithSameContext {

    @Test
    fun noProblemo() {
      assertThat(testComponent.enabled).isTrue
    }
  }

  @Nested
  @TestPropertySource(properties = ["test=true"])
  inner class ClassWithDifferentContext {

    @Test
    fun problemo() {
      assertThat(testComponent.enabled).isTrue
    }
  }

  @TestComponent
  class MyTestComponent(var enabled: Boolean = false)

  class MyTestExecutionListener : TestExecutionListener {

    override fun beforeTestMethod(testContext: TestContext) {
      testContext.applicationContext.getBean(MyTestComponent::class.java).enabled = true
    }

    override fun afterTestMethod(testContext: TestContext) {
      testContext.applicationContext.getBean(MyTestComponent::class.java).enabled = false
    }
  }

}
