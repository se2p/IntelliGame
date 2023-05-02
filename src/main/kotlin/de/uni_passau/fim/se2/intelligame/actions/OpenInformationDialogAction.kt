package de.uni_passau.fim.se2.intelligame.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import de.uni_passau.fim.se2.intelligame.components.MoreInformationDialog

class OpenInformationDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MoreInformationDialog(null).show()
    }
}