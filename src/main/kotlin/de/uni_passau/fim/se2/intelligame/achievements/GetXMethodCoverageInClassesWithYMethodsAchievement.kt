package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import de.uni_passau.fim.se2.intelligame.util.CoverageInfo

object GetXMethodCoverageInClassesWithYMethodsAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo, className: String) {
        if (coverageInfo.totalMethodCount >= requiredTotalMethods()
            && !getClassesWhichFulfillRequirements().split(",").contains(className)
        ) {
            val achievedCoverage = coverageInfo.coveredMethodCount.toDouble() / coverageInfo.totalMethodCount
            if (achievedCoverage >= requiredCoverage()) {
                var classesWhichFulfillRequirements = getClassesWhichFulfillRequirements()
                if (classesWhichFulfillRequirements == "") {
                    classesWhichFulfillRequirements = className
                } else {
                    classesWhichFulfillRequirements += ",$className"
                }
                updateClassesWhichFulfillRequirements(classesWhichFulfillRequirements)
                if (progress() == nextStep()) {
                    showAchievementNotification("Congratulations! You unlocked level " + (getLevel() + 1) + " of the  'Class Reviewer - Methods' Achievement")
                    updateClassesWhichFulfillRequirements("")
                    increaseLevel()
                }
            } else if (achievedCoverage >= requiredCoverage() - 0.02) {
                showAchievementNotification(
                    "Hey you are about to fulfill a requirement for an Achievement progress! Only " + "%.2f".format(
                        (requiredCoverage() - achievedCoverage) * 100
                    ) + "% Method-coverage missing in the class " + className + ". Keep going!"
                )
            }
        }
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        val value = properties.getValue("GetXMethodCoverageInClassesWithYMethodsAchievement", "")
        return if (value == "") {
            0
        } else {
            value.split(",").size
        }
    }

    override fun updateProgress(progress: Int) {
    }

    private fun updateClassesWhichFulfillRequirements(classesWhichFulfillRequirements: String) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("GetXMethodCoverageInClassesWithYMethodsAchievement", classesWhichFulfillRequirements, "")
    }

    private fun getClassesWhichFulfillRequirements(): String {
        val properties = PropertiesComponent.getInstance()
        return properties.getValue("GetXMethodCoverageInClassesWithYMethodsAchievement", "")
    }

    override fun getLevel(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("GetXMethodCoverageInClassesWithYMethodsAchievementLevel", 0)
    }

    private fun increaseLevel() {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("GetXMethodCoverageInClassesWithYMethodsAchievementLevel", (getLevel() + 1), 0)
    }

    override fun getDescription(): String {
        return "Cover " + nextStep() + " classes which have at least " + requiredTotalMethods() + " methods by at least " + requiredCoverage() * 100 + "%"
    }

    override fun getName(): String {
        return "Class Reviewer - Methods"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 50, 2 to 250, 3 to 500)
    }

    private fun requiredCoverage(): Double {
        val level = getLevel()
        if (level > 1) {
            if (level > 2) {
                if (level > 3) {
                    return 0.90
                }
                return 0.85
            }
            return 0.80
        }
        return 0.6
    }

    private fun requiredTotalMethods(): Int {
        val level = getLevel()
        if (level > 1) {
            if (level > 2) {
                if (level > 3) {
                    return 25
                }
                return 15
            }
            return 8
        }
        return 3
    }
}