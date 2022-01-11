package com.github.jonaslerchenberger.tesga.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.github.jonaslerchenberger.tesga.services.ProjectService

internal class ProjectManagerImpl : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<ProjectService>()
    }
}
