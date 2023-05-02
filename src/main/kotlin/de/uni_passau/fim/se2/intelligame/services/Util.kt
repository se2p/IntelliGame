package de.uni_passau.fim.se2.intelligame.services

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import de.uni_passau.fim.se2.intelligame.achievements.*
import de.uni_passau.fim.se2.intelligame.components.MoreInformationDialog

class Util {
    companion object {

        fun showAchievementNotification(message: String) {
            NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                .createNotification(
                    message,
                    NotificationType.INFORMATION
                )
                .addAction(
                    NotificationAction.createSimple("Show more information",
                        Runnable {
                            val dialog = MoreInformationDialog(null)
                            dialog.show()

                        })
                )
                .notify(null)
        }

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
                TriggerXAssertsByTestsAchievement,
                UseXPrintfDebuggingAchievement
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
                SetXMethodBreakpointsAchievement,
                UseXPrintfDebuggingAchievement
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