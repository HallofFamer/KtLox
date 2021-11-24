package com.mysidia.ktlox.printer

import com.mysidia.ktlox.ast.Expr

interface ExprPrinter : Expr.Visitor<String>{
    fun print(expr: Expr) = expr.accept(this)
}