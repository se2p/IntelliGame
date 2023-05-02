package de.uni_passau.fim.se2.intelligame.services

import org.apache.commons.text.diff.CommandVisitor


class CountChangesCommandVisitor : CommandVisitor<Char> {
    private var counter: Int = 0
    override fun visitInsertCommand(`object`: Char?) {
        counter++
    }

    override fun visitKeepCommand(`object`: Char?) = Unit

    override fun visitDeleteCommand(`object`: Char?) {
        counter++
    }

}