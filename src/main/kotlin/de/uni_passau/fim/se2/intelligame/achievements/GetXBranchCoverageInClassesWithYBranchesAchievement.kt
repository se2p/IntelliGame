package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import de.uni_passau.fim.se2.intelligame.util.CoverageInfo

object GetXBranchCoverageInClassesWithYBranchesAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo, className: String) {
        if (coverageInfo.totalBranchCount >= requiredTotalBranches()
            && !getClassesWhichFulfillRequirements().split(",").contains(className)
        ) {
            val achievedCoverage = coverageInfo.coveredBranchCount.toDouble() / coverageInfo.totalBranchCount
            if (achievedCoverage >= requiredCoverage()) {
                var classesWhichFulfillRequirements = getClassesWhichFulfillRequirements()
                if (classesWhichFulfillRequirements == "") {
                    classesWhichFulfillRequirements = className
                } else {
                    classesWhichFulfillRequirements += ",$className"
                }
                updateClassesWhichFulfillRequirements(classesWhichFulfillRequirements)
                if (progress() == nextStep()) {
                    showAchievementNotification("Congratulations! You unlocked level " +
                            (getLevel() + 1) + " of the 'Class Reviewer - Branches' Achievement")
                    updateClassesWhichFulfillRequirements("")
                    increaseLevel()
                }
            } else if (achievedCoverage >= requiredCoverage() - 0.02) {
                showAchievementNotification(
                    "Hey you are about to fulfill a requirement for an Achievement progress! Only " + "%.2f".format(
                        (requiredCoverage() - achievedCoverage) * 100
                    ) + "% Branch-coverage missing in the class " + className + ". Keep going!"
                )
            }
        }
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        val value = properties.getValue("GetXBranchCoverageInClassesWithYBranchesAchievement", "")
        return if (value == "") {
            0
        } else {
            value.split(",").size
        }
    }

    override fun updateProgress(progress: Int) = Unit

    private fun updateClassesWhichFulfillRequirements(classesWhichFulfillRequirements: String) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("GetXBranchCoverageInClassesWithYBranchesAchievement", classesWhichFulfillRequirements, "")
    }

    private fun getClassesWhichFulfillRequirements(): String {
        val properties = PropertiesComponent.getInstance()
        return properties.getValue("GetXBranchCoverageInClassesWithYBranchesAchievement", "")
    }

    override fun getLevel(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("GetXBranchCoverageInClassesWithYBranchesAchievementLevel", 0)
    }

    private fun increaseLevel() {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("GetXBranchCoverageInClassesWithYBranchesAchievementLevel", (getLevel() + 1), 0)
    }

    override fun getDescription(): String {
        return "Cover " + nextStep() + " classes which have at least "+
                requiredTotalBranches() + " branches by at least " +
                requiredCoverage() * 100 + "%. Attention: for this achievement the tracing option of " +
                "the IntelliJ Runner must be enabled."
    }

    override fun getName(): String {
        return "Class Reviewer - Branches"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 5, 1 to 20, 2 to 75, 3 to 250)
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
        return 0.75
    }

    private fun requiredTotalBranches(): Int {
        val level = getLevel()
        if (level > 1) {
            if (level > 2) {
                if (level > 3) {
                    return 500
                }
                return 250
            }
            return 50
        }
        return 15
    }
}