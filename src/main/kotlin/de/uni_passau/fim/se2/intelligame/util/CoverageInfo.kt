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

package de.uni_passau.fim.se2.intelligame.util

class CoverageInfo(
    val totalClassCount: Int, val coveredClassCount: Int, val totalMethodCount: Int, val coveredMethodCount: Int,
    val totalLineCount: Int, val coveredLineCount: Int, val totalBranchCount: Int, val coveredBranchCount: Int
) {
    fun isEmpty(): Boolean {
        return (coveredClassCount == 0 && coveredMethodCount == 0 && coveredLineCount == 0 && coveredBranchCount == 0)
    }
}