package com.mysidia.ktlox.printer

import com.mysidia.ktlox.ast.Expr

class AstPrinter : ExprPrinter{

    override fun visitAssignExpr(expr: Expr.Assign) = "(${expr.name} = ${expr.value})"

    override fun visitBinaryExpr(expr: Expr.Binary) = parenthesize(expr.operator.lexeme, expr.left, expr.right)

    override fun visitCallExpr(expr: Expr.Call) : String {
        return if(expr.arguments == null) "property getter ${expr.callee}"
        else "call ${expr.callee}(${expr.arguments.joinToString(", ")})"
    }

    override fun visitFunctionExpr(expr: Expr.Function) = "function(${expr.body.joinToString(", ")})"

    override fun visitGetExpr(expr: Expr.Get) = "property getter ${expr.obj}.${expr.name}"

    override fun visitGroupingExpr(expr: Expr.Grouping) = parenthesize("group", expr.expression)

    override fun visitLiteralExpr(expr: Expr.Literal) = if(expr.value == null) "nil" else expr.value.toString()

    override fun visitLogicalExpr(expr: Expr.Logical) = parenthesize(expr.operator.lexeme, expr.left, expr.right)

    override fun visitSetExpr(expr: Expr.Set) = "property setter ${expr.obj}.${expr.name}=${expr.value}"

    override fun visitSuperExpr(expr: Expr.Super) = "${expr.keyword.lexeme}.${expr.method.lexeme}"

    override fun visitThisExpr(expr: Expr.This) = expr.keyword.lexeme

    override fun visitUnaryExpr(expr: Expr.Unary) = parenthesize(expr.operator.lexeme, expr.right)

    override fun visitVariableExpr(expr: Expr.Variable) = "var ${expr.name}"

    private fun parenthesize(name: String, vararg expressions: Expr) : String{
        val builder = StringBuilder()
        builder.append("($name")
        expressions.forEach {
            builder.append(" ${it.accept(this)}")
        }
        builder.append(")")
        return builder.toString()
    }
}