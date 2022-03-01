package com.github.jonaslerchenberger.tesga.listeners

import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.coverage.*
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
                    val javaPackageFile: VirtualFile? = VirtualFileManager.getInstance().findFileByNioPath(
                        Path.of(
                            myProject?.basePath ?: "", "/src/main/java"
                        )
                    )
                    if (javaPackageFile != null) {
//                        val projectDir : PsiDirectory = PsiDirectoryFactory.getInstance(CoverageListener.myProject).createDirectory(
//                            javaPackageFile)
//                        val result = annotator.getDirCoverageInformationString(projectDir, suitesBundle, dataManager)
                        val field: Field = annotator.javaClass.getDeclaredField("myDirCoverageInfos")
                        field.isAccessible = true
                        val value: HashMap<Any, Any> = field.get(annotator) as HashMap<Any, Any>
                        val coverageInfo = value[javaPackageFile]
                        var coveredLineCount = 0
                        var totalLineCount = 0
                        var totalClassCount = 0
                        var coveredClassCount = 0
                        var totalMethodCount = 0
                        var coveredMethodCount = 0
                        var coveredBranchCount = 0
                        var totalBranchCount = 0
                        if (coverageInfo != null) {
                            coveredLineCount =
                                coverageInfo.javaClass.getMethod("getCoveredLineCount").invoke(coverageInfo) as Int
                            totalLineCount = getFieldAsInt(coverageInfo, "totalLineCount")
                            totalClassCount = getFieldAsInt(coverageInfo, "totalClassCount")
                            coveredClassCount = getFieldAsInt(coverageInfo, "coveredClassCount")
                            totalMethodCount = getFieldAsInt(coverageInfo, "totalMethodCount")
                            coveredMethodCount = getFieldAsInt(coverageInfo, "coveredMethodCount")
                            coveredBranchCount = getFieldAsInt(coverageInfo, "coveredBranchCount")
                            totalBranchCount = getFieldAsInt(coverageInfo, "totalBranchCount")
                        }
                        val convertedCoverageInfo = CoverageInfo(
                            totalClassCount,
                            coveredClassCount,
                            totalMethodCount,
                            coveredMethodCount,
                            totalLineCount,
                            coveredLineCount,
                            totalBranchCount,
                            coveredBranchCount
                        )
                        CoverXLinesAchievement.triggerAchievement(convertedCoverageInfo)
                        CoverXMethodsAchievement.triggerAchievement(convertedCoverageInfo)
                        CoverXClassesAchievement.triggerAchievement(convertedCoverageInfo)
                        CoverXBranchesAchievement.triggerAchievement(convertedCoverageInfo)
                    }
                }
            })
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

    fun getFieldAsInt(coverageInfo: Any, fieldName: String): Int {
        val field: Field? = findUnderlyingField(coverageInfo.javaClass, fieldName)
        return if (field == null) {
            0
        } else {
            field.get(coverageInfo) as Int
        }
    }
}