package com.github.jonaslerchenberger.tesga.achievements

import com.github.difflib.DiffUtils
import com.github.difflib.patch.DeltaType
import com.github.difflib.patch.Patch
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files


object RefactorCodeAchievement : SMTRunnerEventsListener, Achievement() {
    private var testsUnderObservation = hashMapOf<String, String>()
    private var classesUnderObservation = hashMapOf<String, List<String>>()
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
        val pathToTest = PROJECT?.basePath + "/src/test/java/" + test.parent.name.replace(".", "/") + ".java"
        val pathToCode =
            PROJECT?.basePath + "/src/main/java/" + test.parent.name.dropLast(4).replace(".", "/") + ".java"
        val testFile = File(pathToTest);
        val codeFile = File(pathToCode)

        if (key != null && testFile.exists() && codeFile.exists()) {
//            val testFileContent = FileUtils.readFileToString(testFile, "utf-8")
//            val codeFileContent = FileUtils.lineIterator(codeFile, "utf-8")
            val testFileContent = FileUtils.readFileToString(testFile, "utf-8")
            val codeFileContent = Files.readAllLines(codeFile.toPath())
            if (test.magnitudeInfo == TestStateInfo.Magnitude.PASSED_INDEX) {
                if (!testsUnderObservation.containsKey(key) && !classesUnderObservation.containsKey(key)) {
                    testsUnderObservation[key] = testFileContent
                    classesUnderObservation[key] = codeFileContent
                } else if (testsUnderObservation[key] == testFileContent){
//                    val countChangesCommandVisitor = CountChangesCommandVisitor()
//                    val fileToCompare = classesUnderObservation[key]
//
//                    while (fileToCompare!!.hasNext() || codeFileContent.hasNext()) {
//                        var left = ""
//                        if (fileToCompare.hasNext()) {
//                            left = fileToCompare.nextLine()
//                        }
//                        left += "\n"
//
//                        var right = ""
//                        if (codeFileContent.hasNext()) {
//                            right = codeFileContent.nextLine()
//                        }
//                        right += "\n"
//
//                        val comparator = StringsComparator(left, right)
//                        if (comparator.script.lcsLength > kotlin.math.max(left.length.toDouble(), right.length * 0.4)) {
//                            comparator.script.visit(countChangesCommandVisitor)
//                        } else {
//                            val leftComparator = StringsComparator(left, "\n")
//                            leftComparator.script.visit(countChangesCommandVisitor)
//                            val rightComparator = StringsComparator("\n", right)
//                            rightComparator.script.visit(countChangesCommandVisitor)
//                        }
//                    }
//                    var counter = countChangesCommandVisitor.counter

                    val fileToCompare = classesUnderObservation[key]
                    val patch: Patch<String> = DiffUtils.diff(fileToCompare, codeFileContent)
                    var counter = 0
                    for (delta in patch.deltas) {
                        if (delta.type.equals(DeltaType.INSERT)) {
                            counter += delta.target.lines.size
                        } else if (delta.type.equals(DeltaType.DELETE)) {
                            counter += delta.source.lines.size
                        } else if (delta.type.equals(DeltaType.CHANGE)) {
                            counter += delta.source.lines.size
                            counter += delta.target.lines.size
                        }
                    }
                    var progress = progress()
                    progress += counter
                    if (progress >= nextStep()) {
                        showAchievementNotification("Congratulations! You unlocked the 'Shine in new splendour' Achievement")
                    }
                    updateProgress(progress)
                    classesUnderObservation[key] = codeFileContent
                }
            } else if (test.magnitudeInfo == TestStateInfo.Magnitude.FAILED_INDEX) {
                if (testsUnderObservation.containsKey(key) && classesUnderObservation.containsKey(key)) {
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
        return properties.getInt("refactorCodeAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("refactorCodeAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Change lines between two consecutive passing test runs."
    }

    override fun getName(): String {
        return "Shine in new splendour"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 5, 1 to 50, 2 to 500, 3 to 2500)
    }
}