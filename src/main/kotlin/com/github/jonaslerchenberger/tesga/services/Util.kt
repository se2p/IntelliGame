package com.github.jonaslerchenberger.tesga.services

import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.github.jonaslerchenberger.tesga.listeners.ActionAchievement
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

        fun getActionAchievements(): List<ActionAchievement> {
            return listOf(
                AssertTriggeredByTestAchievement, RunXDebuggerModeAchievement, RunXTestsAchievement, SetXBreakpointsAchievement,
                SetXConditionalBreakpointsAchievement, SetXFieldWatchpointsAchievement, SetXLineBreakpointsAchievement,
                SetXMethodBreakpointsAchievement
            )
        }
    }
}