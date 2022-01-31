package com.github.jonaslerchenberger.tesga.components

import com.github.jonaslerchenberger.tesga.achievements.*
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
        main.preferredSize = Dimension(840, 600)
        main.border = JBEmptyBorder(10)
        val content = panel {
            row {
                label("Achievements")
            }
            val dialogPanel = achievementList(myDisposable)
            row {
                dialogPanel.border = JBEmptyBorder(10)
                cell(dialogPanel).horizontalAlign(HorizontalAlign.FILL).verticalAlign(VerticalAlign.FILL).resizableColumn()
            }.resizableRow()
        }
        main.add(content)
        return main
    }

    init {
        title = "Achievements"
        init()
    }

    fun achievementList(parentDisposable: Disposable): DialogPanel {
        val panel = panel {
            for (achievement in Util.getAchievements()) {
                row {
                    label(achievement.getName()).horizontalAlign(HorizontalAlign.LEFT)
                    label(achievement.getDescription()).horizontalAlign(HorizontalAlign.LEFT)
                    val progressBar = JProgressBar(0, achievement.nextStep());
                    progressBar.value = achievement.progress();
                    progressBar.isStringPainted = false;
                    cell(progressBar).horizontalAlign(HorizontalAlign.RIGHT)
                    label(achievement.progress().toString() + " / " + achievement.nextStep().toString())
                }.layout(RowLayout.PARENT_GRID)
            }
            /*row("Debugger") {
                label("This is a Description of the Achievement")
                val progressBar = JProgressBar(0, 100);
                progressBar.value = 60;
                progressBar.isStringPainted = true;
                cell(progressBar)
            }
            row {
                checkBox("checkBox")
            }

            row {
                button("button") {}
            }

            row("actionButton:") {
                val action = object : DumbAwareAction("Action text", "Action description", AllIcons.Actions.QuickfixOffBulb) {
                    override fun actionPerformed(e: AnActionEvent) {
                    }
                }
                actionButton(action)
            }

            row("actionsButton:") {
                actionsButton(object : DumbAwareAction("Action one") {
                    override fun actionPerformed(e: AnActionEvent) {
                    }
                },
                    object : DumbAwareAction("Action two") {
                        override fun actionPerformed(e: AnActionEvent) {
                        }
                    })
            }

            row("label:") {
                label("Some label")
            }

            row("text:") {
                text("text supports max line width and can contain links, try <a href='https://www.jetbrains.com'>jetbrains.com</a>")
            }

            row("link:") {
                link("Focusable link") {}
            }

            row("browserLink:") {
                browserLink("jetbrains.com", "https://www.jetbrains.com")
            }

            row("dropDownLink:") {
                dropDownLink("Item 1", listOf("Item 1", "Item 2", "Item 3"))
            }

            row("icon:") {
                icon(AllIcons.Actions.QuickfixOffBulb)
            }

            row("contextHelp:") {
                contextHelp("contextHelp description", "contextHelp title")
            }

            row("textField:") {
                textField()
            }

            row("textFieldWithBrowseButton:") {
                textFieldWithBrowseButton()
            }

            row("expandableTextField:") {
                ExpandableTextField()
            }

            row("intTextField(0..100):") {
                intTextField(0..100)
            }

            row("spinner(0..100):") {
                spinner(0..100)
            }

            row("spinner(0.0..100.0, 0.01):") {
                spinner(0.0..100.0, 0.01)
            }

            row {
                label("textArea:")
                    .verticalAlign(VerticalAlign.TOP)
                    .gap(RightGap.SMALL)
                textArea()
                    .rows(5)
                    .horizontalAlign(HorizontalAlign.FILL)
            }.layout(RowLayout.PARENT_GRID)

            row("comboBox:") {
                comboBox(arrayOf("Item 1", "Item 2"))
            }*/
        }

        val disposable = Disposer.newDisposable()
        panel.registerValidators(disposable)
        Disposer.register(parentDisposable, disposable)

        return panel
    }
}