package de.uni_passau.fim.se2.intelligame.components

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.UIUtil
import de.uni_passau.fim.se2.intelligame.achievements.Achievement
import de.uni_passau.fim.se2.intelligame.achievements.Achievement.Language
import de.uni_passau.fim.se2.intelligame.util.Util
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JProgressBar


class AchievementToolWindow : ToolWindowFactory {

    private val contentFactory = ContentFactory.getInstance()

    companion object {
        fun createPanel(): JComponent {

            val main = JPanel()
            main.minimumSize = Dimension(400, 300)
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
                    icon(TrophyIcons.trophyPlatinIcon)
                    label("").resizableColumn()
                }.resizableRow()
                val dialogPanel = achievementList()
                row {
                    dialogPanel.border = JBEmptyBorder(5)
                    cell(dialogPanel).align(Align.FILL).resizableColumn()
                }.resizableRow()
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
                                2 -> if (UIUtil.isUnderDarcula()) {
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
                                2 -> if (UIUtil.isUnderDarcula()) {
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
                                2 -> if (UIUtil.isUnderDarcula()) {
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
                                2 -> if (UIUtil.isUnderDarcula()) {
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
                                2 -> if (UIUtil.isUnderDarcula()) {
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
        toolWindow.setIcon(TrophyIcons.trophyDefaultIcon)
        val content = contentFactory.createContent(createPanel(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun getAnchor(): ToolWindowAnchor {
        return ToolWindowAnchor.RIGHT
    }

    override fun shouldBeAvailable(project: Project) = true
}