package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import com.intellij.xdebugger.impl.breakpoints.XBreakpointBase

object SetXConditionalBreakpointsAchievement : XBreakpointListener<XBreakpoint<*>>,
    Achievement() {

    override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
        if (breakpoint is XBreakpointBase<*, *, *>
            && breakpoint.isConditionEnabled
            && breakpoint.getConditionExpression() != null) {
            var progress = progress()
            progress += 1
            handleProgress(progress)
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

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 3, 1 to 10, 2 to 100, 3 to 1000)
    }
}