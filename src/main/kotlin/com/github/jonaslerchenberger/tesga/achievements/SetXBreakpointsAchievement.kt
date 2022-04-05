package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener

object SetXBreakpointsAchievement : XBreakpointListener<XBreakpoint<*>>,
    Achievement() {
    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        var progress = progress()
        progress += 1
        if (progress == nextStep()) {
            showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the 'Take some breaks' Achievement")
        }
        updateProgress(progress)
        super.breakpointAdded(breakpoint)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("setXBreakpointsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("setXBreakpointsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Can be achieved by setting breakpoints"
    }

    override fun getName(): String {
        return "Take some breaks"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }
}