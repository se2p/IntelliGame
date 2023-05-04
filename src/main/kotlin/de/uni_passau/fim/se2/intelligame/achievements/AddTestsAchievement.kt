package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File

object AddTestsAchievement : Achievement(), BulkFileListener {

    private var filesUnderObservation = hashMapOf<String, Int>()

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

    override fun before(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.path.endsWith("Test.java")) {
                val file = File(event.path)
                if (file.exists()) {
                    val counter =
                        countTests(
                            file.readText().replace("/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex(), "")
                        )
                    filesUnderObservation[event.path] = counter
                }
            }
            super.before(events)
        }
    }

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.path.endsWith("Test.java")) {
                val file = File(event.path)
                if (file.exists()) {
                    val counter =
                        countTests(
                            file.readText().replace("/\\*(?:[^*]|\\*+[^*/])*\\*+/|//.*".toRegex(), "")
                        )
                    if (filesUnderObservation.containsKey(event.path)
                        && filesUnderObservation[event.path]!! < counter) {
                        var progress = progress()
                        progress += 1
                        handleProgress(progress)
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
}