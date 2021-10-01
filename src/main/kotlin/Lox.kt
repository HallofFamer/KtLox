package com.mysidia.ktlox

import com.mysidia.ktlox.interpreter.*
import com.mysidia.ktlox.lexer.*
import com.mysidia.ktlox.parser.Parser
import com.mysidia.ktlox.resolver.Resolver
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

object Lox{

    private val interpreter = Interpreter()
    private var hadError = false
    private var hadRuntimeError = false

    private fun runFile(path: String){
        val bytes = Files.readAllBytes(Paths.get(path))
        run(String(bytes, Charset.defaultCharset()))
        if(hadError) exitProcess(65)
        if(hadRuntimeError) exitProcess(70)
    }

    private fun runPrompt(){
        val reader = BufferedReader(InputStreamReader(System.`in`))
        while(true){
            print("> ")
            val line = reader.readLine() ?: break
            run(line)
            hadError = false
        }
    }

    private fun run(source: String){
        val lexer = Lexer(source)
        val tokens = lexer.scanTokens()
        val parser = Parser(tokens)
        val stmts = parser.parse()
        if(hadError) return
        val resolver = Resolver(interpreter)
        resolver.resolve(stmts)
        if(hadError) return
        interpreter.interpret(stmts)
    }

    fun error(line: Int, message: String){
        report(line, "", message)
    }

    fun error(token: Token, message: String){
        if(token.type == TokenType.EOF) report(token.line, " at end", message)
        else report(token.line, " at '" + token.lexeme + "'", message)
    }

    private fun report(line: Int, where: String, message: String){
        System.err.println("[line $line] Error$where: $message" )
        hadError = true
    }

    fun runtimeError(error: RuntimeError) {
        System.err.println("${error.message}\n[line ${error.token.line}]")
        hadRuntimeError = true
    }

    @JvmStatic
    fun main(args: Array<String>) {
        when(args.size){
            0 -> runPrompt()
            1 -> runFile(args[0])
            else -> {
                println("Usage: ktlox [script]")
                exitProcess(64)
            }
        }
    }
}