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
import com.intellij.openapi.roots.ProjectRootManager
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.sql.Timestamp
import java.util.stream.Collectors

object CSVReportGenerator {

    fun generateCSVReport(project: Project?) {
        if (project == null) return
        val achievementNamesLevel = Util.getAchievements().stream().map { it.getName() + "Level" }.collect(
            Collectors.toList())
        val achievementNamesProgress = Util.getAchievements().stream().map { it.getName() + "Progress" }.collect(
            Collectors.toList())
        val headers = mutableListOf<String>()
        headers += listOf("Timestamp")
        headers += achievementNamesLevel
        headers += achievementNamesProgress
        try {
            val path = ProjectRootManager.getInstance(project).contentRoots[0].path + "${File.separator}.evaluation"
            val printer = getNewPrinter("TestReport.csv", path, headers)
            val timestamp = Timestamp(System.currentTimeMillis()).toString()
            val row = mutableListOf<String>()
            row += listOf(timestamp)
            row += Util.getAchievements().stream().map { it.getLevel().toString() }.collect(Collectors.toList())
            row += Util.getAchievements().stream().map { it.progress().toString() }.collect(Collectors.toList())
            printer.printRecord(row)
            printer.flush()
        } catch (_: Exception) {}
    }

    @Throws(IOException::class)
    fun getNewPrinter(name: String, path: String, headers: List<String>): CSVPrinter {
        val folder: File
        val filePath: Path
        val namePath = Paths.get(name)
        if (namePath.isAbsolute) {
            filePath = namePath
            folder = File(filePath.parent.toString())
        } else {
            filePath = Paths.get(path + File.separator + name)
            folder = File(path)
        }
        if (!folder.exists()) {
            Files.createDirectory(filePath.parent)
        }
        return if (filePath.toFile().length() > 0) {
            val writer = Files.newBufferedWriter(
                filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND
            )
            CSVPrinter(writer, CSVFormat.DEFAULT.withSkipHeaderRecord())
        } else {
            val writer = Files.newBufferedWriter(
                filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND
            )
            CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(*headers.toTypedArray()))
        }
    }

}