package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object CoverXMethodsAchievement: Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo) {
        var progress = progress()
        progress += coverageInfo.coveredMethodCount
        if (progress == nextStep()) {
            showAchievementNotification("Congratulations! You unlocked 'Check your methods' Achievement")
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

    override fun nextStep(): Int {
        if (progress() > 10) {
            if (progress() > 100) {
                if (progress() > 1000) {
                    return 10000;
                }
                return 1000;
            }
            return 100;
        }
        return 10;
    }
}