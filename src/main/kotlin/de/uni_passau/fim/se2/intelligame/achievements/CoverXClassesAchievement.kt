package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import de.uni_passau.fim.se2.intelligame.util.CoverageInfo

object CoverXClassesAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo) {
        var progress = progress()
        progress += coverageInfo.coveredClassCount
        if (progress >= nextStep()) {
            updateProgress(progress)
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the '" + getName() + "' achievement!")
        } else {
            val progressGroupBeforeUpdate = getProgressGroup()
            updateProgress(progress)
            val progressGroupAfterUpdate = getProgressGroup()
            if (progressGroupAfterUpdate.first > progressGroupBeforeUpdate.first) {
                showAchievementNotification(
                    "You are making progress on an achievement! You have already reached " + progressGroupAfterUpdate.second + "% of the next level of the '" + getName() + "' achievement!"
                )
            }
        }
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("CoverXClassesAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("CoverXClassesAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Cover X classes with your tests"
    }

    override fun getName(): String {
        return "Check your classes"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }
}