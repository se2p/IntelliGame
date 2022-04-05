package com.github.jonaslerchenberger.tesga.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.PsiTreeChangeListener
import com.intellij.psi.impl.source.PsiModifierListImpl

object AddTestsAchievement: Achievement(), PsiTreeChangeListener{
    override fun progress(): Int {
        val properties = PropertiesComponent.getInstance()
        return properties.getInt("AddTestsAchievement", 0)
    }

    override fun updateProgress(progress: Int) {
        val properties = PropertiesComponent.getInstance()
        properties.setValue("AddTestsAchievement", progress, 0)
    }

    override fun getDescription(): String {
        return "Create X tests"
    }

    override fun getName(): String {
        return "Safety first"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }

    override fun beforeChildAddition(event: PsiTreeChangeEvent) {
    }

    override fun beforeChildRemoval(event: PsiTreeChangeEvent) {
    }

    override fun beforeChildReplacement(event: PsiTreeChangeEvent) {
    }

    override fun beforeChildMovement(event: PsiTreeChangeEvent) {
    }

    override fun beforeChildrenChange(event: PsiTreeChangeEvent) {
    }

    override fun beforePropertyChange(event: PsiTreeChangeEvent) {
    }

    override fun childAdded(event: PsiTreeChangeEvent) {
        if ((event.child as PsiModifierListImpl).text.equals("@Test")) {
            var progress = progress()
            progress++
            if (progress == nextStep()) {
                showAchievementNotification("Congratulations! You unlocked level " + getLevel() + " of the 'Safety first' Achievement")
            }
            updateProgress(progress)
        }
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
    }

    override fun childrenChanged(event: PsiTreeChangeEvent) {
    }

    override fun childMoved(event: PsiTreeChangeEvent) {
    }

    override fun propertyChanged(event: PsiTreeChangeEvent) {
    }
}