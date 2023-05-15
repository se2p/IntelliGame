package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File

object RefactorAddXAssertionsAchievement : BulkFileListener, Achievement() {
    private var filesUnderObservation = hashMapOf<String, Int>()
    override fun before(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = File(event.path)
            if (file.exists()) {
                var counter = 0
                if (event.path.endsWith("Test.java")) {
                    counter =
                        countAsserts(
                            file.readText().replace("/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex(), "")
                        )
                } else if (event.path.endsWith("test.js")) {
                    counter =
                        countJestAsserts(
                            file.readText().replace("/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex(), "")
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
                        countAsserts(
                            file.readText().replace("/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex(), "")
                        )
                } else if (event.path.endsWith("test.js")) {
                    counter =
                        countJestAsserts(
                            file.readText().replace("/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex(), "")
                        )
                }

                if (counter != 0) {
                    if (filesUnderObservation.containsKey(event.path)
                        && filesUnderObservation[event.path]!! < counter) {
                        var progress = progress()
                        progress += (counter - filesUnderObservation[event.path]!!)
                        handleProgress(progress)
                    }
                    filesUnderObservation[event.path] = counter
                }
            }
        }
        super.after(events)
    }

    private fun countAsserts(string: String): Int {
        return (string.split("assert").dropLastWhile { it.isEmpty() }.toTypedArray().size - 1).coerceAtLeast(0)
    }

    private fun countJestAsserts(string: String): Int {
        return "\\sexpect\\(".toRegex().findAll(string).count()
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("RefactorAddXAssertionsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("RefactorAddXAssertionsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Add assertions to your tests."
    }

    override fun getName(): String {
        return "Double check"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }

    override fun supportsLanguages(): List<Language> {
        return listOf(Language.Java, Language.JavaScript)
    }
}