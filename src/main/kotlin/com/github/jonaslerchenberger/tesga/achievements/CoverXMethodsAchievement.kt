package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object CoverXMethodsAchievement: Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo) {
        var progress = progress()
        progress += coverageInfo.coveredMethodCount
        if (progress == nextStep()) {
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the 'Check your methods' Achievement")
        }
        updateProgress(progress)
    }
    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("coverXMethodsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("coverXMethodsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Cover X methods with your tests"
    }

    override fun getName(): String {
        return "Check your methods"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }
}