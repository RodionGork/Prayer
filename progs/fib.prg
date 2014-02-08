sub main
    let 'max 1000
    let 'xx 0 ; let 'yy 1
    while $xx $max le ; call 'next_fib $xx $yy ; call 'println $xx

sub next_fib a b
    let 'xx $b ; let 'yy $a $b +

