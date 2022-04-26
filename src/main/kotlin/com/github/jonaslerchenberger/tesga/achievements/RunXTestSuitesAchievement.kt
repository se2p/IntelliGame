package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent

object RunXTestSuitesAchievement : SMTRunnerEventsListener, Achievement() {
    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        var progress = progress()
        progress += 1
        handleProgress(progress)
        if (GetXLineCoverageInClassesWithYLinesAchievement.getLevel() == 0 && GetXLineCoverageInClassesWithYLinesAchievement.getClassesWhichFulfillRequirements() == "") {
            showAchievementNotification("You might want to run your tests with coverage to see if you missed anything!")
        }
    }

    override fun onTestsCountInSuite(count: Int) {
    }

    override fun onTestStarted(test: SMTestProxy) {
    }

    override fun onTestFinished(test: SMTestProxy) {
    }

    override fun onTestFailed(test: SMTestProxy) {
    }

    override fun onTestIgnored(test: SMTestProxy) {
    }

    override fun onSuiteFinished(suite: SMTestProxy) {
    }

    override fun onSuiteStarted(suite: SMTestProxy) {
    }

    override fun onCustomProgressTestsCategory(categoryName: String?, testCount: Int) {
    }

    override fun onCustomProgressTestStarted() {
    }

    override fun onCustomProgressTestFailed() {
    }

    override fun onCustomProgressTestFinished() {
    }

    override fun onSuiteTreeNodeAdded(testProxy: SMTestProxy?) {
    }

    override fun onSuiteTreeStarted(suite: SMTestProxy?) {
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("runXTestSuitesAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("runXTestSuitesAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Can be achieved by running tests"
    }

    override fun getName(): String {
        return "Mr Tester"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 100, 2 to 1000, 3 to 10000)
    }
}