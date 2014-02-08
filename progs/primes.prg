sub main
  let 'nums 0 ; call 'remember 2 ; call 'remember 3
  let 'cur 4
  while $nums 1007 < ; call 'trynext
  let 'i 0
  #while $i $nums < ; call 'println 'a $i [] ; let 'i $i 1 +
  call 'println 'a $nums 1 - []

sub remember x
  let 'a $nums + $x
  let 'nums $nums 1 +

sub trynext
  let 'c 0
  while 'a $c [] dup dup * $cur <= $cur rot % and ; let 'c $c 1 +
  if 'a $c [] dup * $cur > ; call 'remember $cur
  let 'cur $cur 1 +

