/*
 * Copyright 2023 IntelliGame contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_passau.fim.se2.intelligame.services

import com.intellij.coverage.CoverageDataManagerImpl
import com.intellij.execution.ExecutionManager
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import de.uni_passau.fim.se2.intelligame.MyBundle
import de.uni_passau.fim.se2.intelligame.achievements.*
import de.uni_passau.fim.se2.intelligame.listeners.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File
import java.sql.Timestamp
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class ProjectService(project: Project): Disposable {

    init {
        println(MyBundle.message("projectService", project.name))

        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, TriggerXAssertsByTestsAchievement)
        project.messageBus.connect().subscribe(XDebuggerManager.TOPIC, RunXDebuggerModeAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RunXTestsAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RunXTestSuitesAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RunXTestSuitesWithXTestsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXBreakpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXConditionalBreakpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXFieldWatchpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXLineBreakpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXMethodBreakpointsAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, FindXBugsAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RepairXWrongTestsAchievement)

        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, RefactorAddXAssertionsAchievement)
        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, AddTestsAchievement)
        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, BulkFileListenerImpl)
        project.messageBus.connect().subscribe(ExecutionManager.EXECUTION_TOPIC, ConsoleListener)

        CoverageDataManagerImpl.getInstance(project)?.addSuiteListener(CoverageListener, this)

        if (MyBundle.message("projectURL").isNotEmpty() && MyBundle.message("projectToken").isNotEmpty()) {
            val properties = PropertiesComponent.getInstance()
            if (properties.getValue("uuid") == null)
                properties.setValue("uuid", UUID.randomUUID().toString())
            val projectPath = project.basePath

            if (project.name == MyBundle.message("projectName")) {
                createBranch(projectPath!!)
                commitAllFilesToBranch(projectPath, properties.getValue("uuid")!!)
            }

            val executorService = Executors.newScheduledThreadPool(1)
            executorService.scheduleAtFixedRate({
                try {
                    if (project.name == MyBundle.message("projectName")) {
                        commitAllFilesToBranch(projectPath!!, properties.getValue("uuid")!!)
                    }
                } catch (_: java.lang.Exception) {}
            }, 0, 1, TimeUnit.MINUTES)
        }
    }

    private fun createBranch(projectPath: String) {
        val properties = PropertiesComponent.getInstance()
        val uuid = properties.getValue("uuid")
        val gitWorkDir = File(projectPath)
        try {
            Git.open(gitWorkDir).use { git ->
                git.checkout().setName("main").call()
                git.checkout().setName(uuid).setCreateBranch(true).call()
            }
        } catch (_: Exception) {}
    }

    private fun commitAllFilesToBranch(projectPath: String, branch: String) {
        val gitWorkDir = File(projectPath)
        val user = UsernamePasswordCredentialsProvider(
            MyBundle.message("projectToken"), "")
        try {
            Git.open(gitWorkDir).use { git ->
                checkoutBranch(projectPath, branch)
                if (git.repository.branch != branch) return
                git.add().addFilepattern(".").call()
                val timestamp = Timestamp(System.currentTimeMillis())
                git.commit().setMessage(timestamp.toString()).call()
                git.push().setRemote(MyBundle.message("projectURL"))
                    .setCredentialsProvider(user).call()
            }
        } catch (_: Exception) {}

    }

    private fun checkoutBranch(projectPath: String, name: String) {
        val gitWorkDir = File(projectPath)
        try {
            Git.open(gitWorkDir).use { git ->
                if (git.branchList().call().find { it.name == name } == null) createBranch(projectPath)
                git.checkout().setName(name).call()
            }
        } catch (_: Exception) {}
    }

    override fun dispose() = Unit
}
