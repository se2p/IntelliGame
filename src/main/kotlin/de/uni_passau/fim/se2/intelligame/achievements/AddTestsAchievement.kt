package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File

object AddTestsAchievement : Achievement(), BulkFileListener {

    private var filesUnderObservation = hashMapOf<String, Int>()
    private val regex = "/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex()

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("AddTestsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("AddTestsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Create X tests"
    }

    override fun getName(): String {
        return "Safety First"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java, Language.JavaScript)
    }

    override fun before(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = File(event.path)
            if (file.exists()) {
                var counter = 0
                if (event.path.endsWith("Test.java")) {
                    counter =
                        countTests(
                            file.readText().replace(regex, "")
                        )
                } else if (event.path.endsWith("test.js")) {
                    counter =
                        countJestTests(
                            file.readText().replace(regex, "")
                        )
                }
                filesUnderObservation[event.path] = counter
            }
            super.before(events)
        }
    }

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = File(event.path)
            if (file.exists()) {
                var counter = 0
                if (event.path.endsWith("Test.java")) {
                    counter =
                        countTests(
                            file.readText().replace(regex, "")
                        )
                } else if (event.path.endsWith("test.js")) {
                    counter =
                        countJestTests(
                            file.readText().replace(regex, "")
                        )
                }

                if (counter != 0) {
                    if (filesUnderObservation.containsKey(event.path)
                        && filesUnderObservation[event.path]!! < counter) {
                        var progress = progress()
                        progress += (counter - filesUnderObservation[event.path]!!)
                        val project = event.file?.let { ProjectLocator.getInstance().guessProjectForFile(it) }
                        handleProgress(progress, project)
                    }
                    filesUnderObservation[event.path] = counter
                }
            }
        }
        super.after(events)
    }

    private fun countTests(string: String): Int {
        return (string.split("@Test").dropLastWhile { it.isEmpty() }.toTypedArray().size - 1).coerceAtLeast(0)
    }

    private fun countJestTests(string: String): Int {
        return "\\stest\\(".toRegex().findAll(string).count()
    }
}