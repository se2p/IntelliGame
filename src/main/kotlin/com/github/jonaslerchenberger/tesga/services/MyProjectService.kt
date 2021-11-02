package com.github.jonaslerchenberger.tesga.services

import com.intellij.openapi.project.Project
import com.github.jonaslerchenberger.tesga.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
