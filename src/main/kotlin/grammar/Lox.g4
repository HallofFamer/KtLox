grammar Lox;

program: declaration* EOF;
declaration: classDecl | funDecl | traitDecl | varDecl | statement;
classDecl: CLASS IDENTIFIER (LT IDENTIFIER)? (WITH parameters)? LBRACE CLASS? function* RBRACE;
funDecl: FUN function;
traitDecl: TRAIT IDENTIFIER (WITH parameters);
varDecl: VAR IDENTIFIER (EQ expression)? SEMICOLON;

statement: breakStmt | exprStmt | forStmt | ifStmt | printStmt | returnStmt | whileStmt | block;
breakStmt: BREAK SEMICOLON;
exprStmt: expression SEMICOLON;
forStmt: FOR LPAREN (varDecl | exprStmt)? SEMICOLON expression? SEMICOLON expression? RPAREN statement;
ifStmt: IF LPAREN expression RPAREN statement (ELSE statement)?;
printStmt: PRINT exprStmt;
returnStmt: RETURN expression? SEMICOLON;
whileStmt: WHILE LPAREN expression RPAREN statement;
block: LBRACE declaration* RBRACE;

expression: assignment;
assignment: ((call DOT)? IDENTIFIER EQ assignment) | logicOr;
logicOr: (logicAND OR logicAND)*;
logicAND: (equality AND equality)*;
equality: (comparison (BangEQ | EQEQ) comparison)*;
comparison: (term (LT | LTEQ | GT | GTEQ) term)*;
term: (factor (PLUS | MINUS) factor)*;
factor: (unary (STAR | SLASH) unary)*;
unary: ((BANG | MINUS) unary) | call;
call: primary (LPAREN arguments? RPAREN | DOT IDENTIFIER)*;
primary: 'nil' | 'true' | 'false' | NUMBER | STRING | IDENTIFIER | (LPAREN expression RPAREN) | (FUN functionBody) | (SUPER DOT IDENTIFIER);

function: IDENTIFIER functionBody;
functionBody: LPAREN parameters? RPAREN block;
parameters: IDENTIFIER (COMMA IDENTIFIER)*;
arguments: expression (COMMA expression)*;

SEPARATOR: [ \t\r\n];
KEYWORD: AND | BREAK | CLASS | ELSE | FOR | FUN | IF | OR | PRINT | RETURN | SUPER | TRAIT | VAR | WHILE | WITH;
AND: 'and';
BREAK: 'break';
CLASS: 'class';
ELSE: 'else';
FOR: 'for';
FUN: 'fun';
IF: 'if';
OR: 'or';
PRINT: 'print';
RETURN: 'return';
SUPER: 'super';
TRAIT: 'trait';
VAR: 'var';
WHILE: 'while';
WITH: 'with';
DOT: '.';
COMMA: ',';
SEMICOLON: ';';
EQ: '=';
LBRACE: '{';
RBRACE: '}';
LPAREN: '(';
RPAREN: ')';
EQEQ: '==';
BangEQ: '!=';
LT: '<';
LTEQ: '<=';
GT: '>';
GTEQ: '>=';
PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';

NUMBER: DIGIT+ (DOT DIGIT+)?;
STRING: '\'" (.)*? "\'';
IDENTIFIER: ALPHA (ALPHA | DIGIT)*;
ALPHA: [a-zA-Z_]+;
DIGIT: [0-9];