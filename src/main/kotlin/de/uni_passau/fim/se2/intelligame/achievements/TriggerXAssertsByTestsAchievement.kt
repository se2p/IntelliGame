package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent

object TriggerXAssertsByTestsAchievement : SMTRunnerEventsListener,
    Achievement() {
    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestsCountInSuite(count: Int) = Unit

    override fun onTestStarted(test: SMTestProxy) = Unit

    override fun onTestFinished(test: SMTestProxy) = Unit

    override fun onTestFailed(test: SMTestProxy) {
        if (test.stacktrace!!.startsWith("java.lang.AssertionError")) {
            var progress = progress()
            progress += 1
            handleProgress(progress)
        }
    }

    override fun onTestIgnored(test: SMTestProxy) = Unit

    override fun onSuiteFinished(suite: SMTestProxy) = Unit

    override fun onSuiteStarted(suite: SMTestProxy) = Unit

    override fun onCustomProgressTestsCategory(categoryName: String?, testCount: Int) = Unit

    override fun onCustomProgressTestStarted() = Unit

    override fun onCustomProgressTestFailed() = Unit

    override fun onCustomProgressTestFinished() = Unit

    override fun onSuiteTreeNodeAdded(testProxy: SMTestProxy?) = Unit

    override fun onSuiteTreeStarted(suite: SMTestProxy?) = Unit

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("assertTriggeredByTest", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("assertTriggeredByTest", progress, 0)
    }

    override fun getDescription(): String {
        return "Asserts have to be triggered by tests"
    }

    override fun getName(): String {
        return "Assert and Tested"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}