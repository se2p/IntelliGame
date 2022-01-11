package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.listeners.ActionAchievement
import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener

class SetXLineBreakpointsAchievement {
    companion object : XBreakpointListener<XBreakpoint<*>>,
        ActionAchievement() {
        override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
            if (breakpoint.type.id == "java-line") {
                var progress = progress()
                progress += 1
                if (progress == nextStep()) {
                    showAchievementNotification("Congratulations! You unlocked the Bronze Break the line Achievement")
                }
                updateProgress(progress)
            }
            super.breakpointAdded(breakpoint)
        }

        override fun progress(): Int {
            val properties = PropertiesComponent.getInstance()
            return properties.getInt("setXLineBreakpointsAchievement", 0)
        }

        override fun updateProgress(progress: Int) {
            val properties = PropertiesComponent.getInstance()
            properties.setValue("setXLineBreakpointsAchievement", progress, 0)
        }

        override fun getDescription(): String {
            return "Can be achieved by setting line breakpoints"
        }

        override fun getName(): String {
            return "Break the Line"
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