package com.github.jonaslerchenberger.tesga.achievements

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

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
    }

    override fun onTestsCountInSuite(count: Int) {
    }

    override fun onTestStarted(test: SMTestProxy) {
    }

    override fun onTestFinished(test: SMTestProxy) {
        val key = test.locationUrl
        val path =
            PROJECT?.basePath + "/src/test/java/" + test.parent.name.replace(".", "/") + ".java"
        val testFile = File(path);
        if (key != null && testFile.exists()) {
            val fileContent = FileUtils.readFileToString(testFile, Charset.defaultCharset()).replace(System.getProperty("line.separator"), "")
            if (test.magnitudeInfo == TestStateInfo.Magnitude.FAILED_INDEX) {
                if (!testsUnderObservation.containsKey(key)) {
                    testsUnderObservation[key] = fileContent
                }
            } else if (test.magnitudeInfo == TestStateInfo.Magnitude.PASSED_INDEX) {
                if (testsUnderObservation.containsKey(key) &&
                    testsUnderObservation[key] == fileContent
                ) {
                    var progress = progress()
                    progress += 1
                    if (progress == nextStep()) {
                        showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the 'Bug Finder' Achievement")
                    }
                    updateProgress(progress)
                } else {
                    testsUnderObservation.remove(key)
                }
            }
        }
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
        return properties.getInt("testsFoundXBugs", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("testsFoundXBugs", progress, 0)
    }

    override fun getDescription(): String {
        return "Find bugs in the code with your tests. "
//                "Your test code should be the same between " +
//                "the first failed test run and the first successful run."
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