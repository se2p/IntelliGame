package com.github.jonaslerchenberger.tesga.achievements

import com.github.jonaslerchenberger.tesga.listeners.ActionAchievement
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.ide.util.PropertiesComponent

class RunXTestsAchievement {
    companion object : SMTRunnerEventsListener,
        ActionAchievement() {
        override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        }

        override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
            var progress = SetXBreakpointsAchievement.progress()
            progress += 1
            if (progress == nextStep()) {
                showAchievementNotification("Congratulations! You unlocked the Bronze Tester Achievement")
            }
            SetXBreakpointsAchievement.updateProgress(progress)
        }

        override fun onTestsCountInSuite(count: Int) {
        }

        override fun onTestStarted(test: SMTestProxy) {
        }

        override fun onTestFinished(test: SMTestProxy) {
        }

        override fun onTestFailed(test: SMTestProxy) {
        }

        override fun onTestIgnored(test: SMTestProxy) {
        }

        override fun onSuiteFinished(suite: SMTestProxy) {
        }

        override fun onSuiteStarted(suite: SMTestProxy) {
        }

        override fun onCustomProgressTestsCategory(categoryName: String?, testCount: Int) {
        }

        override fun onCustomProgressTestStarted() {
        }

        override fun onCustomProgressTestFailed() {
        }

        override fun onCustomProgressTestFinished() {
        }

        override fun onSuiteTreeNodeAdded(testProxy: SMTestProxy?) {
        }

        override fun onSuiteTreeStarted(suite: SMTestProxy?) {
        }

        override fun progress(): Int {
            val properties = PropertiesComponent.getInstance()
            return properties.getInt("runXTestsAchievement", 0)
        }

        override fun updateProgress(progress: Int) {
            val properties = PropertiesComponent.getInstance()
            properties.setValue("runXTestsAchievement", progress, 0)
        }

        override fun getDescription(): String {
            return "Can be achieved by executing tests"
        }

        override fun getName(): String {
            return "Mr Tester"
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