package com.github.jonaslerchenberger.tesga.listeners

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
import com.intellij.psi.PsiDirectory
import com.intellij.psi.impl.file.PsiDirectoryFactory
import jetbrains.coverage.report.idea.IDEACoverageData
import java.lang.reflect.Field
import java.nio.file.Path


object CoverageListener : CoverageSuiteListener {
    lateinit var myProject: Project
    override fun coverageGathered(suite: CoverageSuite) {
        myProject = suite.project
        super.coverageGathered(suite)
    }

    override fun beforeSuiteChosen() {
    }

    override fun afterSuiteChosen() {
        var dataManager = CoverageDataManagerImpl.getInstance(myProject)
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return
        }
        val suitesBundle: CoverageSuitesBundle = dataManager.currentSuitesBundle ?: return
        val projectData = suitesBundle.coverageData

//        val ideacoverageData = IDEACoverageData(projectData, dataManager)
        /*val statisticsCalculator = StatisticsCalculatorImpl()
        statisticsCalculator.compute(ideaCoverageData)
        val overallStats = statisticsCalculator.overallStats*/
        val viewManager = CoverageViewManager.getInstance(myProject)

         val coverageViewExtension = suitesBundle.coverageEngine.createCoverageViewExtension(
                 myProject,
                 suitesBundle,
                 viewManager.state
             )

        val structure = CoverageViewTreeStructure(myProject, suitesBundle, viewManager.state)
//        var annotator = suitesBundle.coverageEngine.getCoverageAnnotator(myProject) as BaseCoverageAnnotator as JavaCoverageAnnotator
        var annotator = suitesBundle.coverageEngine.getCoverageAnnotator(myProject)
//        var packageDir = JavaPsiFacade.getInstance(myProject).findPackage("")
        var projectDir : PsiDirectory = PsiDirectoryFactory.getInstance(myProject).createDirectory(myProject.baseDir)
        var packageDir: PsiDirectory = projectDir.subdirectories.find { it.name == "src" } ?: projectDir
        var result = annotator.getDirCoverageInformationString(packageDir, suitesBundle, dataManager)


        ProgressManager.getInstance()
            .run(object : Backgroundable(myProject, CoverageBundle.message("coverage.report.building")) {
                override fun run(indicator: ProgressIndicator) {
                    println("run")
                }

                override fun onSuccess() {
                    var javaPackageFile : VirtualFile? = VirtualFileManager.getInstance().findFileByNioPath(Path.of(
                        myProject?.basePath ?: "", "/src/main/java"))
                    if (javaPackageFile != null) {
                        var projectDir : PsiDirectory = PsiDirectoryFactory.getInstance(CoverageListener.myProject).createDirectory(
                            javaPackageFile)
                        var result = annotator.getDirCoverageInformationString(projectDir, suitesBundle, dataManager)
                        val field: Field = annotator.javaClass.getDeclaredField("myDirCoverageInfos")
                        field.setAccessible(true)
                        val value: HashMap<Any, Any> = field.get(annotator) as HashMap<Any, Any>
                        val coverageInfo = value[javaPackageFile]
                        var coveredLinesCount = 0
                        var totalLineCount = 0
                        var totalClassCount = 0
                        var coveredClassCount = 0
                        var totalMethodCount = 0
                        var coveredMethodCount = 0
                        if (coverageInfo != null) {
                            coveredLinesCount = coverageInfo.javaClass.getMethod("getCoveredLineCount").invoke(coverageInfo) as Int
                            totalLineCount = getFieldAsInt(coverageInfo, "totalLineCount")
                            totalClassCount = getFieldAsInt(coverageInfo, "totalClassCount")
                            coveredClassCount = getFieldAsInt(coverageInfo, "coveredClassCount")
                            totalMethodCount = getFieldAsInt(coverageInfo, "totalMethodCount")
                            coveredMethodCount = getFieldAsInt(coverageInfo, "coveredMethodCount")
                        }
                        println("result: " + result)
                    }
                }
            })

//        var result = suitesBundle.coverageEngine.getCoverageAnnotator(myProject).getPackageCoverageInformationString(
//            structure.rootElement.getValue() as PsiDirectory, null
//            , dataManager
//        )
//        val coverageView = CoverageView(myProject, dataManager, viewManager.state)
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