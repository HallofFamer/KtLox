package com.mysidia.ktlox.resolver

import com.mysidia.ktlox.lexer.Token

data class Variable(val name: Token, var state: VariableState)