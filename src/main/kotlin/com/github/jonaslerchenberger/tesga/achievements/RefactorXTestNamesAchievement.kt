package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object RefactorXTestNamesAchievement : Achievement() {
    fun triggerAchievement() {
        var progress = progress()
        progress++
        if (progress == nextStep()) {
            showAchievementNotification("Congratulations! You unlocked 'The Eponym' Achievement")
        }
        updateProgress(progress)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("RefactorXTestNamesAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("RefactorXTestNamesAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Give your tests better names"
    }

    override fun getName(): String {
        return "The Eponym"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }
}