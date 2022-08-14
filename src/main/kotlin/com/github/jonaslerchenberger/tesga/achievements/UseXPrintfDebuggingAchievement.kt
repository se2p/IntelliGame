package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.io.File


object UseXPrintfDebuggingAchievement : SMTRunnerEventsListener, Achievement() {
    private var classesUnderObservation = hashMapOf<String, Int>()
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
        if (key != null) {
            if (test.magnitudeInfo == TestStateInfo.Magnitude.FAILED_INDEX) {
                val fileUrl = (test.locationUrl?.removeRange(test.locationUrl!!.lastIndexOf("/"), test.locationUrl!!.length)
                    ?.removePrefix("java:test://")
                    ?.replace(".", "/")
                    ?: "")
                val pathToCode = PROJECT?.basePath + "/src/main/java/" + fileUrl.dropLast(4) + ".java"
                val classUnderTest = File(pathToCode);
                if (classUnderTest.exists()) {
                    val printlnCounter = countPrintls(classUnderTest.readText())
                    if (classesUnderObservation.containsKey(key) && classesUnderObservation[key]!! < printlnCounter) {
                        var progress = progress()
                        progress += 1
                        handleProgress(progress)
                    }
                    classesUnderObservation[key] = printlnCounter
                }
            } else if (test.magnitudeInfo == TestStateInfo.Magnitude.PASSED_INDEX) {
                if (classesUnderObservation.containsKey(key)) {
                    classesUnderObservation.remove(test.locationUrl)
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
        return properties.getInt("usedXPrintfDebugging", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("usedXPrintfDebugging", progress, 0)
    }

    override fun getDescription(): String {
        return "Use System.out.println method to debug your code."
    }

    override fun getName(): String {
        return "Console is the new Debug Mode"
    }

    private fun countPrintls(string: String): Int {
        return string.split("System.out.print").dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}