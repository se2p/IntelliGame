package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener

object SetXMethodBreakpointsAchievement : XBreakpointListener<XBreakpoint<*>>,
    Achievement() {
    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        if (breakpoint.type.id == "java-method") {
            var progress = progress()
            progress += 1
            handleProgress(progress)
        }
        super.breakpointAdded(breakpoint)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("setXMethodBreakpointsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("setXMethodBreakpointsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Can be achieved by setting method breakpoints"
    }

    override fun getName(): String {
        return "Break the Method"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}