sub main
    call 'pre_init
    let 'f 'lastfunc
    call 'println "Blaha Muha"
    call 'test
    call 'print 15 ; call 'print " " ; call 'println 1234
    call $f

sub pre_init
    let 'secret "Great Secret!"

sub test
    call 'println "Test working!"
    call 'println $secret

sub lastfunc
    call 'println "Last Func"
