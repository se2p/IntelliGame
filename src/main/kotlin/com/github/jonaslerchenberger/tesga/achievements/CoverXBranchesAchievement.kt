package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object CoverXBranchesAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo) {
        var progress = progress()
        progress += coverageInfo.coveredBranchCount
        if (progress >= nextStep()) {
            updateProgress(progress)
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the  '" + getName() + "' achievement!")
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

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("CoverXBranchesAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("CoverXBranchesAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Cover X branches with your tests"
    }

    override fun getName(): String {
        return "Check your branches"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }
}