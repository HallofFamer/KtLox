package com.mysidia.ktlox

import java.io.PrintWriter
import java.util.*
import kotlin.system.exitProcess

object Ast {

    @JvmStatic
    fun main(args: Array<String>) {
        if(args.size != 1){
            System.err.println("Usage: generate_ast <output directory>")
            exitProcess(64)
        }
        val outputDir = args[0]

        val grammarExprs = listOf(
            "Assign     : Token name, Expr value",
            "Binary     : Expr left, Token operator, Expr right",
            "Call       : Expr callee, Token paren, List<Expr>? arguments",
            "Function   : List<Token>? params, List<Stmt> body",
            "Get        : Expr obj, Token name",
            "Grouping   : Expr expression",
            "Literal    : Any? value",
            "Logical    : Expr left, Token operator, Expr right",
            "Set        : Expr obj, Token name, Expr value",
            "Super      : Token keyword, Token method",
            "This       : Token keyword",
            "Unary      : Token operator, Expr right",
            "Variable   : Token name"
        )
        defineAst(outputDir, "Expr", grammarExprs)

        val grammarStmts = listOf(
            "Block      : List<Stmt> statements",
            "Break      : Token token",
            "Class      : Token name, Expr.Variable superclass, List<Expr.Variable> traits, List<Stmt.Function> methods, List<Stmt.Function> classMethods",
            "Function   : Token name, Expr.Function functionBody",
            "Expression : Expr expression",
            "If         : Expr condition, Stmt thenBranch, Stmt? elseBranch",
            "Return     : Token keyword, Expr? value",
            "Trait      : Token name, List<Expr.Variable> traits, List<Stmt.Function> methods",
            "Var        : Token name, Expr? initializer",
            "While      : Expr condition, Stmt body"
        )
        defineAst(outputDir, "Stmt", grammarStmts)
    }

    private fun defineAst(outputDir: String, baseName: String, types: List<String>) {
        val path = "$outputDir/$baseName.kt"
        val writer = PrintWriter(path, "UTF-8")
        writer.println("package com.mysidia.ktlox.ast")
        writer.println()
        writer.println("import com.mysidia.ktlox.lexer.Token")
        writer.println()
        writer.println("abstract class $baseName {")
        writer.println()
        defineVisitor(writer, baseName, types)

        writer.println()
        types.forEach {
            val className = it.split(":")[0].trim()
            val fields = it.split(":")[1].trim()
            defineType(writer, baseName, className, fields)
        }

        writer.println("    abstract fun <R> accept(visitor: Visitor<R>) : R")
        writer.println("}")
        writer.close()
    }

    private fun defineType(writer: PrintWriter, baseName: String, className: String, fieldList: String) {
        writer.print("    data class $className(")
        val fields = if(fieldList.isEmpty()) listOf() else fieldList.split(", ")
        fields.forEachIndexed{ index, it ->
            val type = it.split(" ")[0]
            val name = it.split(" ")[1]
            writer.print("val $name: $type")
            if(index < fields.size - 1) writer.print(", ")
        }
        writer.println(") : $baseName(){")
        writer.println("        override fun <R> accept(visitor: Visitor<R>) = visitor.visit$className$baseName(this)")
        writer.println("    }")
        writer.println()
    }

    private fun defineVisitor(writer: PrintWriter, baseName: String, types: List<String>){
        writer.println("    interface Visitor<R> {")
        types.forEach {
            val typeName = it.split(":")[0].trim()
            writer.println("        fun visit$typeName$baseName(${baseName.lowercase(Locale.getDefault())}: $typeName) : R")
        }
        writer.println("    }")
    }
}