package de.uni_passau.fim.se2.intelligame.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import de.uni_passau.fim.se2.intelligame.services.ProjectService

internal class ProjectActivity: ProjectActivity {

    override suspend fun execute(project: Project) {
        project.service<ProjectService>()
    }
}