package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object GetXBranchCoverageInClassesWithYBranchesAchievement  : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo, className: String) {
        if ((coverageInfo.coveredBranchCount.toDouble() / coverageInfo.totalBranchCount) >= requiredCoverage()
            && coverageInfo.totalBranchCount >= requiredTotalBranches()
            && !getClassesWhichFulfillRequirements().split(",").contains(className)
        ) {
            var classesWhichFulfillRequirements = getClassesWhichFulfillRequirements()
            classesWhichFulfillRequirements += ",$className"
            updateClassesWhichFulfillRequirements(classesWhichFulfillRequirements)
            if (progress() == nextStep()) {
                showAchievementNotification("Congratulations! You unlocked 'Class Reviewer - Branches' Achievement")
                updateClassesWhichFulfillRequirements("")
                increaseLevel()
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

    override fun updateProgress(progress: Int) {
    }

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
        return "Cover " + nextStep() + " classes which have at least " + requiredTotalBranches() + " branches by at least " + requiredCoverage() * 100 + "%"
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