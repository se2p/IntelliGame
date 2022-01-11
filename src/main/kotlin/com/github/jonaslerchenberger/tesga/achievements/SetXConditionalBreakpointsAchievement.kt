package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.listeners.ActionAchievement
import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import com.intellij.xdebugger.impl.breakpoints.XBreakpointBase

class SetXConditionalBreakpointsAchievement {
    companion object : XBreakpointListener<XBreakpoint<*>>,
        ActionAchievement() {

        override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
            if (breakpoint is XBreakpointBase<*, *, *>) {
                if (breakpoint.isConditionEnabled && breakpoint.getConditionExpression() != null) {
                    var progress = progress()
                    progress += 1
                    if (progress == nextStep()) {
                        showAchievementNotification("Congratulations! You unlocked the Bronze Make Your Choice Achievement")
                    }
                    updateProgress(progress)
                }
            }
            super.breakpointChanged(breakpoint)
        }

        override fun progress(): Int {
            val properties = PropertiesComponent.getInstance()
            return properties.getInt("setXConditionalBreakpointsAchievement", 0)
        }

        override fun updateProgress(progress: Int) {
            val properties = PropertiesComponent.getInstance()
            properties.setValue("setXConditionalBreakpointsAchievement", progress, 0)
        }

        override fun getDescription(): String {
            return "Can be achieved by setting conditional breakpoints"
        }

        override fun getName(): String {
            return "Make Your Choice"
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