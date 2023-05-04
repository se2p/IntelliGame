package de.uni_passau.fim.se2.intelligame.services

import com.intellij.coverage.CoverageDataManagerImpl
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import de.uni_passau.fim.se2.intelligame.MyBundle
import de.uni_passau.fim.se2.intelligame.achievements.*
import de.uni_passau.fim.se2.intelligame.listeners.*

class ProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
        PropertiesComponent.getInstance()

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

        FindXBugsAchievement.setProject(project)
        RepairXWrongTestsAchievement.setProject(project)

        CoverageDataManagerImpl.getInstance(project)?.addSuiteListener(CoverageListener, Disposer.newDisposable())
    }
}
