package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.listeners.ActionAchievement
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManagerListener

class RunXDebuggerModeAchievement {
    companion object : XDebuggerManagerListener,
        ActionAchievement() {
        override fun processStarted(debugProcess: XDebugProcess) {
            var progress = progress()
            progress += 1
            if (progress == nextStep()) {
                showAchievementNotification("Congratulations! You unlocked the Bronze Debugger Achievement")
            }
            updateProgress(progress)
            super.processStarted(debugProcess)
        }

        override fun progress(): Int {
            val properties = PropertiesComponent.getInstance()
            return properties.getInt("executedDebugger", 0)
        }

        override fun updateProgress(progress: Int) {
            val properties = PropertiesComponent.getInstance()
            properties.setValue("executedDebugger", progress, 0)
        }

        override fun getDescription(): String {
            return "You get this achievement by using the debug mode."
        }

        override fun getName(): String {
            return "Mr Debugger"
        }

        override fun nextStep(): Int {
            if (progress() > 3) {
                if (progress() > 10) {
                    if (progress() > 100) {
                        return 1000;
                    }
                    return 100;
                }
                return 10;
            }
            return 3;
        }
    }
}