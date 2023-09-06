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

import com.intellij.openapi.util.IconLoader

object TrophyIcons {
    @JvmField
    val trophyDefaultIcon = IconLoader.getIcon("/icons/trophy-variant-outline.svg", javaClass)
    val trophyToolWindowIcon = IconLoader.getIcon("/icons/trophy-tool-window.svg", javaClass)
    val trophyBronzeIcon = IconLoader.getIcon("/icons/trophy-bronze.svg", javaClass)
    val trophySilverIcon = IconLoader.getIcon("/icons/trophy-silver.svg", javaClass)
    val trophySilverLightIcon = IconLoader.getIcon("/icons/trophy-silver-light.svg", javaClass)
    val trophyGoldIcon = IconLoader.getIcon("/icons/trophy-gold.svg", javaClass)
    val trophyPlatinIcon = IconLoader.getIcon("/icons/trophy-platin.svg", javaClass)
    val javaIcon = IconLoader.getIcon("/icons/java.svg", javaClass)
    val javaScriptIcon = IconLoader.getIcon("/icons/javascript.svg", javaClass)
}