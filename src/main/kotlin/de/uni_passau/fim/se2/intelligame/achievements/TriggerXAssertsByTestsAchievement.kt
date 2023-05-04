package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.DataManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.roots.FileIndexFacade
import com.intellij.psi.search.ProjectScopeImpl
import javax.swing.SwingUtilities

object TriggerXAssertsByTestsAchievement : SMTRunnerEventsListener,
    Achievement() {
    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestsCountInSuite(count: Int) = Unit

    override fun onTestStarted(test: SMTestProxy) = Unit

    override fun onTestFinished(test: SMTestProxy) {
        if (test.isPassed) {
            ApplicationManager.getApplication().isReadAccessAllowed
            val project = DataManager.getInstance().dataContextFromFocus.resultSync.getData(PlatformDataKeys.PROJECT)!!
            SwingUtilities.invokeLater {
                val testText =
                    test.getLocation(project, ProjectScopeImpl(project, FileIndexFacade.getInstance(project)))!!
                    .psiElement.text
                val count = "assert".toRegex().findAll(testText).count()
                var progress = progress()
                progress += count
                handleProgress(progress)
            }
        }
    }

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
        return properties.getInt("assertTriggeredByTest", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("assertTriggeredByTest", progress, 0)
    }

    override fun getDescription(): String {
        return "Asserts have to be successfully passed by tests"
    }

    override fun getName(): String {
        return "Assert and Tested"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 100, 2 to 1000, 3 to 10000)
    }
}