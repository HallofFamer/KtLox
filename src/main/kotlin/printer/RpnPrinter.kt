package com.mysidia.ktlox.printer

import com.mysidia.ktlox.ast.Expr
import com.mysidia.ktlox.lexer.TokenType

class RpnPrinter : ExprPrinter{

    override fun visitAssignExpr(expr: Expr.Assign) = "= ${expr.name} ${expr.value}"

    override fun visitBinaryExpr(expr: Expr.Binary) = "${expr.left.accept(this)} ${expr.right.accept(this)} ${expr.operator.lexeme}"

    override fun visitCallExpr(expr: Expr.Call) : String{
        return if(expr.arguments == null) "property getter ${expr.callee}"
        else "call ${expr.callee}(${expr.arguments.joinToString(", ")})"
    }

    override fun visitFunctionExpr(expr: Expr.Function) = "function(${expr.body.joinToString(", ")})"

    override fun visitGetExpr(expr: Expr.Get) = "property getter ${expr.obj}.${expr.name}"

    override fun visitGroupingExpr(expr: Expr.Grouping) = expr.expression.accept(this)

    override fun visitLiteralExpr(expr: Expr.Literal) = expr.value.toString()

    override fun visitLogicalExpr(expr: Expr.Logical) = "${expr.left.accept(this)} ${expr.right.accept(this)} ${expr.operator.lexeme}"

    override fun visitSetExpr(expr: Expr.Set) = "property setter ${expr.obj}.${expr.name}=${expr.value}"

    override fun visitSuperExpr(expr: Expr.Super) = "${expr.keyword.lexeme}.${expr.method.lexeme}"

    override fun visitThisExpr(expr: Expr.This) = expr.keyword.lexeme

    override fun visitUnaryExpr(expr: Expr.Unary) : String {
        val operator = if(expr.operator.type == TokenType.MINUS) "~" else expr.operator.lexeme
        return "${expr.right.accept(this)} $operator"
    }

    override fun visitVariableExpr(expr: Expr.Variable) = "var ${expr.name}"
}