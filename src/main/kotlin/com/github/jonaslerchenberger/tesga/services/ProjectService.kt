package com.github.jonaslerchenberger.tesga.services

import com.github.jonaslerchenberger.tesga.MyBundle
import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.github.jonaslerchenberger.tesga.listeners.*
import com.intellij.coverage.CoverageDataManagerImpl
import com.intellij.execution.ExecutionManager
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.impl.PsiManagerImpl
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointListener

class ProjectService(private val project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
        PropertiesComponent.getInstance()
//        val jlistener = TestListener(project)
        val editorListenerImpl = EditorListenerImpl()
//        val setXConditionalBreakpointsAchievement = SetXConditionalBreakpointsAchievement(project)
//
//        val actionAchievements = listOf<ActionAchievement>(AssertTriggeredByTestAchievement, SetXConditionalBreakpointsAchievement, RunXDebuggerModeAchievement)
//        Util.setActionAchievements(actionAchievements)
//        project.getMessageBus().connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, jlistener)
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
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, UseXPrintfDebuggingAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RepairXWrongTestsAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RefactorCodeAchievement)
        project.messageBus.connect().subscribe(ExecutionManager.EXECUTION_TOPIC, ExecutionListenerImpl)

        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, BulkFileListenerImpl)
        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, RefactorAddXAssertionsAchievement)

//        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, TestListener)

        FindXBugsAchievement.setProject(project)
        UseXPrintfDebuggingAchievement.setProject(project)
        RepairXWrongTestsAchievement.setProject(project)
        RefactorCodeAchievement.setProject(project)
        TestListener.setProject(project)
//        project.messageBus.connect().subscribe(EditorTrackerListener.TOPIC, editorListenerImpl)
//        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, bulkFileListenerImpl)
        /*project.messageBus.connect().setDefaultHandler { method, objects ->
            for (`object` in objects) {
                println("method:$method");
            }
        }*/

//        var extensionPoint = Extensions.getRootArea().getExtensionPoint(ConsoleInputFilterProvider.INPUT_FILTER_PROVIDERS)
//        extensionPoint.registerExtension(inputFilterProvider, LoadingOrder.FIRST, disposable)
        CoverageDataManagerImpl.getInstance(project)?.addSuiteListener(CoverageListener, Disposer.newDisposable())


        val psiManager = PsiManagerImpl.getInstance(project);
        psiManager.addPsiTreeChangeListener(AddTestsAchievement, Disposer.newDisposable())

        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
            .createNotification("Plugin started successfully", NotificationType.INFORMATION)
            .notify(project)

        showAchievementNotification("Just for testing")
    }

    fun showAchievementNotification(message: String) {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
            .createNotification(
                message,
                NotificationType.INFORMATION
            )
            .addAction(
                NotificationAction.createSimple("Show more information",
                    Runnable {
                        val dialog = MoreInformationDialog(project)
                        dialog.show()

                    })
            )
            .notify(project)
    }
}
