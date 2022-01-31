package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType

abstract class Achievement() {
    // absolute number of calling the action of the achievement
    abstract fun progress(): Int

    // update progress
    abstract fun updateProgress(progress: Int)

    abstract fun getDescription(): String

    abstract fun getName(): String

    abstract fun nextStep(): Int

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
}