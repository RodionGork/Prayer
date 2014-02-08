Prayer
======

Small scripting programming language resembling forth, intended for driving quest game engine.

Main goal is to keep it simple and so easily portable. Here are two versions:

- `Java` version as a maven project;
- `JavaScript` version as a set of js files driven by sample html.

Sample programs are in `prog` directory.

-----

To build and run `Java` version use the following commands (make sure you have
JDK 7 and maven):

    mvn package
    
    java -jar target/Prayer.jar progs/fib.prg

The `fib.prg` demonstrates calculation of Fibonacci numbers.