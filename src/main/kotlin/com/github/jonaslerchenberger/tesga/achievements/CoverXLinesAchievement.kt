package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object CoverXLinesAchievement: Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo) {
        var progress = progress()
        progress += coverageInfo.coveredLineCount
        if (progress == nextStep()) {
            showAchievementNotification("Congratulations! You unlocked 'Line-by-line' Achievement")
        }
        updateProgress(progress)
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

    override fun nextStep(): Int {
        if (progress() > 100) {
            if (progress() > 1000) {
                if (progress() > 10000) {
                    return 100000;
                }
                return 10000;
            }
            return 1000;
        }
        return 100;
    }
}