import scala.runtime.RichInt

def sig(num: Int): Int = {
  if (num < 0) return -1
  else if (num > 0) return 1
  0
}

sig(5)
sig(-545)
sig(0)

{}

for ( i <- 10 to 0 by -1 ) println(i)
def countdown(n: Int): Unit = {
  (0 to n).reverse.foreach(println(_))
}
countdown(40)
def productLoop(str: String): Long = {
  var acc = 1L
  for (i <- str) acc *= i
  acc
}
productLoop("Hello")
def productWOLoop(str:String): Long = {
  str.map(_.toLong).product
}
productWOLoop("Hello")
def productRecurs(str: String): Long = {
  if (str.length == 0) return 1
  str.head.toLong*productRecurs(str.tail)
}
productRecurs("Hello")
def pow(x: Double, n: Int): Double = {
  if (n==0) 1
  else if (n < 0) pow(1/x, -n)
  else if (n%2 == 0) pow(x, n/2)*pow(x, n/2)
  else x*pow(x, n-1)
}
pow(2, -5)