package com.github.jonaslerchenberger.tesga.components

import com.github.jonaslerchenberger.tesga.achievements.*
import com.github.jonaslerchenberger.tesga.icons.TrophyIcons
import com.github.jonaslerchenberger.tesga.services.Util
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.DialogPanel
import com.intellij.ui.components.JBScrollBar
import com.intellij.ui.components.dialog
import com.intellij.ui.dsl.builder.panel
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.util.ui.JBEmptyBorder
import org.jetbrains.annotations.Nullable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*


class MoreInformationDialog(val project: Project?) : DialogWrapper(project, null, true, IdeModalityType.MODELESS, false) {
    @Nullable
    override fun createCenterPanel(): JComponent? {

        val main = JPanel()
        main.minimumSize = Dimension(400, 300)
//        main.maximumSize = Dimension(600, 400)
        main.border = JBEmptyBorder(10)
        val content = panel {
            val dialogPanel = achievementList(myDisposable)
            row {
                dialogPanel.border = JBEmptyBorder(10)
                cell(dialogPanel).horizontalAlign(HorizontalAlign.FILL).verticalAlign(VerticalAlign.FILL).resizableColumn()
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
            for (achievement in Util.getAchievements()) {
                row {
                    when (achievement.getLevel()) {
                        0 -> icon(TrophyIcons.trophyDefaultIcon)
                        1 -> icon(TrophyIcons.trophyBronzeIcon)
                        2 -> icon(TrophyIcons.trophySilverIcon)
                        3 -> icon(TrophyIcons.trophyGoldIcon)
                        else -> {
                            icon(TrophyIcons.trophyDefaultIcon)
                        }
                    }
                    label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                    contextHelp(achievement.getDescription(), achievement.getName())
                    val progressBar = JProgressBar(0, achievement.nextStep());
                    progressBar.value = achievement.progress();
                    progressBar.isStringPainted = false;
                    cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                    label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                }.layout(RowLayout.PARENT_GRID)
            }
        }

        panel.minimumSize = Dimension(400, 800)
//        panel.preferredSize = Dimension(500, 500)

        val disposable = Disposer.newDisposable()
        panel.registerValidators(disposable)
        Disposer.register(parentDisposable, disposable)

        return panel
    }
}