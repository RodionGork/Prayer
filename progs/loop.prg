sub main
    let 'x 10
    while $x ; call 'condprint $x ; let 'x $x -1 +

sub condprint z
    if $z 2 % ; call 'println $z
