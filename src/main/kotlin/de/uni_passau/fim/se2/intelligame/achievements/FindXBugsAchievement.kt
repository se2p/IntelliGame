package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.readText
import com.intellij.psi.search.GlobalSearchScope
import de.uni_passau.fim.se2.intelligame.util.Util
import javax.swing.SwingUtilities

object FindXBugsAchievement : SMTRunnerEventsListener, Achievement() {
    private var testsUnderObservation = hashMapOf<String, String>()
    private var project: Project? = null

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        project = Util.getProject(testsRoot.locationUrl)
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestsCountInSuite(count: Int) = Unit

    override fun onTestStarted(test: SMTestProxy) {
        if (project == null) project = Util.getProject(test.locationUrl)
    }

    override fun onTestFinished(test: SMTestProxy) {
        SwingUtilities.invokeLater {
            val fileContent =
                test
                .getLocation(project!!, GlobalSearchScope.allScope(project!!))
                ?.virtualFile
                ?.readText()
                ?.replace("\n", "")
                ?.replace("\r", "")
            val key = test.locationUrl

            if (key != null && fileContent != null) {
                // If the test fails check if the file content was already saved before, if not add content
                if (test.magnitudeInfo == TestStateInfo.Magnitude.FAILED_INDEX
                    || test.magnitudeInfo == TestStateInfo.Magnitude.ERROR_INDEX) {
                    if (!testsUnderObservation.containsKey(key)) {
                        testsUnderObservation[key] = fileContent
                    }
                } else if (test.magnitudeInfo == TestStateInfo.Magnitude.PASSED_INDEX) {
                    // If test passes check if the content is still the same
                    if (testsUnderObservation.containsKey(key) &&
                        testsUnderObservation[key] == fileContent
                    ) {
                        var progress = progress()
                        progress++
                        handleProgress(progress, project)
                        testsUnderObservation.remove(key)
                    } else {
                        testsUnderObservation.remove(key)
                    }
                }
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
        return properties.getInt("testsFoundXBugs", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("testsFoundXBugs", progress, 0)
    }

    override fun getDescription(): String {
        return "Find bugs in the code with your tests. " +
                "Your test code should be the same between " +
                "the first failed test run and the first successful run."
    }

    override fun getName(): String {
        return "Bug Finder"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }

    override fun nextStep(): Int {
        if (progress() > 3) {
            if (progress() > 10) {
                if (progress() > 100) {
                    return 1000
                }
                return 100
            }
            return 10
        }
        return 3
    }

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java, Language.JavaScript)
    }
}