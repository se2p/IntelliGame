package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent

object RunXTestSuitesWithXTestsAchievement : SMTRunnerEventsListener, Achievement() {
    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        val testCounter = RunXTestsAchievement.getAllTests(testsRoot.children)
        triggerAchievement(testCounter)
    }

    fun triggerAchievement(tests: Int) {
        if (tests >= requiredTestsInSuite()) {
            var progress = progress()
            progress += 1
            if (progress == nextStep()) {
                updateProgress(0)
                increaseLevel()
                showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '" +
                        getName() + "' - Achievement")
            } else {
                val progressGroupBeforeUpdate = getProgressGroup()
                updateProgress(progress)
                val progressGroupAfterUpdate = getProgressGroup()
                if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                    showAchievementNotification(
                        "You are making progress on an achievement! You have already reached " +
                                progressGroupAfterUpdate.second + "% of the next level of the '" +
                                getName() + "' achievement!"
                    )
                }
            }
        }
        refreshWindow()
    }

    override fun onTestsCountInSuite(count: Int) = Unit

    override fun onTestStarted(test: SMTestProxy) = Unit

    override fun onTestFinished(test: SMTestProxy) = Unit

    override fun onTestFailed(test: SMTestProxy) = Unit

    override fun onTestIgnored(test: SMTestProxy) = Unit

    override fun onSuiteFinished(suite: SMTestProxy) = Unit

    override fun onSuiteStarted(suite: SMTestProxy) = Unit

    override fun onCustomProgressTestsCategory(categoryName: String?, testCount: Int) = Unit

    override fun onCustomProgressTestStarted() = Unit

    override fun onCustomProgressTestFailed() = Unit

    override fun onCustomProgressTestFinished() = Unit

    override fun onSuiteTreeNodeAdded(testProxy: SMTestProxy?) = Unit

    override fun onSuiteTreeStarted(suite: SMTestProxy?) = Unit

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
        return "The Tester - Advanced"
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

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java, Language.JavaScript)
    }
}