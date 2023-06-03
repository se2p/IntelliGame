package de.uni_passau.fim.se2.intelligame.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import de.uni_passau.fim.se2.intelligame.achievements.*

object Util {

    fun getAchievements(): List<Achievement> {
        return listOf(
            AddTestsAchievement,
            CoverXBranchesAchievement,
            CoverXClassesAchievement,
            CoverXLinesAchievement,
            CoverXMethodsAchievement,
            FindXBugsAchievement,
            GetXBranchCoverageInClassesWithYBranchesAchievement,
            GetXLineCoverageInClassesWithYLinesAchievement,
            GetXMethodCoverageInClassesWithYMethodsAchievement,
            RefactorAddXAssertionsAchievement,
            RefactorCodeAchievement,
            RefactorExtractXMethodsAchievement,
            RefactorInlineXMethodsAchievement,
            RefactorXTestNamesAchievement,
            RepairXWrongTestsAchievement,
            RunWithCoverageAchievement,
            RunXDebuggerModeAchievement,
            RunXTestsAchievement,
            RunXTestSuitesAchievement,
            RunXTestSuitesWithXTestsAchievement,
            SetXBreakpointsAchievement,
            SetXConditionalBreakpointsAchievement,
            SetXFieldWatchpointsAchievement,
            SetXLineBreakpointsAchievement,
            SetXMethodBreakpointsAchievement,
            TriggerXAssertsByTestsAchievement
        )
    }

    fun getRefactoringAchievements(): List<Achievement> {
        return listOf(
            RefactorCodeAchievement,
            RefactorXTestNamesAchievement,
            RefactorExtractXMethodsAchievement,
            RefactorInlineXMethodsAchievement,
            RefactorAddXAssertionsAchievement
        )
    }

    fun getCoverageAchievements(): List<Achievement> {
        return listOf(
            RunWithCoverageAchievement,
            CoverXLinesAchievement,
            CoverXMethodsAchievement,
            CoverXClassesAchievement,
            CoverXBranchesAchievement
        )
    }

    fun getAdvancedCoverageAchievements(): List<Achievement> {
        return listOf(
            GetXLineCoverageInClassesWithYLinesAchievement,
            GetXMethodCoverageInClassesWithYMethodsAchievement,
            GetXBranchCoverageInClassesWithYBranchesAchievement
        )
    }

    fun getDebuggingAchievements(): List<Achievement> {
        return listOf(
            RunXDebuggerModeAchievement,
            SetXBreakpointsAchievement,
            SetXConditionalBreakpointsAchievement,
            SetXFieldWatchpointsAchievement,
            SetXLineBreakpointsAchievement,
            SetXMethodBreakpointsAchievement
        )
    }

    fun getTestsAchievement(): List<Achievement> {
        return listOf(
            RunXTestsAchievement,
            RunXTestSuitesAchievement,
            RunXTestSuitesWithXTestsAchievement,
            TriggerXAssertsByTestsAchievement,
            FindXBugsAchievement,
            RepairXWrongTestsAchievement,
            AddTestsAchievement
        )
    }

    fun getProject(locationUrl: String?): Project? {
        val projects = ProjectManager.getInstance().openProjects
        var project: Project? = null
        if (projects.size == 1) {
            project = projects[0]
            return project
        }
        for (p in projects) {
            if (p.basePath?.let { locationUrl?.contains(it) } == true) {
                project = p
            }
        }
        return project
    }
}