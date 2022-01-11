package com.github.jonaslerchenberger.tesga.services

import com.github.jonaslerchenberger.tesga.MyBundle
import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.components.MoreInformationDialog
import com.github.jonaslerchenberger.tesga.listeners.ActionAchievement
import com.github.jonaslerchenberger.tesga.listeners.TestListener
import com.intellij.coverage.CoverageDataManagerImpl
import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.filters.ConsoleInputFilterProvider
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointListener

class ProjectService(private val project: Project) {
    private val actionAchievements = listOf<ActionAchievement>()

    init {
        println(MyBundle.message("projectService", project.name))
        PropertiesComponent.getInstance()
//        val jlistener = TestListener(project)
        val usedXPrintfDebuggingAchievement = UsedXPrintfDebuggingAchievement(project)
//        val setXConditionalBreakpointsAchievement = SetXConditionalBreakpointsAchievement(project)
//
//        val actionAchievements = listOf<ActionAchievement>(AssertTriggeredByTestAchievement, SetXConditionalBreakpointsAchievement, RunXDebuggerModeAchievement)
//        Util.setActionAchievements(actionAchievements)
//        project.getMessageBus().connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, jlistener)
        project.getMessageBus().connect().subscribe(ExecutionManager.EXECUTION_TOPIC, usedXPrintfDebuggingAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, AssertTriggeredByTestAchievement)
        project.messageBus.connect().subscribe(XDebuggerManager.TOPIC, RunXDebuggerModeAchievement)
        project.messageBus.connect().subscribe(SMTRunnerEventsListener.TEST_STATUS, RunXTestsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXBreakpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXConditionalBreakpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXFieldWatchpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXLineBreakpointsAchievement)
        project.messageBus.connect().subscribe(XBreakpointListener.TOPIC, SetXMethodBreakpointsAchievement)
        /*project.messageBus.connect().setDefaultHandler { method, objects ->
            for (`object` in objects) {
                println("method:$method");
            }
        }*/

//        var extensionPoint = Extensions.getRootArea().getExtensionPoint(ConsoleInputFilterProvider.INPUT_FILTER_PROVIDERS)
//        extensionPoint.registerExtension(inputFilterProvider, LoadingOrder.FIRST, disposable)

        var manager = CoverageDataManagerImpl(project);
        println(manager.state)

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
                        println("New Information")
                        val dialog = MoreInformationDialog(project)
                        dialog.show()

                    })
            )
            .notify(project)
    }
}
