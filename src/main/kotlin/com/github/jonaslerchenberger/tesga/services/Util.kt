package com.github.jonaslerchenberger.tesga.services

import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType

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
                TriggerXAssertsByTestsAchievement,
                RunXDebuggerModeAchievement,
                RunXTestsAchievement,
                SetXBreakpointsAchievement,
                SetXConditionalBreakpointsAchievement,
                SetXFieldWatchpointsAchievement,
                SetXLineBreakpointsAchievement,
                SetXMethodBreakpointsAchievement,
                UseXPrintfDebuggingAchievement,
                FindXBugsAchievement,
                RepairXWrongTestsAchievement,
                RefactorCodeAchievement,
                RunWithCoverageAchievement,
                CoverXLinesAchievement,
                CoverXMethodsAchievement,
                CoverXClassesAchievement,
                CoverXBranchesAchievement,
                AddTestsAchievement,
                RefactorXTestNamesAchievement,
                RefactorExtractXMethodsAchievement,
                RefactorInlineXMethodsAchievement,
                GetXLineCoverageInClassesWithYLinesAchievement,
                GetXMethodCoverageInClassesWithYMethodsAchievement,
                GetXBranchCoverageInClassesWithYBranchesAchievement,
                RunXTestSuitesAchievement,
                RunXTestSuitesWithXTestsAchievement,
                RefactorAddXAssertionsAchievement
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