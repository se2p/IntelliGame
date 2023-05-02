package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset

object FindXBugsAchievement : SMTRunnerEventsListener, Achievement() {
    private var testsUnderObservation = hashMapOf<String, String>()
    private var PROJECT: Project? = null

    fun setProject(project: Project) {
        PROJECT = project
    }

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestsCountInSuite(count: Int) = Unit

    override fun onTestStarted(test: SMTestProxy) = Unit

    override fun onTestFinished(test: SMTestProxy) {
        val key = test.locationUrl
        // Find the correct absolute path to the test class therefore the test-identifier and prefix is removed
        val path =
            PROJECT?.basePath + "/src/test/java/" + (test.locationUrl?.removeRange(
                test.locationUrl!!.lastIndexOf("/"),
                test.locationUrl!!.length
            )
                ?.removePrefix("java:test://")
                ?.replace(".", "/")
                ?: "") + ".java"
        val testFile = File(path)
        // Check if test file exists (if the path was built correctly)
        if (key != null && testFile.exists()) {
            // File content as String
            val fileContent = FileUtils.readFileToString(testFile, Charset.defaultCharset())
                .replace(System.getProperty("line.separator"), "")
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
                    handleProgress(progress)
                    testsUnderObservation.remove(key)
                } else {
                    testsUnderObservation.remove(key)
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
                    return 1000;
                }
                return 100;
            }
            return 10;
        }
        return 3;
    }
}