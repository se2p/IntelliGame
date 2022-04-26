package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent

object RunWithCoverageAchievement : Achievement() {
    fun triggerAchievement() {
        var progress = progress()
        progress += 1
        handleProgress(progress)
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

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}