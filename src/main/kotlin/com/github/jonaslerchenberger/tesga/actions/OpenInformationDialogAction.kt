package com.github.jonaslerchenberger.tesga.actions

import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenInformationDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MoreInformationDialog(null).show()
    }
}