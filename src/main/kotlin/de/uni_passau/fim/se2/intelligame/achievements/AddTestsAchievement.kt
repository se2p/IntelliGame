package de.uni_passau.fim.se2.intelligame.achievements

import com.intellij.ide.util.PropertiesComponent
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.PsiTreeChangeListener
import com.intellij.psi.impl.source.PsiModifierListImpl

object AddTestsAchievement : Achievement(), PsiTreeChangeListener {
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
        return "Safety First"
    }

    override fun getStepLevelMatrix(): LinkedHashMap<Int, Int> {
        return linkedMapOf(0 to 10, 1 to 100, 2 to 1000, 3 to 10000)
    }

    override fun beforeChildAddition(event: PsiTreeChangeEvent) = Unit

    override fun beforeChildRemoval(event: PsiTreeChangeEvent) = Unit

    override fun beforeChildReplacement(event: PsiTreeChangeEvent) = Unit

    override fun beforeChildMovement(event: PsiTreeChangeEvent) = Unit

    override fun beforeChildrenChange(event: PsiTreeChangeEvent) = Unit

    override fun beforePropertyChange(event: PsiTreeChangeEvent) = Unit

    override fun childAdded(event: PsiTreeChangeEvent) {
        if ((event.child is PsiModifierListImpl) && (event.child as PsiModifierListImpl).text.equals("@Test")) {
            var progress = progress()
            progress++
            handleProgress(progress)
        }
    }

    override fun childRemoved(event: PsiTreeChangeEvent) = Unit

    override fun childReplaced(event: PsiTreeChangeEvent) = Unit

    override fun childrenChanged(event: PsiTreeChangeEvent) = Unit

    override fun childMoved(event: PsiTreeChangeEvent) = Unit

    override fun propertyChanged(event: PsiTreeChangeEvent) = Unit
}