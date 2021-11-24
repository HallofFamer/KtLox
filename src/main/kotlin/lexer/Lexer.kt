package com.mysidia.ktlox.lexer

import com.mysidia.ktlox.Lox
import com.mysidia.ktlox.lexer.TokenType.*

class Lexer(private val source: String) {

    private val tokens = mutableListOf<Token>()
    private var start = 0
    private var current = 0
    private var line = 1
    private val isAtEnd get() = (current >= source.length)

    fun scanTokens() : List<Token>{
        while(!isAtEnd){
            // We are at the beginning of the next lexeme
            start = current
            scanToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun scanToken(){
        when(val c = advance()){
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '%' -> addToken(MODULUS)
            '!' -> addToken(if(match('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if(match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if(match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if(match('=')) GREATER_EQUAL else GREATER)
            '/' -> addTokenForSlash()
            ' ', '\r', '\t' -> Unit
            '\n' -> line++
            '"' -> string()
            else -> addTokensForOthers(c)
        }
    }

    private fun advance() = source[current++]

    private fun addToken(type: TokenType){
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?){
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun match(expected: Char) : Boolean{
        if(isAtEnd) return false
        if(source[current] != expected) return false
        current++
        return true
    }

    private fun addTokenForSlash(){
        when{
            match('/') -> while(peek() != '\n' && !isAtEnd) advance()
            match('*') -> docBlockComments()
            else -> addToken(SLASH)
        }
    }

    private fun peek() = if(isAtEnd) '\n' else source[current]

    private fun docBlockComments(){
        var nestLevel = 1
        while(nestLevel > 0 && !isAtEnd){
            if(peek() == '\n') line++
            else if(peek() == '/' && peekNext() == '*') nestLevel++
            else if(peek() == '*' && peekNext() == '/') nestLevel--
            advance()
        }

        if(isAtEnd){
            Lox.error(line, "Undetermined docblock comment.")
        }
        else advance()
    }

    private fun string(){
        while(peek() != '"' && !isAtEnd){
            if(peek() == '\n') line++
            advance()
        }

        if(isAtEnd){
            Lox.error(line, "Unterminated string.")
            return
        }

        advance()
        addToken(STRING, source.substring(start + 1, current - 1))
    }

    private fun addTokensForOthers(c: Char){
        when{
            c.isDigit() -> number()
            isAlpha(c) -> identifier()
            else -> Lox.error(line, "Unexpected Character.")
        }
    }

    private fun isDigit(c : Char) = c in '0'..'9'

    private fun number(){
        while(isDigit(peek())) advance()
        if(peek() == '.' && isDigit(peekNext())){
            advance()
            while(isDigit(peek())) advance()
            addToken(FLOAT, source.substring(start, current).toDouble())
        }
        else addToken(INT, source.substring(start, current).toLong())
    }

    private fun peekNext() = if(current + 1 >= source.length) '\u0000' else source[current + 1]

    private fun isAlpha(c : Char) = (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'

    private fun isAlphaNumeric(c : Char) = isAlpha(c) || isDigit(c)

    private fun identifier(){
        while(isAlphaNumeric(peek())) advance()
        val type = Keywords[source.substring(start, current)] ?: IDENTIFIER
        addToken(type)
    }

    companion object{
        val Keywords = hashMapOf(
            "and" to AND,
            "break" to BREAK,
            "class" to CLASS,
            "else" to ELSE,
            "false" to FALSE,
            "for" to FOR,
            "fun" to FUN,
            "if" to IF,
            "nil" to NIL,
            "or" to OR,
            "return" to RETURN,
            "super" to SUPER,
            "this" to THIS,
            "trait" to TRAIT,
            "true" to TRUE,
            "var" to VAR,
            "while" to WHILE,
            "with" to WITH
        )
    }
}