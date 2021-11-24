# KtLox
Kotlin Treewalk Interpreter for Lox

## Introduction
KtLox is an implementation of the programming language Lox in Kotlin. Currently it uses naive treewalk interpreter, and is not optimized for speed. The initial version of KtLox has only features already present in the original JLox/CLox, but subsequent versions will continue to add new features to make it a powerful language. This is an experiment on the design and implementation of language features.

The original version of Lox programming language can be found at Bob Nystrom's repository:
https://github.com/munificent/craftinginterpreters

## Features
- Lexer, Parser and Treewalk Interpreter
- Unary and Binary Expression/Operators
- If and Else Statement
- While and For Loop
- Break Statement(challenge from the book)
- Scope and Environment
- Functions and Closures
- Resolver and Semantic Analysis
- Anonymous Functions(challenge from the book)
- Classes and Objects
- Methods and Property Getters
- Inheritance and this/super keywords
- Metaclasses(challenge from the book)
- Traits(challenge from the book)
- Framework for creating native functions/classes(since v1.1.0)

## Roadmap

### KtLox v1.1.0(current version)
- Improved object model - Everything is an object, including nil, true, false, number, string, etc.
- Framework for writing Native functions and classes.
- Root class Object which serves as superclass of every class.
- Remove print statement and replace it by print/println native functions.

### KtLox v1.2.0(next version)
- Full Fledged Standard Library: Boolean, Number, String, Array, Dictionary, DateTime, etc.
- Move all native classes from common package to the std.lang package.
- Allow customized runtime configurations for KtLox at startup with config.properties
- Split the Number class, which will distinguish between integers and floating numbers.

### KtLox v1.3.0
- Array/Dictionary Literals and square bracket notation for array/dictionary access.
- Short closures/lambda expression with nonlocal returns.
- Replace C style for loop by Kotlin style for-in loop for collection.
- Improved Collection framework/library for KtLox
- (maybe) Null-safe operator (?.).

### KtLox v1.4.0
- Immutable variable declaration with **val**.
- Function/method parameters are immutable by default(unless var keyword is used).
- Classes, functions and traits are immutable variables. 
- (maybe) Allow properties/instance variables to be defined inside the class statements, instead of initializers.

### KtLox v1.5.0
- Refinement of metaclass system to match smalltalk's metaobject protocol.
- Improvement of trait system in KtLox.
- Add some Metaclasses and traits to the standard library.
- (maybe) Add feature for anonymous class and trait.

### KtLox v1.6.0
- Introduction of Namespace for KtLox's module system.
- Allow importing namespaces and aliasing of imported classes/functions.
- Redesign the existing standard library with namespaces(KtLox.Standard package).

### KtLox v1.7.0
- Exception handling: throw statement, try/catch/finally statement, etc.
- Add class Exception as well as a few more exception subclasses to the standard library.
- (maybe) Pattern Matching

### KtLox v1.8.0
- Operator Overloading: enable user defined classes to overload operators, these operators are treated as method calls.
- Method interception when an undefined method call is invoked on an object/class, similar to Smalltalk's doesNotUnderstand: message.
- (maybe) Semicolon inference that allows semicolons to be omitted when its obvious that the statement is finished at the end of the line.

### KtLox v1.9.0
- Introduction of async and await keywords, which allows C#/JS style of concurrency.
- Add class Promise(or Future) to the standard library, which represents an async task to be completed in future.
- (maybe) Continuation and Context, similar to smalltalk style stack manipulation.

### KtLox v2.0.0
- Optional static typing support for instance variables and function/method parameters.
- (maybe) Type inference for immutable variables, as well as possible optimization for typed variables.
- (maybe) Slot as reified variables, similar to Pharo Smalltalk and Self's implementation. 
