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
            if (progress < value) {
                return key
            }
        }
        return 4
    }

    open fun nextStep(): Int {
        val stepLevelMatrix = getStepLevelMatrix()
        val progress = progress()
        for ((key, value) in stepLevelMatrix) {
            if (progress < value) {
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

    /**
     * Get the current progress group
     * Groups are Divided:
     * 0: 0 - 24,9%
     * 1: 25% - 49,9%
     * 2: 50% - 74,9%
     * 3: 75% - 100%
     */
    protected fun getProgressGroup(): Pair<Int, String> {
        val progressInPercent = (progress().toFloat() / nextStep())
        val missingPercent = "%.2f".format(((1.00 - progressInPercent) * 100))
        if (progressInPercent >= 0.25) {
            if (progressInPercent >= 0.5) {
                if (progressInPercent >= 0.75) {
                    return Pair(3, missingPercent)
                }
                return Pair(2, missingPercent)
            }
            return Pair(1, missingPercent)
        }
        return Pair(0, missingPercent)
    }

    protected fun handleProgress(progress: Int) {
        if (progress == nextStep()) {
            updateProgress(progress)
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '" + getName() + "' achievement!")
        } else {
            val progressGroupBeforeUpdate = getProgressGroup()
            updateProgress(progress)
            val progressGroupAfterUpdate = getProgressGroup()
            if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                showAchievementNotification(
                    "You are making progress on an achievement! Only " + progressGroupAfterUpdate.second + "% are missing for the next level of the '" + getName() + "' achievement!"
                )
            }
        }
    }
}