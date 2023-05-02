package de.uni_passau.fim.se2.intelligame.achievements

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

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) = Unit

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        for (test in testsRoot.children) {
            val key = test.locationUrl
            val fileUrl = test.locationUrl
                ?.removePrefix("java:suite://")
                ?.replace(".", "/")
                ?: ""
            val pathToCode = PROJECT?.basePath + "/src/main/java/" + fileUrl.dropLast(4) + ".java"
            val pathToTest = PROJECT?.basePath + "/src/test/java/" + fileUrl.replace(".", "/") + ".java"

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
                    } else if (testsUnderObservation[key] == testFileContent) {
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
                            updateProgress(progress)
                            showAchievementNotification("Congratulations! You unlocked level " +
                                    getLevel() + " of the  '" + getName() + "' achievement!")
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
                        classesUnderObservation[key] = codeFileContent
                    }
                } else if (test.magnitudeInfo == TestStateInfo.Magnitude.FAILED_INDEX
                    && testsUnderObservation.containsKey(key)
                    && classesUnderObservation.containsKey(key)) {
                    testsUnderObservation.remove(key)
                    classesUnderObservation.remove(key)
                }
            }
        }
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