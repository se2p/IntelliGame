package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object CoverXLinesAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo) {
        var progress = progress()
        progress += coverageInfo.coveredLineCount
        if (progress >= nextStep()) {
            updateProgress(progress)
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '" + getName() + "' achievement!")
        } else {
            val progressGroupBeforeUpdate = getProgressGroup()
            updateProgress(progress)
            val progressGroupAfterUpdate = getProgressGroup()
            if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                showAchievementNotification(
                    "You are making progress on an achievement! You have already reached " + progressGroupAfterUpdate.second + "% of the next level of the '" + getName() + "' achievement!"
                )
            }
        }
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("coverXLinesAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("coverXLinesAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Cover X lines with your tests"
    }

    override fun getName(): String {
        return "Line-by-line"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 100, 1 to 1000, 2 to 10000, 3 to 100000)
    }
}