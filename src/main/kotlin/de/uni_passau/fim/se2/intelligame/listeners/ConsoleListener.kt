package de.uni_passau.fim.se2.intelligame.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Key
import de.uni_passau.fim.se2.intelligame.achievements.*
import de.uni_passau.fim.se2.intelligame.util.CoverageInfo
import java.io.File

object ConsoleListener : ExecutionListener {

    var coverageRun = false
    var project: Project? = null

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        handler.addProcessListener(ConsoleAdapter())
        project = env.project
        coverageRun = false
    }

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        if (coverageRun) {
            val path = ProjectRootManager.getInstance(env.project).contentRoots[0].path
            val file = File("$path/coverage/coverage-summary.json")
            if (!file.exists()) return
            val coverageData = file.readText().split("\n")
            for(line in coverageData) {
                val regex = Regex(",\"$path/(.*)\": \\{" +
                        "\"lines\":\\{\"total\":(\\d+),\"covered\":(\\d+),\"skipped\":\\d+,\"pct\":[\\d.]+}," +
                        "\"functions\":\\{\"total\":(\\d+),\"covered\":(\\d+),\"skipped\":\\d+,\"pct\":[\\d.]+}," +
                        "\"statements\":\\{\"total\":(\\d+),\"covered\":(\\d+),\"skipped\":\\d+,\"pct\":[\\d.]+}," +
                        "\"branches\":\\{\"total\":(\\d+),\"covered\":(\\d+),\"skipped\":\\d+,\"pct\":[\\d.]+}}")
                if (!regex.containsMatchIn(line)) continue
                val groupValues = regex.findAll(line).first().groupValues
                val fileName = groupValues[1]
                val coverageInfo = CoverageInfo(
                    1,
                    1,
                    groupValues[4].toInt(),
                    groupValues[5].toInt(),
                    groupValues[2].toInt(),
                    groupValues[3].toInt(),
                    groupValues[8].toInt(),
                    groupValues[9].toInt(),
                )

                GetXLineCoverageInClassesWithYLinesAchievement.triggerAchievement(coverageInfo, fileName, project)
                GetXBranchCoverageInClassesWithYBranchesAchievement.triggerAchievement(coverageInfo, fileName, project)
                GetXMethodCoverageInClassesWithYMethodsAchievement.triggerAchievement(coverageInfo, fileName, project)
                CoverXClassesAchievement.triggerAchievement(coverageInfo, project)
            }
            RunXTestSuitesAchievement.triggerAchievement()
            coverageRun = false
        }
        super.processTerminated(executorId, env, handler, exitCode)
    }

    class ConsoleAdapter : ProcessAdapter() {

        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
            if (outputType != ProcessOutputTypes.STDOUT) return
            triggerTestsAchievements(event.text)
            val coverageInfo = extractCoverageInfo(event.text)
            if (coverageInfo.isEmpty()) return
            coverageRun = true
            RunWithCoverageAchievement.triggerAchievement(project)
            CoverXLinesAchievement.triggerAchievement(coverageInfo, project)
            CoverXMethodsAchievement.triggerAchievement(coverageInfo, project)
            CoverXBranchesAchievement.triggerAchievement(coverageInfo, project)
        }

        private fun triggerTestsAchievements(output: String) {
            val regex = Regex("Tests:[^,]*,\\s(\\d+)\\stotal")
            val tests = regex.find(output)?.groupValues?.get(1)?.toInt()
            if (tests != null) {
                RunXTestsAchievement.triggerAchievement(tests)
                RunXTestSuitesWithXTestsAchievement.triggerAchievement(tests)
            }
        }

        private fun extractCoverageInfo(output: String): CoverageInfo {
            val branchesRegex = Regex("Branches\\s*:\\s\\d+.\\d+%\\s\\(\\s(\\d+)/(\\d+)\\s\\)")
            val linesRegex = Regex("Lines\\s*:\\s\\d+.\\d+%\\s\\(\\s(\\d+)/(\\d+)\\s\\)")
            val functionsRegex = Regex("Functions\\s*:\\s\\d+.\\d+%\\s\\(\\s(\\d+)/(\\d+)\\s\\)")
            val coveredLinesCount = linesRegex.find(output)?.groupValues?.get(1)?.toInt()
            val totalLinesCount = linesRegex.find(output)?.groupValues?.get(2)?.toInt()
            val coveredMethodsCount = functionsRegex.find(output)?.groupValues?.get(1)?.toInt()
            val totalMethodsCount = functionsRegex.find(output)?.groupValues?.get(2)?.toInt()
            val coveredBranchesCount = branchesRegex.find(output)?.groupValues?.get(1)?.toInt()
            val totalBranchesCount = branchesRegex.find(output)?.groupValues?.get(2)?.toInt()
            return CoverageInfo(
                0,
                0,
                totalMethodsCount ?: 0,
                coveredMethodsCount ?: 0,
                totalLinesCount ?: 0,
                coveredLinesCount ?: 0,
                totalBranchesCount ?: 0,
                coveredBranchesCount ?: 0
            )
        }
    }
}