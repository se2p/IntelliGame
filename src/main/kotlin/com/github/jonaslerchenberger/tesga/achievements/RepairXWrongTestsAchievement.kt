package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset

object RepairXWrongTestsAchievement : SMTRunnerEventsListener, Achievement() {
    private var testsUnderObservation = hashMapOf<String, String>()
    private var classesUnderObservation = hashMapOf<String, String>()
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
        val pathToTest =
            PROJECT?.basePath + "/src/test/java/" + test.parent.name.replace(".", "/") + ".java"
        val pathToCode =
            PROJECT?.basePath + "/src/main/java/" + test.parent.name.dropLast(4).replace(".", "/")+ ".java"
        val testFile = File(pathToTest);
        val codeFile = File(pathToCode)
        if (key != null && testFile.exists() && codeFile.exists()) {
            val testFileContent = FileUtils.readFileToString(testFile, Charset.defaultCharset()).replace(System.getProperty("line.separator"), "")
            val codeFileContent = FileUtils.readFileToString(codeFile, Charset.defaultCharset()).replace(System.getProperty("line.separator"), "")
            if (test.magnitudeInfo == TestStateInfo.Magnitude.FAILED_INDEX) {
                if (!testsUnderObservation.containsKey(key) && !classesUnderObservation.containsKey(key)) {
                    testsUnderObservation[key] = testFileContent
                    classesUnderObservation[key] = codeFileContent
                }
            } else if (test.magnitudeInfo == TestStateInfo.Magnitude.PASSED_INDEX) {
                if (testsUnderObservation.containsKey(key) && classesUnderObservation.containsKey(key) &&
                    testsUnderObservation[key] != testFileContent && classesUnderObservation[key] == codeFileContent
                ) {
                    var progress = progress()
                    progress += 1
                    if (progress == nextStep()) {
                        showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the 'Test Fixer' Achievement")
                    }
                    updateProgress(progress)
                } else {
                    testsUnderObservation.remove(key)
                    classesUnderObservation.remove(key)
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
        return properties.getInt("repairedXTests", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("repairedXTests", progress, 0)
    }

    override fun getDescription(): String {
        return "Repair wrong tests"
    }

    override fun getName(): String {
        return "Test Fixer"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}