package com.mysidia.ktlox.parser

import com.mysidia.ktlox.Lox
import com.mysidia.ktlox.ast.Expr
import com.mysidia.ktlox.ast.Stmt
import com.mysidia.ktlox.lexer.Token
import com.mysidia.ktlox.lexer.TokenType
import com.mysidia.ktlox.lexer.TokenType.*

class Parser(private val tokens: List<Token>) {

    private var current = 0
    private val isAtEnd get() = peek.type == EOF
    private var loopDepth = 0
    private val next get() = tokens[current + 1]
    private val peek get() = tokens[current]
    private val previous get() = tokens[current - 1]
    private val rootClass = Expr.Variable(Token(IDENTIFIER, "Object", null, 0))

    fun parse() : List<Stmt> {
        val statements = mutableListOf<Stmt>()
        while(!isAtEnd){
            statements.add(declaration() ?: continue)
        }
        return statements
    }

    private fun advance() : Token {
        if(!isAtEnd) current++
        return previous
    }

    private fun and(): Expr {
        var expr = equality()
        while(match(AND)){
            val operator = previous
            val right = equality()
            expr = Expr.Logical(expr, operator, right)
        }
        return expr
    }

    private fun assignment(): Expr {
        val expr = or()
        if(match(EQUAL)){
            val equals = previous
            val value = assignment()
            when(expr){
                is Expr.Variable -> return Expr.Assign(expr.name, value)
                is Expr.Get -> return Expr.Set(expr.obj, expr.name, value)
                else -> error(equals, "Invalid assignment target.")
            }
        }
        return expr
    }

    private fun block(): List<Stmt> {
        val statements = mutableListOf<Stmt>()
        while(!check(RIGHT_BRACE) && !isAtEnd){
            statements.add(declaration() ?: continue)
        }
        consume(RIGHT_BRACE, "Expect '}' after block.")
        return statements
    }

    private fun breakStatement(): Stmt {
        if(loopDepth == 0) error(previous, "must be inside a loop to use 'break'.")
        consume(SEMICOLON, "Expect ';' after 'break'.")
        return Stmt.Break(previous)
    }

    private fun call(): Expr {
        var expr = primary()
        while(true){
            expr = when{
                match(LEFT_PAREN) -> finishCall(expr)
                match(DOT) -> Expr.Get(expr, consume(IDENTIFIER, "Expect Proper"))
                else -> break
            }
        }
        return expr
    }

    private fun check(type: TokenType) = if(isAtEnd) false else peek.type == type

    private fun checkNext(type: TokenType) =
        if(isAtEnd || (next.type == EOF)) false
        else (next.type == type)

    private fun classDeclaration(): Stmt.Class {
        val name = consume(IDENTIFIER, "Expect class name.")
        val superclass = if(match(LESS)) Expr.Variable(consume(IDENTIFIER, "Expect superclass name.")) else rootClass
        val traits = withClause()

        consume(LEFT_BRACE, "Expect '{' before class body.")
        val methods = mutableListOf<Stmt.Function>()
        val classMethods = mutableListOf<Stmt.Function>()
        while(!check(RIGHT_BRACE) && !isAtEnd){
            if(match(CLASS)) classMethods.add(function("method"))
            else methods.add(function("method"))
        }
        consume(RIGHT_BRACE, "Expect '}' after class body.")
        return Stmt.Class(name, superclass, traits, methods, classMethods)
    }

    private fun comparison(): Expr {
        var expr = term()
        while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            val operator = previous
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun consume(type: TokenType, message: String) = if(check(type)) advance() else throw error(peek, message)

    private fun declaration(): Stmt? {
        return try{
            when{
                match(CLASS) -> classDeclaration()
                check(FUN) && checkNext(IDENTIFIER) -> functionDeclaration()
                match(TRAIT) -> traitDeclaration()
                match(VAR) -> varDeclaration()
                else -> statement()
            }
        } catch(error: ParserError){
            synchronize()
            null
        }
    }

    private fun expression() = assignment()

    private fun equality(): Expr {
        var expr = comparison()
        while(match(BANG_EQUAL, EQUAL_EQUAL)){
            val operator = previous
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun error(token: Token, message: String) : ParserError {
        Lox.error(token, message)
        return ParserError(message)
    }

    private fun expressionStatement(): Stmt {
        val expr = expression()
        consume(SEMICOLON, "Expect ';' after expression.")
        return Stmt.Expression(expr)
    }

    private fun factor(): Expr {
        var expr = unary()
        while(match(SLASH, STAR)){
            val operator = previous
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun finishCall(callee: Expr): Expr {
        val arguments = mutableListOf<Expr>()
        if(!check(RIGHT_PAREN)){
            do {
                if(arguments.size >= 16) error(peek, "Cannot have more than 16 arguments.")
                arguments.add(expression())
            }while(match(COMMA))
        }
        val paren = consume(RIGHT_PAREN, "Expect ')' after arguments")
        return Expr.Call(callee, paren, arguments)
    }

    private fun forStatement(): Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'for'.")
        val initializer = when{
            match(SEMICOLON) -> null
            match(VAR) -> varDeclaration()
            else -> expressionStatement()
        }
        val condition = if(!check(SEMICOLON)) expression() else Expr.Literal(true)
        consume(SEMICOLON, "Expect ';' after loop condition.")
        val increment = if(!check(RIGHT_PAREN)) expression() else null
        consume(RIGHT_PAREN, "Expect ';' after for clause.")

        try{
            loopDepth++
            var body = statement()
            increment?.apply { body = Stmt.Block(listOf(body, Stmt.Expression(increment))) }
            body = Stmt.While(condition, body)
            initializer?.apply { body = Stmt.Block(listOf(initializer, body)) }
            return body
        }
        finally{
            loopDepth--
        }
    }

    private fun function(kind: String): Stmt.Function {
        val name = consume(IDENTIFIER, "Expect $kind name.")
        return Stmt.Function(name, functionBody(kind))
    }

    private fun functionBody(kind: String): Expr.Function {
        var parameters : MutableList<Token>? = null
        if(kind != "method" || check(LEFT_PAREN)){
            consume(LEFT_PAREN, "Expect '(' after $kind name.")
            parameters = mutableListOf()
            if(!check(RIGHT_PAREN)){
                do {
                    if(parameters.size >= 16) error(peek, "Cannot have more than 16 parameters.")
                    parameters.add(consume(IDENTIFIER, "Expect parameter name."))
                }while (match(COMMA))
            }
            consume(RIGHT_PAREN, "Expect ')' after parameters.")
        }
        consume(LEFT_BRACE, "Expect '{' before $kind body.")
        val body = block()
        return Expr.Function(parameters, body)
    }

    private fun functionDeclaration(): Stmt.Function {
        consume(FUN, "")
        return function("function")
    }

    private fun ifStatement(): Stmt.If {
        consume(LEFT_PAREN, "Expect '(' after 'if'.")
        val condition = expression()
        consume(RIGHT_PAREN, "Expect ')' after if condition.")
        val thenBranch = statement()
        val elseBranch = if(match(ELSE)) statement() else null
        return Stmt.If(condition, thenBranch, elseBranch)
    }

    private fun match(vararg types : TokenType) : Boolean {
        types.forEach {
            if(check(it)){
                advance()
                return true
            }
        }
        return false
    }

    private fun or(): Expr {
        var expr = and()
        while(match(OR)){
            val operator = previous
            val right = and()
            expr = Expr.Logical(expr, operator, right)
        }
        return expr
    }

    private fun primary(): Expr {
        return when{
            match(FALSE) -> Expr.Literal(false)
            match(TRUE) -> Expr.Literal(true)
            match(NIL) -> Expr.Literal(null)
            match(NUMBER, STRING) -> Expr.Literal(previous.literal)
            match(FUN) -> functionBody("function")
            match(IDENTIFIER) -> Expr.Variable(previous)
            match(SUPER) -> {
                val keyword = previous
                consume(DOT, "Expect '.' after 'super'")
                Expr.Super(keyword, consume(IDENTIFIER, "Expect superclass method name."))
            }
            match(THIS) -> Expr.This(previous)
            match(LEFT_PAREN) -> {
                val expr = expression()
                consume(RIGHT_PAREN, "Expect ')' after expression.")
                Expr.Grouping(expr)
            }
            else -> throw error(peek, "Expect Expression")
        }
    }

    private fun printStatement(): Stmt.Print {
        val value = expression()
        consume(SEMICOLON, "Expect ';' after value.")
        return Stmt.Print(value)
    }

    private fun returnStatement(): Stmt.Return {
        val keyword = previous
        val value = if(!check(SEMICOLON)) expression() else null
        consume(SEMICOLON, "Expect ';' after return value.")
        return Stmt.Return(keyword, value)
    }

    private fun statement(): Stmt {
        return when {
            match(BREAK) -> breakStatement()
            match(FOR) -> forStatement()
            match(IF) -> ifStatement()
            match(PRINT) -> printStatement()
            match(RETURN) -> returnStatement()
            match(WHILE) -> whileStatement()
            match(LEFT_BRACE) -> Stmt.Block(block())
            else -> expressionStatement()
        }
    }

    private fun synchronize(){
        advance()
        while(!isAtEnd){
            if(previous.type == SEMICOLON) return
            when(peek.type){
                CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> return
                else -> continue
            }
        }
        advance()
    }

    private fun term() : Expr {
        var expr = factor()
        while(match(MINUS, PLUS)){
            val operator = previous
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun traitDeclaration(): Stmt.Trait {
        val name = consume(IDENTIFIER, "Expect trait name.")
        val traits = withClause()
        consume(LEFT_BRACE, "Expect '{' before trait body.")
        val methods = mutableListOf<Stmt.Function>()
        while(!check(RIGHT_BRACE) && !isAtEnd){
            methods.add(function("method"))
        }
        consume(RIGHT_BRACE, "Expect '}' after trait body.")
        return Stmt.Trait(name, traits, methods)
    }

    private fun unary(): Expr{
        return if(match(BANG, MINUS)){
            val operator = previous
            val right = unary()
            Expr.Unary(operator, right)
        } else call()
    }

    private fun varDeclaration(): Stmt {
        val name = consume(IDENTIFIER, "Expect variable name.")
        val initializer = if(match(EQUAL)) expression() else null
        consume(SEMICOLON, "Expect ';' after variable declaration.")
        return Stmt.Var(name, initializer)
    }

    private fun whileStatement(): Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'while'.")
        val condition = expression()
        consume(RIGHT_PAREN, "Expect ')' after condition.")
        try{
            loopDepth++
            val body = statement()
            return Stmt.While(condition, body)
        }
        finally{
            loopDepth--
        }
    }

    private fun withClause() : List<Expr.Variable>{
        val traits = mutableListOf<Expr.Variable>()
        if(match(WITH)){
            do{
                if(traits.size >= 16) error(peek, "A class cannot implement more than 16 traits.")
                val trait = Expr.Variable(consume(IDENTIFIER, "Expect trait name."))
                traits.add(trait)
            }while(match(COMMA))
        }
        return traits
    }
}