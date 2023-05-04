package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent

object RefactorExtractXMethodsAchievement : Achievement() {
    fun triggerAchievement() {
        var progress = progress()
        progress++
        handleProgress(progress)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("RefactorExtractXMethodsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("RefactorExtractXMethodsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Extract methods"
    }

    override fun getName(): String {
        return "The Method Extractor"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }
}