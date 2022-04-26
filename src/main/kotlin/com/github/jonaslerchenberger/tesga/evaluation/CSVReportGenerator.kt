package com.github.jonaslerchenberger.tesga.evaluation

import com.github.jonaslerchenberger.tesga.achievements.Achievement
import com.github.jonaslerchenberger.tesga.services.Util
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

object CSVReportGenerator {

    fun generateCSVReport() {
        val achievementNames = Util.getAchievements().stream().map(Achievement::getName).collect(Collectors.toList())
        val headers = mutableListOf<String>()
        headers += achievementNames
        val path = "/Users/jonas/Desktop/Master/Masterarbeit/Evaluation"
        val printer = path?.let { getNewPrinter("TestReport.csv", it, headers) }
        val row = Util.getAchievements().stream().map(Achievement::getLevel).collect(Collectors.toList())
        printer?.printRecord(row)
        printer?.flush()
    }

    @Throws(IOException::class)
    fun getNewPrinter(name: String, path: String, headers: List<String>): CSVPrinter? {
        val folder: File
        val filePath: Path
        val namePath = Paths.get(name)
        if (namePath.isAbsolute) {
            filePath = namePath
            folder = File(filePath.parent.toString())
        } else {
            filePath = Paths.get(path + System.getProperty("file.separator") + name)
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