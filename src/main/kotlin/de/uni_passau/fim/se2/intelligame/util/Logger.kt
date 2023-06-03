package de.uni_passau.fim.se2.intelligame.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.sql.Timestamp

object Logger {

    enum class Kind {
        Achievement, Notification, Main, Debug
    }

    fun logStatus(text: String, kind: Kind, project: Project?) {

        if (project != null) {
            try {
                val path = ProjectRootManager.getInstance(project).contentRoots[0].path+
                        "${File.separator}.evaluation${File.separator}evaluationLogs.txt"
                val output: Writer
                output = BufferedWriter(FileWriter(File(path), true))

                val timestamp = Timestamp(System.currentTimeMillis()).toString()
                output.appendLine("$timestamp - ${kind.name} - $text")
                output.close()
            } catch (_: Exception) {}
        }
    }
}