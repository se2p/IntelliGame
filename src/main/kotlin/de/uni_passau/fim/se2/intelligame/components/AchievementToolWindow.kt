/*
 * Copyright 2023 IntelliGame contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_passau.fim.se2.intelligame.components

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor.isBright
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.JBEmptyBorder
import de.uni_passau.fim.se2.intelligame.MyBundle
import de.uni_passau.fim.se2.intelligame.achievements.Achievement.Language
import de.uni_passau.fim.se2.intelligame.util.Util
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JProgressBar


class AchievementToolWindow : ToolWindowFactory {

    private val contentFactory = ContentFactory.getInstance()

    companion object {
        fun createPanel(): JComponent {

            val main = JPanel()
            main.border = JBEmptyBorder(5)
            val content = panel {

                if (MyBundle.message("group") != "control") {
                    row {
                        label("").resizableColumn()
                        icon(TrophyIcons.trophyDefaultIcon)
                        icon(TrophyIcons.trophyBronzeIcon)
                        if (!isBright()) {
                            icon(TrophyIcons.trophySilverIcon)
                        } else {
                            icon(TrophyIcons.trophySilverLightIcon)
                        }
                        icon(TrophyIcons.trophyGoldIcon)
                        icon(TrophyIcons.trophyPlatinIcon)
                        label("").resizableColumn()
                    }.resizableRow()

                    val dialogPanel = achievementList()

                    row {
                        dialogPanel.border = JBEmptyBorder(5)
                        cell(dialogPanel).align(Align.FILL).resizableColumn()
                    }.resizableRow()
                }

                if (MyBundle.message("projectURL").isNotEmpty() && MyBundle.message("projectToken").isNotEmpty()) {
                    val properties = PropertiesComponent.getInstance()
                    val uuid = properties.getValue("uuid")

                    separator()
                    row {
                        label("").resizableColumn()
                        label(uuid!!).bold()
                        actionButton(CopyToClipboardAction(uuid))
                        label("").resizableColumn()
                    }.resizableRow()
                }
            }
            main.add(content)
            return JBScrollPane(main)
        }

        private fun achievementList(): JPanel {

            val panel = panel {
                groupRowsRange("Testing") {
                    for (achievement in Util.getTestsAchievement()) {
                        row {
                            when (achievement.getLevel()) {
                                0 -> icon(TrophyIcons.trophyDefaultIcon)
                                1 -> icon(TrophyIcons.trophyBronzeIcon)
                                2 -> if (!isBright()) {
                                    icon(TrophyIcons.trophySilverIcon)
                                } else {
                                    icon(TrophyIcons.trophySilverLightIcon)
                                }
                                3 -> icon(TrophyIcons.trophyGoldIcon)
                                else -> {
                                    icon(TrophyIcons.trophyPlatinIcon)
                                }
                            }
                            label(achievement.getName()).align(AlignX.LEFT)
                            contextHelp(achievement.getDescription(), achievement.getName())
                            if (achievement.supportsLanguages().contains(Language.Java)) {
                                icon(TrophyIcons.javaIcon)
                            } else {
                                cell()
                            }
                            if (achievement.supportsLanguages().contains(Language.JavaScript)) {
                                icon(TrophyIcons.javaScriptIcon)
                            } else {
                                cell()
                            }
                            if (achievement.getLevel() > 3) {
                                val progressBar = JProgressBar(1, 1)
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString())
                            } else {
                                val progressBar = JProgressBar(0, achievement.nextStep())
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                            }
                        }.layout(RowLayout.PARENT_GRID)
                    }
                }
                groupRowsRange("Coverage") {
                    for (achievement in Util.getCoverageAchievements()) {
                        row {
                            when (achievement.getLevel()) {
                                0 -> icon(TrophyIcons.trophyDefaultIcon)
                                1 -> icon(TrophyIcons.trophyBronzeIcon)
                                2 -> if (!isBright()) {
                                    icon(TrophyIcons.trophySilverIcon)
                                } else {
                                    icon(TrophyIcons.trophySilverLightIcon)
                                }
                                3 -> icon(TrophyIcons.trophyGoldIcon)
                                else -> {
                                    icon(TrophyIcons.trophyPlatinIcon)
                                }
                            }
                            label(achievement.getName()).align(AlignX.LEFT)
                            contextHelp(achievement.getDescription(), achievement.getName())
                            if (achievement.supportsLanguages().contains(Language.Java)) {
                                icon(TrophyIcons.javaIcon)
                            } else {
                                cell()
                            }
                            if (achievement.supportsLanguages().contains(Language.JavaScript)) {
                                icon(TrophyIcons.javaScriptIcon)
                            } else {
                                cell()
                            }
                            if (achievement.getLevel() > 3) {
                                val progressBar = JProgressBar(1, 1)
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString())
                            } else {
                                val progressBar = JProgressBar(0, achievement.nextStep())
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                            }
                        }.layout(RowLayout.PARENT_GRID)
                    }
                }
                groupRowsRange("Coverage - Advanced") {
                    for (achievement in Util.getAdvancedCoverageAchievements()) {
                        row {
                            when (achievement.getLevel()) {
                                0 -> icon(TrophyIcons.trophyDefaultIcon)
                                1 -> icon(TrophyIcons.trophyBronzeIcon)
                                2 -> if (!isBright()) {
                                    icon(TrophyIcons.trophySilverIcon)
                                } else {
                                    icon(TrophyIcons.trophySilverLightIcon)
                                }
                                3 -> icon(TrophyIcons.trophyGoldIcon)
                                else -> {
                                    icon(TrophyIcons.trophyPlatinIcon)
                                }
                            }
                            label(achievement.getName()).align(AlignX.LEFT)
                            contextHelp(achievement.getDescription(), achievement.getName())
                            if (achievement.supportsLanguages().contains(Language.Java)) {
                                icon(TrophyIcons.javaIcon)
                            } else {
                                cell()
                            }
                            if (achievement.supportsLanguages().contains(Language.JavaScript)) {
                                icon(TrophyIcons.javaScriptIcon)
                            } else {
                                cell()
                            }
                            if (achievement.getLevel() > 3) {
                                val progressBar = JProgressBar(1, 1)
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString())
                            } else {
                                val progressBar = JProgressBar(0, achievement.nextStep())
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                            }
                        }.layout(RowLayout.PARENT_GRID)
                    }
                }
                groupRowsRange("Debugging") {
                    for (achievement in Util.getDebuggingAchievements()) {
                        row {
                            when (achievement.getLevel()) {
                                0 -> icon(TrophyIcons.trophyDefaultIcon)
                                1 -> icon(TrophyIcons.trophyBronzeIcon)
                                2 -> if (!isBright()) {
                                    icon(TrophyIcons.trophySilverIcon)
                                } else {
                                    icon(TrophyIcons.trophySilverLightIcon)
                                }
                                3 -> icon(TrophyIcons.trophyGoldIcon)
                                else -> {
                                    icon(TrophyIcons.trophyPlatinIcon)
                                }
                            }
                            label(achievement.getName()).align(AlignX.LEFT)
                            contextHelp(achievement.getDescription(), achievement.getName())
                            if (achievement.supportsLanguages().contains(Language.Java)) {
                                icon(TrophyIcons.javaIcon)
                            } else {
                                cell()
                            }
                            if (achievement.supportsLanguages().contains(Language.JavaScript)) {
                                icon(TrophyIcons.javaScriptIcon)
                            } else {
                                cell()
                            }
                            if (achievement.getLevel() > 3) {
                                val progressBar = JProgressBar(1, 1)
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString())
                            } else {
                                val progressBar = JProgressBar(0, achievement.nextStep())
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                            }
                        }.layout(RowLayout.PARENT_GRID)
                    }
                }
                groupRowsRange("Test Refactoring") {
                    for (achievement in Util.getRefactoringAchievements()) {
                        row {
                            when (achievement.getLevel()) {
                                0 -> icon(TrophyIcons.trophyDefaultIcon)
                                1 -> icon(TrophyIcons.trophyBronzeIcon)
                                2 -> if (!isBright()) {
                                    icon(TrophyIcons.trophySilverIcon)
                                } else {
                                    icon(TrophyIcons.trophySilverLightIcon)
                                }
                                3 -> icon(TrophyIcons.trophyGoldIcon)
                                else -> {
                                    icon(TrophyIcons.trophyPlatinIcon)
                                }
                            }
                            label(achievement.getName()).align(AlignX.LEFT)
                            contextHelp(achievement.getDescription(), achievement.getName())
                            if (achievement.supportsLanguages().contains(Language.Java)) {
                                icon(TrophyIcons.javaIcon)
                            } else {
                                cell()
                            }
                            if (achievement.supportsLanguages().contains(Language.JavaScript)) {
                                icon(TrophyIcons.javaScriptIcon)
                            } else {
                                cell()
                            }
                            if (achievement.getLevel() > 3) {
                                val progressBar = JProgressBar(1, 1)
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString())
                            } else {
                                val progressBar = JProgressBar(0, achievement.nextStep())
                                progressBar.value = achievement.progress()
                                progressBar.isStringPainted = false
                                cell(progressBar).align(AlignX.RIGHT)
                                label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                            }
                        }.layout(RowLayout.PARENT_GRID)
                    }
                }
            }

            return panel
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = if (MyBundle.message("group") == "control") {
            contentFactory.createContent(createPanel(), null, false)
        } else {
            toolWindow.setIcon(TrophyIcons.trophyToolWindowIcon)
            contentFactory.createContent(createPanel(), null, false)
        }

        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true
}