package com.github.jonaslerchenberger.tesga.listeners

import com.github.jonaslerchenberger.tesga.achievements.RefactorExtractXMethodsAchievement
import com.github.jonaslerchenberger.tesga.achievements.RefactorInlineXMethodsAchievement
import com.github.jonaslerchenberger.tesga.achievements.RefactorXTestNamesAchievement
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import gr.uom.java.xmi.UMLModel
import gr.uom.java.xmi.UMLModelASTReader
import gr.uom.java.xmi.diff.ExtractOperationRefactoring
import gr.uom.java.xmi.diff.InlineOperationRefactoring
import gr.uom.java.xmi.diff.RenameOperationRefactoring
import gr.uom.java.xmi.diff.UMLModelDiff
import org.refactoringminer.api.Refactoring
import java.io.File


object BulkFileListenerImpl : BulkFileListener {
    private var filesUnderObservation = hashMapOf<String, UMLModel>()

    override fun before(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.path.endsWith(".java")) {
                val file = File(event.path)
                if (file.exists()) {
                    val folder = file.parentFile
                    val modelBefore: UMLModel = UMLModelASTReader(folder).umlModel
                    filesUnderObservation[folder.path] = modelBefore
                }
            }
        }
        super.before(events)
    }

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.path.endsWith(".java")) {
                val file = File(event.path)
                if (file.exists()) {
                    val folder = file.parentFile
                    var modelAfter: UMLModel = UMLModelASTReader(folder).umlModel
                    var modelBefore = filesUnderObservation[folder.path]
                    val modelDiff: UMLModelDiff? = modelBefore?.diff(modelAfter)
                    val refactorings: List<Refactoring> = modelDiff?.refactorings ?: listOf()
                    for (refactoring in refactorings) {
                        when (refactoring) {
                            is RenameOperationRefactoring -> {
                                if (refactoring.originalOperation.name.length < refactoring.renamedOperation.name.length) {
                                    RefactorXTestNamesAchievement.triggerAchievement()
                                }
                            }
                            is ExtractOperationRefactoring -> {
                                RefactorExtractXMethodsAchievement.triggerAchievement()
                            }
                            is InlineOperationRefactoring -> {
                                RefactorInlineXMethodsAchievement.triggerAchievement()
                            }
                        }
                    }
                }
            }
        }
        /*if (events.size > 0) {
            var text = events[0].file?.let { LoadTextUtil.loadText(it) }
            println("text$text")
        }*/
        //TODO: activate generation
        //CSVReportGenerator.generateCSVReport()
        super.after(events)
    }

}