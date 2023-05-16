package de.uni_passau.fim.se2.intelligame.achievements

import de.uni_passau.fim.se2.intelligame.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project

object CoverXBranchesAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo, project: Project?) {
        var progress = progress()
        progress += coverageInfo.coveredBranchCount
        handleProgress(progress, project)
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
        return "Cover X branches with your tests. Attention: for this achievement the tracing option of " +
                "the IntelliJ Runner must be enabled."
    }

    override fun getName(): String {
        return "Check your branches"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java, Language.JavaScript)
    }
}