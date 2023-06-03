package de.uni_passau.fim.se2.intelligame.components

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class CopyToClipboardAction(private val uuid: String): AnAction("Copy to Clipboard", "", AllIcons.Actions.Copy) {

    override fun actionPerformed(e: AnActionEvent) {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(uuid), null)
    }
}