package de.uni_passau.fim.se2.intelligame.listeners

import de.uni_passau.fim.se2.intelligame.services.ProjectService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

internal class ProjectManagerImpl : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<ProjectService>()
    }
}
