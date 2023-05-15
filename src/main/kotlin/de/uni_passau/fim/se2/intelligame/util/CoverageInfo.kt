package de.uni_passau.fim.se2.intelligame.util

class CoverageInfo(
    val totalClassCount: Int, val coveredClassCount: Int, val totalMethodCount: Int, val coveredMethodCount: Int,
    val totalLineCount: Int, val coveredLineCount: Int, val totalBranchCount: Int, val coveredBranchCount: Int
) {
    fun isEmpty(): Boolean {
        return (coveredClassCount == 0 && coveredMethodCount == 0 && coveredLineCount == 0 && coveredBranchCount == 0)
    }
}