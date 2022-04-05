package com.github.jonaslerchenberger.tesga.components

import com.github.jonaslerchenberger.tesga.icons.TrophyIcons
import com.github.jonaslerchenberger.tesga.services.Util
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.Disposer
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.UIUtil
import org.jetbrains.annotations.Nullable
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JProgressBar


class MoreInformationDialog(val project: Project?) :
    DialogWrapper(project, null, true, IdeModalityType.MODELESS, false) {
    @Nullable
    override fun createCenterPanel(): JComponent? {

        val main = JPanel()
        main.minimumSize = Dimension(400, 300)
//        main.maximumSize = Dimension(600, 400)
        main.border = JBEmptyBorder(5)
        val content = panel {
            row {
                label("").resizableColumn()
                icon(TrophyIcons.trophyDefaultIcon)
                icon(TrophyIcons.trophyBronzeIcon)
                if (UIUtil.isUnderDarcula()) {
                    icon(TrophyIcons.trophySilverIcon)
                } else {
                    icon(TrophyIcons.trophySilverLightIcon)
                }
                icon(TrophyIcons.trophyGoldIcon)
                label("").resizableColumn()
            }.resizableRow()
            val dialogPanel = achievementList(myDisposable)
            row {
                dialogPanel.border = JBEmptyBorder(5)
                cell(dialogPanel).horizontalAlign(HorizontalAlign.FILL).verticalAlign(VerticalAlign.FILL)
                    .resizableColumn()
            }.resizableRow()
        }
        main.add(content)
        return JBScrollPane(main)
    }

    init {
        title = "Achievements"
        setSize(600, 800)
        init()
    }

    private fun achievementList(parentDisposable: Disposable): JPanel {
        val panel = panel {
            groupRowsRange("Testing") {
                for (achievement in Util.getTestsAchievement()) {
                    row {
                        when (achievement.getLevel()) {
                            0 -> icon(TrophyIcons.trophyDefaultIcon)
                            1 -> icon(TrophyIcons.trophyBronzeIcon)
                            2 -> if (UIUtil.isUnderDarcula()) {
                                icon(TrophyIcons.trophySilverIcon)
                            } else {
                                icon(TrophyIcons.trophySilverLightIcon)
                            }
                            3 -> icon(TrophyIcons.trophyGoldIcon)
                            else -> {
                                icon(TrophyIcons.trophyDefaultIcon)
                            }
                        }
                        label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                        contextHelp(achievement.getDescription(), achievement.getName())
                        val progressBar = JProgressBar(0, achievement.nextStep())
                        progressBar.value = achievement.progress()
                        progressBar.isStringPainted = false
                        cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                        label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                    }.layout(RowLayout.PARENT_GRID)
                }
            }
            groupRowsRange("Coverage") {
                for (achievement in Util.getCoverageAchievements()) {
                    row {
                        when (achievement.getLevel()) {
                            0 -> icon(TrophyIcons.trophyDefaultIcon)
                            1 -> icon(TrophyIcons.trophyBronzeIcon)
                            2 -> if (UIUtil.isUnderDarcula()) {
                                icon(TrophyIcons.trophySilverIcon)
                            } else {
                                icon(TrophyIcons.trophySilverLightIcon)
                            }
                            3 -> icon(TrophyIcons.trophyGoldIcon)
                            else -> {
                                icon(TrophyIcons.trophyDefaultIcon)
                            }
                        }
                        label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                        contextHelp(achievement.getDescription(), achievement.getName())
                        val progressBar = JProgressBar(0, achievement.nextStep())
                        progressBar.value = achievement.progress()
                        progressBar.isStringPainted = false
                        cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                        label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                    }.layout(RowLayout.PARENT_GRID)
                }
            }
            groupRowsRange("Coverage - Advanced") {
                for (achievement in Util.getAdvancedCoverageAchievements()) {
                    row {
                        when (achievement.getLevel()) {
                            0 -> icon(TrophyIcons.trophyDefaultIcon)
                            1 -> icon(TrophyIcons.trophyBronzeIcon)
                            2 -> if (UIUtil.isUnderDarcula()) {
                                icon(TrophyIcons.trophySilverIcon)
                            } else {
                                icon(TrophyIcons.trophySilverLightIcon)
                            }
                            3 -> icon(TrophyIcons.trophyGoldIcon)
                            else -> {
                                icon(TrophyIcons.trophyDefaultIcon)
                            }
                        }
                        label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                        contextHelp(achievement.getDescription(), achievement.getName())
                        val progressBar = JProgressBar(0, achievement.nextStep())
                        progressBar.value = achievement.progress()
                        progressBar.isStringPainted = false
                        cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                        label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                    }.layout(RowLayout.PARENT_GRID)
                }
            }
            groupRowsRange("Debugging") {
                for (achievement in Util.getDebuggingAchievements()) {
                    row {
                        when (achievement.getLevel()) {
                            0 -> icon(TrophyIcons.trophyDefaultIcon)
                            1 -> icon(TrophyIcons.trophyBronzeIcon)
                            2 -> if (UIUtil.isUnderDarcula()) {
                                icon(TrophyIcons.trophySilverIcon)
                            } else {
                                icon(TrophyIcons.trophySilverLightIcon)
                            }
                            3 -> icon(TrophyIcons.trophyGoldIcon)
                            else -> {
                                icon(TrophyIcons.trophyDefaultIcon)
                            }
                        }
                        label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                        contextHelp(achievement.getDescription(), achievement.getName())
                        val progressBar = JProgressBar(0, achievement.nextStep())
                        progressBar.value = achievement.progress()
                        progressBar.isStringPainted = false
                        cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                        label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                    }.layout(RowLayout.PARENT_GRID)
                }
            }
            groupRowsRange("Refactoring") {
                for (achievement in Util.getRefactoringAchievements()) {
                    row {
                        when (achievement.getLevel()) {
                            0 -> icon(TrophyIcons.trophyDefaultIcon)
                            1 -> icon(TrophyIcons.trophyBronzeIcon)
                            2 -> if (UIUtil.isUnderDarcula()) {
                                icon(TrophyIcons.trophySilverIcon)
                            } else {
                                icon(TrophyIcons.trophySilverLightIcon)
                            }
                            3 -> icon(TrophyIcons.trophyGoldIcon)
                            else -> {
                                icon(TrophyIcons.trophyDefaultIcon)
                            }
                        }
                        label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                        contextHelp(achievement.getDescription(), achievement.getName())
                        val progressBar = JProgressBar(0, achievement.nextStep())
                        progressBar.value = achievement.progress()
                        progressBar.isStringPainted = false
                        cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                        label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                    }.layout(RowLayout.PARENT_GRID)
                }
            }
        }

//        panel.minimumSize = Dimension(400, 300)
//        panel.preferredSize = Dimension(500, 500)

        val disposable = Disposer.newDisposable()
        panel.registerValidators(disposable)
        Disposer.register(parentDisposable, disposable)

        return panel
    }
}