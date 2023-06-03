package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import de.uni_passau.fim.se2.intelligame.util.Util

object RunXTestsAchievement : SMTRunnerEventsListener, Achievement() {
    private var project: Project? = null

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        project = Util.getProject(testsRoot.locationUrl)
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        var progress = progress()
        progress += getAllTests(testsRoot.children)
        handleProgress(progress, project)
    }

    fun getAllTests(tests: List<SMTestProxy>): Int {
        var number = 0
        for (test in tests) {
            if (test.isLeaf) {
                number++
            } else {
                number += getAllTests(test.children)
            }
        }

        return number
    }

    fun triggerAchievement(tests: Int) {
        var progress = progress()
        progress += tests
        handleProgress(progress, project)
    }

    override fun onTestsCountInSuite(count: Int) = Unit

    override fun onTestStarted(test: SMTestProxy) {
        if (project == null) project = Util.getProject(test.locationUrl)
    }

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

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java, Language.JavaScript)
    }
}