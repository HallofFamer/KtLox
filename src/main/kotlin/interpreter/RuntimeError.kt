package com.mysidia.ktlox.interpreter

import com.mysidia.ktlox.lexer.Token

class RuntimeError(val token: Token, message: String) : RuntimeException(message)