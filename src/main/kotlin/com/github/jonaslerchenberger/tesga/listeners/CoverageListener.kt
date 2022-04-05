package com.github.jonaslerchenberger.tesga.listeners

import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.coverage.*
import com.intellij.coverage.view.CoverageViewManager
import com.intellij.coverage.view.CoverageViewTreeStructure
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.lang.reflect.Field
import java.nio.file.Path


object CoverageListener : CoverageSuiteListener {
    lateinit var myProject: Project
    override fun coverageGathered(suite: CoverageSuite) {
        myProject = suite.project
        RunWithCoverageAchievement.triggerAchievement()
        super.coverageGathered(suite)
    }

    override fun beforeSuiteChosen() {
    }

    override fun afterSuiteChosen() {
        val dataManager = CoverageDataManagerImpl.getInstance(myProject)
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return
        }
        val suitesBundle: CoverageSuitesBundle = dataManager.currentSuitesBundle ?: return

        val annotator = suitesBundle.coverageEngine.getCoverageAnnotator(myProject)


        ProgressManager.getInstance()
            .run(object : Backgroundable(myProject, CoverageBundle.message("coverage.report.building")) {
                override fun run(indicator: ProgressIndicator) {
                }

                override fun onSuccess() {
                    val viewManager = CoverageViewManager.getInstance(myProject!!)
                    val structure = CoverageViewTreeStructure(myProject, suitesBundle, viewManager.state)
                    val javaPackageFile: VirtualFile? = VirtualFileManager.getInstance().findFileByNioPath(
                        Path.of(
                            myProject.basePath ?: "", "/src/main/java"
                        )
                    )
                    if (javaPackageFile != null) {
//                        val projectDir : PsiDirectory = PsiDirectoryFactory.getInstance(CoverageListener.myProject).createDirectory(
//                            javaPackageFile)
//                        val result = annotator.getDirCoverageInformationString(projectDir, suitesBundle, dataManager)
                        // Check for dir coverage information
                        val dirCoverageInfosField: Field = annotator.javaClass.getDeclaredField("myDirCoverageInfos")
                        dirCoverageInfosField.isAccessible = true
                        val dirCoverageInfosValue: HashMap<Any, Any> = dirCoverageInfosField.get(annotator) as HashMap<Any, Any>
                        val dirCoverageInfo = dirCoverageInfosValue[javaPackageFile]
                        if (dirCoverageInfo != null) {
                            val coverageInfo = extractCoverageInfos(dirCoverageInfo)
                            CoverXLinesAchievement.triggerAchievement(coverageInfo)
                            CoverXMethodsAchievement.triggerAchievement(coverageInfo)
                            CoverXClassesAchievement.triggerAchievement(coverageInfo)
                            CoverXBranchesAchievement.triggerAchievement(coverageInfo)
                        }
                        // Check for class coverage information
                        val classCoverageInfosField: Field = annotator.javaClass.getDeclaredField("myClassCoverageInfos")
                        classCoverageInfosField.isAccessible = true
                        val classCoverageInfosValue: Map<Any, Any> = classCoverageInfosField.get(annotator) as Map<Any, Any>
                        for ((key, value) in classCoverageInfosValue) {
                            val coverageInfo = extractCoverageInfos(value)
                            GetXLineCoverageInClassesWithYLinesAchievement.triggerAchievement(coverageInfo,
                                key as String
                            )
                            GetXBranchCoverageInClassesWithYBranchesAchievement.triggerAchievement(coverageInfo, key)
                            GetXMethodCoverageInClassesWithYMethodsAchievement.triggerAchievement(coverageInfo, key)
                        }
                    }
                }
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

    private fun findUnderlyingField(clazz: Class<*>, fieldName: String): Field? {
        var current = clazz
        do {
            try {
                return current.getDeclaredField(fieldName)
            } catch (_: Exception) {
            }
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