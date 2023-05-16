package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project


object RefactorCodeAchievement : Achievement() {

    fun triggerAchievement(project: Project?) {
        var progress = progress()
        progress++
        handleProgress(progress, project)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("refactorCodeAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("refactorCodeAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Refactor test code between two consecutive passing test runs."
    }

    override fun getName(): String {
        return "Shine in new splendour"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 5, 1 to 50, 2 to 500, 3 to 2500)
    }

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java)
    }
}