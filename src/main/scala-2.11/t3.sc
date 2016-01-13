


val arr = 6 to 1 by -1 toArray

val ind = 1 to arr.length map( i =>
  if (i == arr.length && arr.length % 2 != 0) arr.length
  else if(i % 2 == 0) i-1 else i+1) map(_-1)

val arrRevers = for(i <- ind) yield arr(i)

//----------------------------------------------------------

val arr2 = Array(5, -3, -9, 8, 0, 3 ,6, -2, 4)

val arrT = arr2.filter(_ > 0) ++ arr2.filter(_ <= 0)

//----------------------------------------------------------

val arr3 = Array(5.6, -3.2, -9.7, 8.4, 0.9, 3.2 ,6.3, -2.5, 4.7)

val arrRes = arr3.sum / arr3.length

//----------------------------------------------------------

val arr4 = Array(5, -3, -9, 8, 0, 3 ,6, -2, 4)

val arrRes2 = arr4.sorted.reverse

//----------------------------------------------------------

val arr5 = Array(5, -3, -9, 8, 0, 3 ,6, -2, 4, 3, -9)

//----------------------------------------------------------