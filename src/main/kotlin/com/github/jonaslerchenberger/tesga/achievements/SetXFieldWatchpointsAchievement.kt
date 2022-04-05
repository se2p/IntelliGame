package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener

object SetXFieldWatchpointsAchievement : XBreakpointListener<XBreakpoint<*>>,
    Achievement() {
    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        if (breakpoint.type.id == "java-field") {
            var progress = progress()
            progress += 1
            if (progress == nextStep()) {
                showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the 'On the Watch' Achievement")
            }
            updateProgress(progress)
        }
        super.breakpointAdded(breakpoint)
    }

    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("setXFieldWatchpointsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("setXFieldWatchpointsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Can be achieved by setting field watchpoints"
    }

    override fun getName(): String {
        return "On the Watch"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}