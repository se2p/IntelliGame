package de.uni_passau.fim.se2.intelligame.services

import org.apache.commons.text.diff.CommandVisitor


class CountChangesCommandVisitor : CommandVisitor<Char> {
    var counter: Int = 0
    override fun visitInsertCommand(`object`: Char?) {
        counter++
    }

    override fun visitKeepCommand(`object`: Char?) {
    }

    override fun visitDeleteCommand(`object`: Char?) {
        counter++
    }

}