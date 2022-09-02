package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent

object RunXTestSuitesWithXTestsAchievement : SMTRunnerEventsListener, Achievement() {
    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        val testCounter = testsRoot.children.sumOf { it.children.size }
        if (testCounter >= requiredTestsInSuite()) {
            var progress = progress()
            progress += 1
            if (progress == nextStep()) {
                updateProgress(0)
                increaseLevel()
                showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '" + getName() + "' - Achievement")
            } else {
                val progressGroupBeforeUpdate = getProgressGroup()
                updateProgress(progress)
                val progressGroupAfterUpdate = getProgressGroup()
                if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                    showAchievementNotification(
                        "You are making progress on an achievement! You have already reached " + progressGroupAfterUpdate.second + "% of the next level of the '" + getName() + "' achievement!"
                    )
                }
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

    override fun getLevel(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("runXTestSuitesWithXTestsAchievementLevel", 0)
    }

    private fun increaseLevel() {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("runXTestSuitesWithXTestsAchievementLevel", (getLevel() + 1), 0)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("runXTestSuitesWithXTestsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("runXTestSuitesWithXTestsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Run " + nextStep() + " times test suites containing at least " + requiredTestsInSuite() + " tests"
    }

    override fun getName(): String {
        return "Tester - Advanced"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 50, 2 to 100, 3 to 250)
    }

    private fun requiredTestsInSuite(): Int {
        val level = getLevel()
        if (level > 1) {
            if (level > 2) {
                if (level > 3) {
                    return 3000
                }
                return 1000
            }
            return 500
        }
        return 100
    }
}