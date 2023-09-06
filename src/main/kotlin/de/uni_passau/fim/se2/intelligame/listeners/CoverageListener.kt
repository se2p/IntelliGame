/*
 * Copyright 2023 IntelliGame contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_passau.fim.se2.intelligame.listeners

import de.uni_passau.fim.se2.intelligame.achievements.*
import de.uni_passau.fim.se2.intelligame.util.CoverageInfo
import com.intellij.coverage.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import java.lang.reflect.Field


object CoverageListener : CoverageSuiteListener {
    lateinit var project: Project
    override fun coverageGathered(suite: CoverageSuite) {
        project = suite.project
        RunWithCoverageAchievement.triggerAchievement(project)
        super.coverageGathered(suite)
    }

    override fun beforeSuiteChosen() = Unit

    override fun afterSuiteChosen() {
        val dataManager = CoverageDataManagerImpl.getInstance(project)
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return
        }
        val suitesBundle: CoverageSuitesBundle = dataManager.currentSuitesBundle ?: return

        val annotator = suitesBundle.coverageEngine.getCoverageAnnotator(project)

        val modalTask: Task.Modal =
            object : Task.Modal(project, "Modal Cancelable Task", false) {

                override fun run(indicator: ProgressIndicator) {
                    if (annotator::class.simpleName == "JavaCoverageAnnotator") {
                        javaCoverage()
                    } else if (annotator::class.simpleName == "JestCoverageAnnotator") {
                        jestCoverage()
                    }
                }

                fun javaCoverage() {
                    // Check for class coverage information
                    val classCoverageInfosField: Field =
                        annotator.javaClass.getDeclaredField("myClassCoverageInfos")
                    classCoverageInfosField.isAccessible = true
                    val classCoverageInfosValue: Map<Any, Any> =
                        classCoverageInfosField.get(annotator) as Map<Any, Any>
                    for ((key, value) in classCoverageInfosValue) {
                        val coverageInfo = extractCoverageInfos(value)
                        GetXLineCoverageInClassesWithYLinesAchievement.triggerAchievement(
                            coverageInfo,
                            key as String,
                            project
                        )
                        GetXBranchCoverageInClassesWithYBranchesAchievement.triggerAchievement(
                            coverageInfo,
                            key,
                            project
                        )
                        GetXMethodCoverageInClassesWithYMethodsAchievement.triggerAchievement(
                            coverageInfo,
                            key,
                            project
                        )
                        CoverXLinesAchievement.triggerAchievement(coverageInfo, project)
                        CoverXMethodsAchievement.triggerAchievement(coverageInfo, project)
                        CoverXClassesAchievement.triggerAchievement(coverageInfo, project)
                        CoverXBranchesAchievement.triggerAchievement(coverageInfo, project)
                    }
                    val extensionCoverageField: Field =
                        annotator.javaClass.getDeclaredField("myDirCoverageInfos")
                    extensionCoverageField.isAccessible = true
                    val extensionCoverageInfosValue: Map<Any, Any> =
                        extensionCoverageField.get(annotator) as Map<Any, Any>
                    if (extensionCoverageInfosValue.isEmpty()) {
                        ApplicationManager.getApplication().invokeLater(fun() {
                            ProgressManager.getInstance().run(this)
                        })
                    }
                }

                fun jestCoverage() {
                    // Check for file coverage information
                    val fileCoverageInfosField: Field =
                        annotator.javaClass.superclass.getDeclaredField("myFileCoverageInfos")
                    fileCoverageInfosField.isAccessible = true
                    val fileCoverageInfosValue: Map<Any, Any> =
                        fileCoverageInfosField.get(annotator) as Map<Any, Any>
                    for ((key, value) in fileCoverageInfosValue) {
                        val coverageInfo = extractJestCoverageInfos(value)
                        GetXLineCoverageInClassesWithYLinesAchievement.triggerAchievement(
                            coverageInfo,
                            key as String,
                            project
                        )
                        CoverXLinesAchievement.triggerAchievement(coverageInfo, project)
                        CoverXClassesAchievement.triggerAchievement(coverageInfo, project)
                    }
                }
            }

        ApplicationManager.getApplication().invokeLater(fun() {
            ProgressManager.getInstance().run(modalTask)
        })
    }

    private fun extractCoverageInfos(coverageInfo: Any): CoverageInfo {
        val coveredLineCount =
            coverageInfo.javaClass.getMethod("getCoveredLineCount").invoke(coverageInfo) as Int
        val totalLineCount = getFieldAsInt(coverageInfo, "totalLineCount")
        val totalClassCount = getFieldAsInt(coverageInfo, "totalClassCount")
        val coveredClassCount = getFieldAsInt(coverageInfo, "coveredClassCount")
        val totalMethodCount = getFieldAsInt(coverageInfo, "totalMethodCount")
        val coveredMethodCount = getFieldAsInt(coverageInfo, "coveredMethodCount")
        val coveredBranchCount = getFieldAsInt(coverageInfo, "coveredBranchCount")
        val totalBranchCount = getFieldAsInt(coverageInfo, "totalBranchCount")
        return CoverageInfo(
            totalClassCount,
            coveredClassCount,
            totalMethodCount,
            coveredMethodCount,
            totalLineCount,
            coveredLineCount,
            totalBranchCount,
            coveredBranchCount
        )
    }

    private fun extractJestCoverageInfos(coverageInfo: Any): CoverageInfo {
        val coveredLineCount = getFieldAsInt(coverageInfo, "coveredLineCount")
        val totalLineCount = getFieldAsInt(coverageInfo, "totalLineCount")
        return CoverageInfo(
            1,
            1,
            0,
            0,
            totalLineCount,
            coveredLineCount,
            0,
            0
        )
    }

    private fun findUnderlyingField(clazz: Class<*>, fieldName: String): Field? {
        var current = clazz
        do {
            try {
                return current.getDeclaredField(fieldName)
            } catch (_: Exception) {}
        } while (current.superclass.also { current = it } != null)
        return null
    }

    private fun getFieldAsInt(coverageInfo: Any, fieldName: String): Int {
        val field: Field? = findUnderlyingField(coverageInfo.javaClass, fieldName)
        return if (field == null) {
            0
        } else {
            field.get(coverageInfo) as Int
        }
    }
}