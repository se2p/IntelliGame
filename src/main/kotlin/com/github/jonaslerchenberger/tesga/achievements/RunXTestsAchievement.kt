package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent

object RunXTestsAchievement : SMTRunnerEventsListener, Achievement() {
    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        var progress = progress()
        progress += testsRoot.children.sumOf { it.children.size }
        if (progress >= nextStep()) {
            updateProgress(progress)
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '" + getName() + "' - Achievement")
        } else {
            val progressGroupBeforeUpdate = getProgressGroup()
            updateProgress(progress)
            val progressGroupAfterUpdate = getProgressGroup()
            if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                showAchievementNotification(
                    "You are making progress on an achievement! Only " + progressGroupAfterUpdate.second + "% are still missing for the next level of the '" + getName() + "' Achievement!"
                )
            }
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
        return properties.getInt("runXTestsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("runXTestsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Every single test execution counts as progress"
    }

    override fun getName(): String {
        return "Test Executor"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 100, 2 to 1000, 3 to 10000)
    }
}