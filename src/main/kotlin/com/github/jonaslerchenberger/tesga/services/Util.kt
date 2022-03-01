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
                            println("New Information")
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
                CoverXBranchesAchievement
            )
        }
    }
}