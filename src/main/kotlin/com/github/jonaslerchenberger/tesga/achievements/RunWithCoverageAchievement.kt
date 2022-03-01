package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent

object RunWithCoverageAchievement: Achievement() {
    fun triggerAchievement() {
        var progress = progress()
        progress += 1
        if (progress == nextStep()) {
            showAchievementNotification("Congratulations! You unlocked 'Gotta Catch ’Em All' Achievement")
        }
        updateProgress(progress)
    }
    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("runWithCoverageAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("runWithCoverageAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Run your tests with coverage"
    }

    override fun getName(): String {
        return "Gotta Catch ’Em All"
    }

    override fun nextStep(): Int {
        if (progress() > 3) {
            if (progress() > 10) {
                if (progress() > 100) {
                    return 1000;
                }
                return 100;
            }
            return 10;
        }
        return 3;
    }
}