package com.mysidia.ktlox.ast

import com.mysidia.ktlox.lexer.Token

abstract class Stmt {

    interface Visitor<R> {
        fun visitBlockStmt(stmt: Block) : R
        fun visitBreakStmt(stmt: Break) : R
        fun visitClassStmt(stmt: Class) : R
        fun visitFunctionStmt(stmt: Function) : R
        fun visitExpressionStmt(stmt: Expression) : R
        fun visitIfStmt(stmt: If) : R
        fun visitReturnStmt(stmt: Return) : R
        fun visitTraitStmt(stmt: Trait) : R
        fun visitVarStmt(stmt: Var) : R
        fun visitWhileStmt(stmt: While) : R
    }

    data class Block(val statements: List<Stmt>) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitBlockStmt(this)
    }

    data class Break(val token: Token) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitBreakStmt(this)
    }

    data class Class(val name: Token, val superclass: Expr.Variable, val traits: List<Expr.Variable>, val methods: List<Stmt.Function>, val classMethods: List<Stmt.Function>) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitClassStmt(this)
    }

    data class Function(val name: Token, val functionBody: Expr.Function) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitFunctionStmt(this)
    }

    data class Expression(val expression: Expr) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitExpressionStmt(this)
    }

    data class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitIfStmt(this)
    }

    data class Return(val keyword: Token, val value: Expr?) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitReturnStmt(this)
    }

    data class Trait(val name: Token, val traits: List<Expr.Variable>, val methods: List<Stmt.Function>) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitTraitStmt(this)
    }

    data class Var(val name: Token, val initializer: Expr?) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitVarStmt(this)
    }

    data class While(val condition: Expr, val body: Stmt) : Stmt(){
        override fun <R> accept(visitor: Visitor<R>) = visitor.visitWhileStmt(this)
    }

    abstract fun <R> accept(visitor: Visitor<R>) : R
}
