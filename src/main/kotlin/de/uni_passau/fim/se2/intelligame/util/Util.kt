package de.uni_passau.fim.se2.intelligame.util

import de.uni_passau.fim.se2.intelligame.achievements.*

class Util {
    companion object {

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

    }
}