### Program Structure

Program consists of several **subroutines**, each identified by its name. Subroutine definition
starts with the line containing keyword `sub` followed by name. Next lines contain the body of
subroutine:

    sub main
        call "println" "Hello, World!"

Subroutine declaration `sub main` should start from the beginning of the line, without indent. All
following lines should be indented, until next declaration:

    sub main
        call "greeting"
    
    sub greeting
        call "println" "Hello, World!"

In this example subroutine `main` calls another subroutine `greeting`, which in its turn calls
built-in subroutine `println` passing it the string to print.

Default executor starts the program with running subroutine **main**.

### Statements

There are only a few types of statements. They are recognized by the first word of the statement.

**Subroutine call** as we have seen before starts with the keyword `call` which is followed by
the subroutine name (or expression evaluated to subroutine name) and, possibly, more expressions
to be evaluated to subroutine arguments. For example the line `call "println" "Hello"` requires
the executing system to call subroutine **println** passing it string **Hello** as argument.

**Assignment** of a value to variable is described by keyword `let` which is then followed by
the name of the variable (or expression evaluated to subroutine name) and expression the value of
which should be written to variable:

    sub main
        let "x" 5
        let "hi" "Hello, People!"
        call "println" $hi

Here the value `5` is assigned to variable named `x` and string value `Hello, People!` is
assigned to variable `hi`. As you may guess later `$hi` is used to fetch the value of the
variable with the name `hi` - i.e. dollar sign `$` is a prefix for getting variable value.

**Evaluation** statement is started with keyword `eval` it does nothing except evaluating the
expression following this keyword. We shall see later when this could be useful.

**Combined** statement is created by putting several statements into one line, separating them
with semicolon `;` like following:

    sub main
        call "print" "Hello, " ; call "println" "World!"

Note (it is **important**) that semicolon should be separated with spaces on both sides!

**Conditional** statement is not used on its own. It should always be a part of a combined
statement, like this:

    sub conditional_print
        if $x 5 = ; call "println" "X equals to five!"

As you see, conditional statement is started with keyword `if` which is followed by an expression.
You also see from here that expressions are written in postfix notation (i.e. equality operator
is following its two operands) - you will read about it later.

If the expression after `if` is calculated to be non-zero, the rest part of combined statement
is executed. Otherwise it is skipped.

**Loop** statement is similar to conditional. It is started with keyword `while` and the following
part of the combined statement is executed several time, while the expression after `while` is
evaluated to be non-zero:

    sub numbers
        let "x" 10
        while $x ; call "println" $x ; let "x" $x 1 -

Here the second part of combined expression prints the value of `x` and third part decrements the
value of `x` so that the whole statement is executed ten times until `x` becomes zero.

### Expressions

Expressions are evaluated on the special stack, like in **Forth** programming language or in the
processor of **Java Virtual Machine**. The evaluation is performed straightforward through all
components of expression, by the following rules:

- if a **number** (like `1` or `500`) is encountered, it is pushed to stack;
- if a **string** (like "Hello") is encountered, it is pushed to stack too - string is recognized
by being enclosed into double quotes or by being started with a single quote which is useful for
short space-less strings, i.e. `"println"` is equivalent to `'println`;
- if a token is prefixed by sign `$` then the value of the corresponding variable is fetched and
pushed to stack;
- other tokens are considered to be operators or function calls.

Operators usually take one or more operands from the top of the stack, perform an operation and
put the result back to stack. For example:

    sub main
        call 'println 5 3 +

Here execution works as following: the keyword `call` is recognized so the system is ready to
perform subroutine call; then the rest is evaluated:

- `'println` is recognized as a string and is pushed to stack;
- `5` is recognized as a number and is pushed to stack;
- `3` is recognized as a number and is pushed to stack;
- `+` is an operator - it pops two last values from stack (`3` and `5`), adds them together and
then pushes the result (`8`) to stack.

So when expressions are evaluated, two values are left on the stack - the string `println` and the
number `8`. The first is interpreted by `call` to be the name of subroutine, the latter becomes
an argument for subroutine.

Most binary operators work similarly:

    sub main
        let 'x 9
        call 'println $x $x *
        call 'println $x 3 /
        call 'println $x 5 >
        call 'println $x 8 <=

This program will printl values of `81`, `3`, `1` and `0` (you see, logical operators have result
of `0` for `false` and `1` for `true`).

The full list of built-in operators could be found further.

### Tricks

Since subroutine `call` statement uses a string value as a name of subroutine, it could be
evaluated in runtime:

    sub main
        let 'name "println"
        call $name 15

This could be useful for building some flexible code with self-modifying behavior.

Similarly, the names of variables could be constructed in the runtime:

    sub main
        let 'a 1 + 10
        let 'a 2 + 50
        call 'println $a1
        call 'println $a2

In this example the variable name is concatenated of a prefix `a` and index (`1` or `2`). This
is useful when we need an array in our program. Consider the following example:

    sub main
        let 'total 10
	    call 'calc_squares
        call 'print_values

    sub calc_squares
        let 'i 1
        while $i $total <= ; let 'val $i + $i $i * ; let 'i $i 1 +

    sub print_values
        let 'i 1
        while $i $total <= ; call 'println 'val $i [] ; let 'i $i 1 +

Here `main` subroutine assigns `10` to variable `total` and calls `calc_squares` and then
`print_values` subroutines.

The `calc_squares` have a loop inside, which runs variable `i` from `1` to `10` and calculates
the square of it (by `$i $i *` expression) which is assigned to variables from `val1` to `val10`
thanks to `let 'val $i + ...` statement.

The `print_values` also runs a loop for `i` from `1` to `10` and fetches variables from `val1` to
`val10`, printing their values.

Fetching is performed by special binary operator `[]`. Obviously we could not use usual dollar
sign to fetch the variable with the name not evaluated yet. Instead such operator pops out two
last values from the stack (i.e. string `val` and the value of variable `i`), concatenates these
two values (so that string like `val1`, `val3` etc. is formed) and then fetches the value from
variable with such name.

### Variables and arguments

Variables have different scope, which is determined by the length of their names:

- variables with name consisting of a single character (like `a`, `i` or `x`) are considered to
be **local** variables, visible only in the current subroutine;
- variables with longer names are **global**, shared between subroutines.

For example:

    sub main
        let 'x 5
        let 'xaxa 10
        call 'change
        call 'println $x
        call 'println $xaxa
    
    sub change
        let 'x 7
        let 'xaxa 20

This code will print values `5` and `20` because changing variable `x` inside subroutine `change`
does not affect `x` in `main`, while `xaxa` is shared between both subroutines and is changed.

Special case of local variables are subroutine **arguments**. They are represented by a single
char variable names following the definition of the subroutine. When subroutine is called, the
same amount of values are popped out from stack and are assigned to local variables with those
names. For example:

    sub main
        call 'print_three 5 9 30
    
    sub print_three a b c
        call 'print $a
        call 'print " "
        call 'print $b
        call 'print " "
        call 'println $c

Here when `call` statement is executed we have `4` values on the stack - subroutine name and then
three numbers. Subroutine with the required name (`print_three`) is found and is determined to
have `3` arguments. So `3` values are popped out from stack before executing this subroutine and
are assigned to its local variables `a`, `b` and `c` (so `5` goes to `a` etc.)

After execution you will see line `5 9 30` printed.

### Built-ins

Several subroutines and operators are predefined:

- `print` and `println` are subroutines which pop a single value from stack and print it to
output (println goes to new-line after that);
- `+`, `-`, `*`, `/`, `%` are binary operators popping two values from stack and pushing the
result back;
- `add`, `sub`, `mul`, `div`, `mod` are just other names for the same operators;
- `+` or `add` could be also applied to values one or both of which is a string as a concatenation
operator;
- `neg` takes a numeric value from stack and pushes its negation (i.e. `-5` for `5` etc.);
- `=`, `!=`, `>`, `<`, `>=`, `<=` are comparison operators for comparing numbers, they return `1`
or `0` to stack; they have synonyms `eq ne gt lt ge le` respectively, while `>=` also have a form
of `=>` for convenience and `!=` has another form `<>`;
- `=` and `!=` could also be applied to strings;
- `!` or `not` performs a logical negation converting non-zero values to zero and vice versa;
- `&&` and `||` are logical AND and OR, they also have forms `and` and `or` respectively;
- `atoi` pops a string, converts it to integer and pushes back;
- `itoa` on contrary converts integer to string;
- `?int` pops a value and pushes back either `1` if it was a number or `0` otherwise (its synonym
is `isint`);
- `?str` or `isstr` similarly checks the value from stack for being a string;
- `[]` or `peek` takes two values from stack, concatenates them to create a variable name and then
fetches the value of such variable and pushes it to stack.

**Stack manipulation operators** are similar to ones in **Forth** language:

- `drop` pops and loses the value from stack (to remove unnecessary values);
- `dup` duplicates the value at the top of stack (i.e. pops a value and pushes it twice);
- `swap` pops two last values and pushes them back in different order;
- `over` peeks at the second value (one below the top) on the stack and pushes this value
at a top; (so stack `... 2 3 4` turns to `... 2 3 4 3`);
- `rot` rotates three topmost values, so stack `... 1 2 3 4` becomes `... 1 3 4 2` i.e. third
element is moved to top.

