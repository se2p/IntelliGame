package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebuggerManagerListener

object RunXDebuggerModeAchievement : XDebuggerManagerListener,
    Achievement() {
    override fun processStarted(debugProcess: XDebugProcess) {
        var progress = progress()
        progress += 1
        handleProgress(progress)
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
        return "Debugger"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}