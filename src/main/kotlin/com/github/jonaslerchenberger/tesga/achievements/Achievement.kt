package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType

abstract class Achievement {
    // absolute number of calling the action of the achievement
    abstract fun progress(): Int

    // update progress
    abstract fun updateProgress(progress: Int)

    abstract fun getDescription(): String

    abstract fun getName(): String

    abstract fun getStepLevelMatrix(): LinkedHashMap<Int, Int>

    open fun getLevel(): Int {
        val stepLevelMatrix = getStepLevelMatrix()
        val progress = progress()
        for ((key, value) in stepLevelMatrix) {
            if (progress < value ) {
                return key
            }
        }
        return 4
    }

    open fun nextStep(): Int {
        val stepLevelMatrix = getStepLevelMatrix()
        val progress = progress()
        for ((key, value) in stepLevelMatrix) {
            if (progress < value ) {
                return value
            }
        }
        return 0
    }

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
}