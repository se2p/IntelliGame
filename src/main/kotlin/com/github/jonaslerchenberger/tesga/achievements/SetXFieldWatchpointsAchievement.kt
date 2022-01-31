package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener

class SetXFieldWatchpointsAchievement {
    companion object : XBreakpointListener<XBreakpoint<*>>,
        Achievement() {
        override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
            if (breakpoint.type.id == "java-field") {
                var progress = progress()
                progress += 1
                if (progress == nextStep()) {
                    showAchievementNotification("Congratulations! You unlocked the Bronze On the Watch Achievement")
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