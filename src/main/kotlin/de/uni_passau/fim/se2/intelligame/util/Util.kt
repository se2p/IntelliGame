/*
 * Copyright 2023 IntelliGame contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_passau.fim.se2.intelligame.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import de.uni_passau.fim.se2.intelligame.achievements.*

object Util {

    fun getAchievements(): List<Achievement> {
        return listOf(
            AddTestsAchievement,
            CoverXBranchesAchievement,
            CoverXClassesAchievement,
            CoverXLinesAchievement,
            CoverXMethodsAchievement,
            FindXBugsAchievement,
            GetXBranchCoverageInClassesWithYBranchesAchievement,
            GetXLineCoverageInClassesWithYLinesAchievement,
            GetXMethodCoverageInClassesWithYMethodsAchievement,
            RefactorAddXAssertionsAchievement,
            RefactorCodeAchievement,
            RefactorExtractXMethodsAchievement,
            RefactorInlineXMethodsAchievement,
            RefactorXTestNamesAchievement,
            RepairXWrongTestsAchievement,
            RunWithCoverageAchievement,
            RunXDebuggerModeAchievement,
            RunXTestsAchievement,
            RunXTestSuitesAchievement,
            RunXTestSuitesWithXTestsAchievement,
            SetXBreakpointsAchievement,
            SetXConditionalBreakpointsAchievement,
            SetXFieldWatchpointsAchievement,
            SetXLineBreakpointsAchievement,
            SetXMethodBreakpointsAchievement,
            TriggerXAssertsByTestsAchievement
        )
    }

    fun getRefactoringAchievements(): List<Achievement> {
        return listOf(
            RefactorCodeAchievement,
            RefactorXTestNamesAchievement,
            RefactorExtractXMethodsAchievement,
            RefactorInlineXMethodsAchievement,
            RefactorAddXAssertionsAchievement
        )
    }

    fun getCoverageAchievements(): List<Achievement> {
        return listOf(
            RunWithCoverageAchievement,
            CoverXLinesAchievement,
            CoverXMethodsAchievement,
            CoverXClassesAchievement,
            CoverXBranchesAchievement
        )
    }

    fun getAdvancedCoverageAchievements(): List<Achievement> {
        return listOf(
            GetXLineCoverageInClassesWithYLinesAchievement,
            GetXMethodCoverageInClassesWithYMethodsAchievement,
            GetXBranchCoverageInClassesWithYBranchesAchievement
        )
    }

    fun getDebuggingAchievements(): List<Achievement> {
        return listOf(
            RunXDebuggerModeAchievement,
            SetXBreakpointsAchievement,
            SetXConditionalBreakpointsAchievement,
            SetXFieldWatchpointsAchievement,
            SetXLineBreakpointsAchievement,
            SetXMethodBreakpointsAchievement
        )
    }

    fun getTestsAchievement(): List<Achievement> {
        return listOf(
            RunXTestsAchievement,
            RunXTestSuitesAchievement,
            RunXTestSuitesWithXTestsAchievement,
            TriggerXAssertsByTestsAchievement,
            FindXBugsAchievement,
            RepairXWrongTestsAchievement,
            AddTestsAchievement
        )
    }

    fun getProject(locationUrl: String?): Project? {
        val projects = ProjectManager.getInstance().openProjects
        var project: Project? = null
        if (projects.size == 1) {
            project = projects[0]
            return project
        }
        for (p in projects) {
            if (p.basePath?.let { locationUrl?.contains(it) } == true) {
                project = p
            }
        }
        return project
    }
}