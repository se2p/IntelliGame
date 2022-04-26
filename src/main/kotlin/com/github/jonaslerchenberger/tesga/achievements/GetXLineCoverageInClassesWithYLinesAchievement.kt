package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.util.CoverageInfo
import com.intellij.ide.util.PropertiesComponent

object GetXLineCoverageInClassesWithYLinesAchievement : Achievement() {
    fun triggerAchievement(coverageInfo: CoverageInfo, className: String) {
        if (coverageInfo.totalLineCount >= requiredTotalLines()
            && !getClassesWhichFulfillRequirements().split(",").contains(className)
        ) {
            val achievedCoverage = coverageInfo.coveredLineCount.toDouble() / coverageInfo.totalLineCount
            if (achievedCoverage >= requiredCoverage()) {
                var classesWhichFulfillRequirements = getClassesWhichFulfillRequirements()
                classesWhichFulfillRequirements += ",$className"
                updateClassesWhichFulfillRequirements(classesWhichFulfillRequirements)
                if (progress() == nextStep()) {
                    showAchievementNotification("Congratulations! You unlocked level " + (getLevel() + 1) + " of the 'Class Reviewer - Lines' Achievement")
                    updateClassesWhichFulfillRequirements("")
                    increaseLevel()
                }
            } else if (achievedCoverage >= requiredCoverage() - 0.02) {
                showAchievementNotification(
                    "Hey you are about to fulfill a requirement for an Achievement progress! Only " + "%.2f".format(
                        (requiredCoverage() - achievedCoverage) * 100
                    ) + "% Line-coverage missing in the class " + className + ". Keep going!"
                )
            }
        }
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        val value = properties.getValue("GetXLineCoverageInClassesWithYLinesAchievement", "")
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
        properties.setValue("GetXLineCoverageInClassesWithYLinesAchievement", classesWhichFulfillRequirements, "")
    }

    fun getClassesWhichFulfillRequirements(): String {
        val properties = PropertiesComponent.getInstance()
        return properties.getValue("GetXLineCoverageInClassesWithYLinesAchievement", "")
    }

    override fun getLevel(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("GetXLineCoverageInClassesWithYLinesAchievementLevel", 0)
    }

    private fun increaseLevel() {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("GetXLineCoverageInClassesWithYLinesAchievementLevel", (getLevel() + 1), 0)
    }

    override fun getDescription(): String {
        return "Cover " + nextStep() + " classes which have at least " + requiredTotalLines() + " lines by at least " + requiredCoverage() * 100 + "%"
    }

    override fun getName(): String {
        return "Class Reviewer - Lines"
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
        return 0.7
    }

    private fun requiredTotalLines(): Int {
        val level = getLevel()
        if (level > 1) {
            if (level > 2) {
                if (level > 3) {
                    return 500
                }
                return 250
            }
            return 25
        }
        return 5
    }
}