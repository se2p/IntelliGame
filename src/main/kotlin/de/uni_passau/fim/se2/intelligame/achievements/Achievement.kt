package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.DataManager
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import de.uni_passau.fim.se2.intelligame.MyBundle
import de.uni_passau.fim.se2.intelligame.components.AchievementToolWindow
import de.uni_passau.fim.se2.intelligame.util.CSVReportGenerator
import de.uni_passau.fim.se2.intelligame.util.Logger
import javax.swing.SwingUtilities

abstract class Achievement {

    enum class Language {
        Java, JavaScript
    }

    companion object {
        fun refreshWindow() {
            if (MyBundle.message("group") != "control") {
                val project = DataManager.getInstance().dataContextFromFocus.resultSync
                    .getData(PlatformDataKeys.PROJECT)
                val toolWindow = ToolWindowManager.getInstance(project!!).getToolWindow("Achievements")!!
                SwingUtilities.invokeLater {
                    toolWindow.contentManager.removeAllContents(true)
                    val content = ContentFactory.getInstance()
                        .createContent(AchievementToolWindow.createPanel(), null, false)
                    toolWindow.contentManager.addContent(content)
                }
            }
        }
    }

    // absolute number of calling the action of the achievement
    abstract fun progress(): Int

    // update progress
    abstract fun updateProgress(progress: Int)

    abstract fun getDescription(): String

    abstract fun getName(): String

    /**
     * The key is the level (0-4) and the value is the required progress to achieve this level
     */
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

    /**
     * Get the next step with the help of the stepLevelMatrix.
     * Next step = which progress you need to get to the next level.
     */
    open fun nextStep(): Int {
        val stepLevelMatrix = getStepLevelMatrix()
        val progress = progress()
        for ((_, value) in stepLevelMatrix) {
            if (progress < value) {
                return value
            }
        }
        return stepLevelMatrix.getValue(stepLevelMatrix.size - 1)
    }

    /**
     * Shows the balloon with the given message.
     */
    fun showAchievementNotification(message: String, project: Project?) {
        if (MyBundle.message("group") != "control") {
            NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                .createNotification(
                    message,
                    NotificationType.INFORMATION
                )
                .addAction(
                    NotificationAction.createSimple("Show more information"
                    ) {
                        val myProject = DataManager.getInstance().dataContextFromFocus.resultSync
                            .getData(PlatformDataKeys.PROJECT)
                        val toolWindow = ToolWindowManager.getInstance(myProject!!).getToolWindow("Achievements")!!
                        refreshWindow()
                        toolWindow.show()
                    }
                )
                .notify(null)
        }

        Logger.logStatus(message, Logger.Kind.Notification, project)
    }

    /**
     * Get the current progress group
     * Groups are Divided:
     * 0: 0 - 24,9%
     * 1: 25% - 49,9%
     * 2: 50% - 74,9%
     * 3: 75% - 100%
     */
    private fun getProgressGroup(): Pair<Int, String> {
        val progressInPercent = (progress().toFloat() / nextStep())
        val reachedPercentage = "%.2f".format((progressInPercent * 100))
        if (progressInPercent >= 0.25) {
            if (progressInPercent >= 0.5) {
                if (progressInPercent >= 0.75) {
                    return Pair(3, reachedPercentage)
                }
                return Pair(2, reachedPercentage)
            }
            return Pair(1, reachedPercentage)
        }
        return Pair(0, reachedPercentage)
    }

    protected fun handleProgress(progress: Int, project: Project?) {
        if (progress >= nextStep()) {
            updateProgress(progress)
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '"
                    + getName() + "' achievement!", project)
        } else {
            val progressGroupBeforeUpdate = getProgressGroup()
            updateProgress(progress)
            val progressGroupAfterUpdate = getProgressGroup()
            if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                showAchievementNotification(
                    "You are making progress on an achievement! You have already reached " +
                            progressGroupAfterUpdate.second + "% of the next level of the '" +
                            getName() + "' achievement!", project
                )
            }
        }
        refreshWindow()
        CSVReportGenerator.generateCSVReport(project)
    }

    abstract fun supportsLanguages(): List<Language>
}