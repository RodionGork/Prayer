sub main
    call 'print3 100 200 300 rot
    call 'print3 100 200 300 brot
    call 'print3 100 dup 200 swap
    call 'println "Zloba" "Dnya" +
    call 'println "Zloba" "Dnya" eq
    call 'print3 5 3 lt 3 5 lt 3 3 le
    call 'println 5 3 2 + eq

sub print3 a b c
    call 'prints $a ; call 'prints $b ; call 'println $c

sub prints a
    call 'print $a ; call 'print " "

func brot
    eval rot rot

