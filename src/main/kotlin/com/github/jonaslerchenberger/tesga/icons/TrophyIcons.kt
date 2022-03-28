package com.github.jonaslerchenberger.tesga.icons

import com.intellij.openapi.util.IconLoader

object TrophyIcons {
    @JvmField
    val trophyDefaultIcon = IconLoader.getIcon("/icons/trophy-variant.svg", javaClass)
    val trophyBronzeIcon = IconLoader.getIcon("/icons/trophy-bronze.svg", javaClass)
    val trophySilverIcon = IconLoader.getIcon("/icons/trophy-silver.svg", javaClass)
    val trophyGoldIcon = IconLoader.getIcon("/icons/trophy-gold.svg", javaClass)
}